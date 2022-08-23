package org.saeta.controller;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.provider.X509CRLParser;
import org.bouncycastle.x509.util.StreamParsingException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
public class CertificateIntoFunctions {
    // translates OIDs "Friendly" names into something more friendly.
    //http://stackoverflow.com/a/6886721
    // these are the Ldap Names!! Java Sucks!
    static public HashMap<String, String> DistinguishedNames = new HashMap<String, String>()
    {{
        put("GN", "Nombre: ");
        put("G", "Nombre: ");
        put("I", "Inciales: ");
        put("SN", "Apellido: ");
        put("T", "Título: ");
        put("C", "País: ");

        // estos están así en los certificados de la reniec
        put("GIVENNAME", "Nombre: ");
        put("SURNAME", "Apellido: ");

        put("PostalCode", "Código Postal: ");
        put("S", "Estado: ");
        put("ST", "Estado o Provincia: ");
        put("STREETADDRESS", "Estado o Provincia: ");
        put("STATE", "Estado: ");
        put("L", "Ciudad o Localidad: ");
        put("O", "Organización: ");
        put("OU", "Unidad de la Organización: ");
        put("CN", "Entidad: ");
        put("CA", "Autoridad de Certificación: ");
        put("E", "e-mail: ");
        put("MAIL", "e-mail: ");
        put("DC", "Componente de Dominio: ");
        put("STREET", "Dirección: ");
        put("SERIALNUMBER", "Número de Serie: ");
        put("UNSTRUCTUREDNAME", "Nombre: ");
        put("UNSTRUCTUREDADDRESS", "Dirección: ");
        put("NotBefore", "Válido a partir de: ");
        put("NotAfter", "Válido hasta: ");
    }};

    public enum valoresVerificacion
    {
        None,
        MarcaDeUsoInvalida,
        NoEsNoRepudio,
        Expirado,
        AntesPeriodoValido,
        CrlNoValida,
        CrlFormatoNoValido,
        CrlNoseHaPodidoDescargar,
        Revocado,
        CertificadoRaiz,
        DocumentoAlterado,
        selloTiempoInvalido,
        noSeDescargoTSL,
        noSeEncontroCertificadoRaizEnLaTSL;

        @Override
        public String toString()
        {
            return this.name();
        }
    };

    public Certificado validateCerts(X509Certificate cert, String rutatsl, boolean isTimeStamp, int i) throws CertificateParsingException, IOException, StreamParsingException, InvalidNameException, org.bouncycastle.x509.util.StreamParsingException, ParserConfigurationException, SAXException
    {
        Date fechahoy = new Date();
        Certificado certData = new Certificado();

        // extraigo los datos del certificado
        X509Name issDN = (X509Name)cert.getIssuerDN();
        X509Name subjDN = (X509Name)cert.getSubjectDN();

        certData.addCertValuesIssuer(issDN);
        certData.addCertValuesSubject(subjDN);
        certData.setNotAfter(cert.getNotAfter());
        certData.setNotBefore(cert.getNotBefore());
        certData.setEsDelSellodeTIempo(isTimeStamp);

        if (i == 0)
        {
            certData.setUsoscertificdo(getKeyUsage(cert));

            if (certData.getUsoscertificdo() != null)
            {
                // el certificado no es de No Repudio y no es para firmas digitales
                if (!(certData.getUsoscertificdo().contains("nonRepudiation") || certData.getUsoscertificdo().contains("digitalSignature")))
                    certData.v_result.add(valoresVerificacion.MarcaDeUsoInvalida);

                if (!certData.getUsoscertificdo().contains("nonRepudiation"))
                    certData.v_result.add(valoresVerificacion.NoEsNoRepudio);
            }
            else
                certData.v_result.add(valoresVerificacion.MarcaDeUsoInvalida);

        }

        try
        {
            cert.checkValidity(fechahoy);
        }
        catch (CertificateNotYetValidException cex)
        {
            certData.v_result.add(valoresVerificacion.AntesPeriodoValido);
        }
        catch (CertificateExpiredException cex2)
        {
            certData.v_result.add(valoresVerificacion.Expirado);
        }

        List<String> listacrl = getCRLs(cert);
        int itemscrl = listacrl.size();

        if (itemscrl == 0 && !rutatsl.trim().isEmpty())
        {
            if (cert.getIssuerDN().equals(cert.getSubjectDN()))
            {
                certData.v_result.add(valoresVerificacion.CertificadoRaiz);

                byte[] filetsl = descargar(rutatsl);
                if (filetsl != null)
                {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setNamespaceAware(true);
                    Document doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(filetsl));
                    NodeList nList1 = doc.getElementsByTagName("tsl:X509Certificate");

                    boolean encontrotsl = false;

                    for (int temp = 0; temp < nList1.getLength(); temp++)
                    {
                        Node nNode = nList1.item(temp);
                        X509Certificate autoridades = generaX509Certificate(nNode.getTextContent());

                        if (autoridades.equals(cert))
                        {
                            encontrotsl = true;
                            break;
                        }
                    }

                    if (!encontrotsl)
                    {
                        certData.v_result.add(valoresVerificacion.noSeEncontroCertificadoRaizEnLaTSL);
                    }
                }
                else
                {
                    certData.v_result.add(valoresVerificacion.noSeDescargoTSL);
                }
            }
        }

        byte[] b_crl;

        for (int j = 0; j < listacrl.size(); j++)
        {
            boolean ycrlvalida = false;

            // http://www.aspnet-answers.com/microsoft/NET-Security/33503975/check-a-certificate-in-crl.aspx
            b_crl = downloadCRL(listacrl.get(j));

            try
            {
                X509CRLParser x509Parser = new X509CRLParser();
                x509Parser.engineInit(new ByteArrayInputStream(b_crl));
                X509CRL myCrl = (X509CRL)x509Parser.engineRead(); // va a tirar una excepción si el formato no es el correcto

                if (!(myCrl.getNextUpdate().after(fechahoy) && myCrl.getThisUpdate().before(fechahoy)))
                {
                    // esta crl no es válida
                    x509Parser = new X509CRLParser();
                    x509Parser.engineInit(new ByteArrayInputStream(b_crl));
                    myCrl = (X509CRL)x509Parser.engineRead(); // va a tirar una excepción si el formato no es el correcto

                    if (!(myCrl.getNextUpdate().after(fechahoy) && myCrl.getThisUpdate().before(fechahoy)))
                        certData.v_result.add(valoresVerificacion.CrlNoValida);
                    else
                        ycrlvalida = true;
                }
                else
                    ycrlvalida = true;
            }
            catch (Exception e)
            {
                certData.v_result.add(valoresVerificacion.CrlFormatoNoValido);
            }


            // se tiene una crl válida Yay! :D :D :D
            if (ycrlvalida)
            {
                X509CRLParser x509Parser = new X509CRLParser();
                x509Parser.engineInit( new ByteArrayInputStream(b_crl));
                X509CRL myCrl = (X509CRL)x509Parser.engineRead(); // va a tirar una excepción si el formato no es el correcto

                if (myCrl.isRevoked(cert))
                {
                    certData.v_result.add(valoresVerificacion.Revocado);
                    break; // ya no chequeo más crls, con ésta basta
                }
            }
        } // fin del bucle que recorre la lista de crls

        if (i == 0 && cert.getIssuerDN().equals(cert.getSubjectDN()))
            certData.v_result.add(valoresVerificacion.CertificadoRaiz);

        return certData;
    }

    public byte [] descargar(String urlString) throws MalformedURLException, IOException
    {
        URL url = new URL(urlString);
        URLConnection con = url.openConnection();
        con.setConnectTimeout(150000);
        con.setReadTimeout(150000);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try (final BufferedInputStream in = new BufferedInputStream(con.getInputStream()))
        {
            byte[] data = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1)
            {
                buffer.write(data, 0, count);
            }
        }

        buffer.close();
        return buffer.toByteArray();
    }

    private X509Certificate generaX509Certificate(String certEntry) throws IOException
    {
        certEntry = "-----BEGIN CERTIFICATE-----\n" + certEntry + "\n-----END CERTIFICATE-----";
        InputStream in = null;
        X509Certificate cert = null;

        try
        {
            byte[] certEntryBytes = certEntry.getBytes();
            in = new ByteArrayInputStream(certEntryBytes);
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            cert = (X509Certificate) certFactory.generateCertificate(in);
        }
        catch (CertificateException ex) { }
        finally
        {
            if (in != null)
                in.close();
        }
        return cert;
    }

    private LinkedList<String> getKeyUsage(final X509Certificate cert)
    {
        final boolean[] keyUsage = cert.getKeyUsage();

        final String[] keyUsageLabels = new String[] {
                "digitalSignature", "nonRepudiation", "keyEncipherment",
                "dataEncipherment", "keyAgreement", "keyCertSign", "cRLSign",
                "encipherOnly", "decipherOnly" };

        if (keyUsage != null)
        {
            final LinkedList<String> ret = new LinkedList<>();

            for (int i = 0; i < keyUsage.length; ++i)
                if (keyUsage[i])
                {
                    if (i < keyUsageLabels.length)
                        ret.add(keyUsageLabels[i]);
                    else
                        ret.add(String.valueOf(i));
                }

            return ret;
        }
        else
            return null;
    }


    private byte[] downloadCRL(String urlString) throws MalformedURLException, IOException
    {
        URL url = new URL(urlString);
        URLConnection con = url.openConnection();
        con.setConnectTimeout(20000);
        con.setReadTimeout(20000);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (final BufferedInputStream in = new BufferedInputStream(con.getInputStream()))
        {
            byte[] data = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1)
            {
                buffer.write(data, 0, count);
            }
        }
        buffer.close();
        return buffer.toByteArray();
    }

    // http://www.nakov.com/tag/crl-distribution-point/
    private LinkedList<String> getCRLs(X509Certificate cert) throws CertificateParsingException, IOException
    {
        byte[] crldpExt = cert.getExtensionValue(X509Extensions.CRLDistributionPoints.getId());
        if (crldpExt == null)
        {
            LinkedList<String> emptyList = new LinkedList<String>();
            return emptyList;
        }
        ASN1InputStream oAsnInStream = new ASN1InputStream(new ByteArrayInputStream(crldpExt));
        ASN1Object derObjCrlDP = oAsnInStream.readObject();
        DEROctetString dosCrlDP = (DEROctetString) derObjCrlDP;
        byte[] crldpExtOctets = dosCrlDP.getOctets();
        ASN1InputStream oAsnInStream2 = new ASN1InputStream(new ByteArrayInputStream(crldpExtOctets));
        ASN1Object derObj2 = oAsnInStream2.readObject();
        CRLDistPoint distPoint = CRLDistPoint.getInstance(derObj2);
        LinkedList<String> crlUrls = new LinkedList<String>();
        for (DistributionPoint dp : distPoint.getDistributionPoints())
        {
            DistributionPointName dpn = dp.getDistributionPoint();
            // Look for URIs in fullName
            if (dpn != null)
            {
                if (dpn.getType() == DistributionPointName.FULL_NAME)
                {
                    GeneralName[] genNames = GeneralNames.getInstance(dpn.getName()).getNames();
                    // Look for an URI
                    for (GeneralName genName : genNames)
                    {
                        if (genName.getTagNo() == GeneralName.uniformResourceIdentifier)
                        {
                            String url = DERIA5String.getInstance(genName.getName()).getString();
                            crlUrls.add(url);
                        }
                    }
                }
            }
        }
        return crlUrls;
    }

    public String getTitulo(X509Certificate certificado)
    {
        String dn = certificado.getSubjectDN().toString();
        String titulo0 = "";
        try
        {
            LdapName ln = new LdapName(dn);
            for (Rdn rdn : ln.getRdns())
            {
                if (rdn.getType().equalsIgnoreCase("T"))
                {
                    titulo0 = rdn.getValue().toString();
                    break;
                }
            }
        } catch (Exception e)
        {
        }
        return titulo0;
    }

    public String getAreaOrg(X509Certificate certificado)
    {
        String dn = certificado.getSubjectDN().toString();
        String area = "";
        try
        {
            LdapName ln = new LdapName(dn);
            for (Rdn rdn : ln.getRdns())
            {
                if (rdn.getType().equalsIgnoreCase("OU"))
                {
                    area = rdn.getValue().toString();
                    break;
                }
            }
        } catch (Exception e)
        {
        }
        return area;
    }

    public String validateCrls(X509Certificate cert) throws CertificateParsingException, IOException, StreamParsingException, UnknownHostException
    {
        List<String> listacrl = getCRLs(cert);
        String infoCrl = "crl valida";

        if (listacrl.isEmpty())
            infoCrl = "no se encuntra la crl";

        for (int j = 0; j < listacrl.size(); j++)
        {
            byte[] b_crl = downloadCRL(listacrl.get(j)); // leo lo descargado, puede ser cualquier cosa
            X509CRLParser x509Parser = new X509CRLParser();
            x509Parser.engineInit(new ByteArrayInputStream(b_crl));
            X509CRL myCrl = (X509CRL) x509Parser.engineRead(); // va a tirar una excepción si el formato no es el correcto
            Date fechahoy = new Date();
            if (!(myCrl.getNextUpdate().after(fechahoy) && myCrl.getThisUpdate().before(fechahoy)))
            {
                infoCrl = "crl no se encuentra en el rango valido de fechas";
                break;
            }
            if (myCrl.isRevoked(cert))
            {
                infoCrl = "El certificado esta revocado";
                break;
            }
        }

        return infoCrl;
    }

    public String getCN(X509Certificate certificado)
    {
        String dn = certificado.getSubjectDN().toString();
        String cn0 = "";
        try
        {
            LdapName ln = new LdapName(dn);
            for (Rdn rdn : ln.getRdns())
            {
                if (rdn.getType().equalsIgnoreCase("CN"))
                {
                    cn0 = rdn.getValue().toString();
                    break;
                }
            }
        } catch (Exception e)
        {
        }
        return cn0;
    }

    public String getEmpresa(X509Certificate certificado)
    {
        String dn = certificado.getSubjectDN().toString();
        String empresa = "";
        try
        {
            LdapName ln = new LdapName(dn);
            for (Rdn rdn : ln.getRdns())
            {
                if (rdn.getType().equalsIgnoreCase("O"))
                {
                    empresa = rdn.getValue().toString();
                    break;
                }
            }
        } catch (Exception e)
        {
        }
        return empresa;
    }
}

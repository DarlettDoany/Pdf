package org.saeta.controller;
import com.aspose.words.Document;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.security.CertificateVerification;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import com.itextpdf.text.pdf.security.VerificationException;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.DigestInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.tsp.TimeStampToken;
import org.bouncycastle.x509.util.StreamParsingException;

import javax.naming.InvalidNameException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
public class ValidarPdf {
    public ValidarPdf() {}

    public DocumentoPdf validar(File rutadocumento) throws Exception
    {
        Path path = rutadocumento.toPath();
        byte[] bytesdocumento = Files.readAllBytes(path);
        return validar(bytesdocumento, rutadocumento.getName());
    }


    public DocumentoPdf validar(String rutadocumento) throws Exception
    {
        Path path = Paths.get(rutadocumento, new String[0]);
        byte[] bytesdocumento = Files.readAllBytes(path);
        return validar(bytesdocumento, path.toFile().getName());
    }



    public DocumentoPdf validar(byte[] bytesdocumento, String nombredocumento) throws Exception
    {
        Security.addProvider((Provider)new BouncyCastleProvider());
        Security.insertProviderAt((Provider)new BouncyCastleProvider(), 1);
        List<Firmante> listaFirmante = new ArrayList<>();
        DocumentoPdf documentoPdf = null;
        CertificateIntoFunctions certInfo = new CertificateIntoFunctions();

        byte[] digest;
        documentoPdf = new DocumentoPdf();
        documentoPdf.setMensaje("");
        String contenido = "";
        int totsellosregistrados = 0;
        boolean estadodocumento = false;
        boolean estadorevocacion = false;
        boolean estadodecertificados = false;
        String algoritmoresumen = null;
        String hashalgoritmo = null;

        if (bytesdocumento == null) {
            contenido = "El documento no existe";
            return null;
        }

        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        File documento = null;
        PdfReader reader = new PdfReader(bytesdocumento);
        AcroFields af = reader.getAcroFields();
        ArrayList<String> names = af.getSignatureNames();
        int totfirmasregistradas = names.size();
        contenido = contenido + "Detalles del documento firmado\n";
        contenido = contenido + "--------------------------------------------\n";
        documentoPdf.setNombre(nombredocumento);

        contenido = contenido + "Nombre: " + nombredocumento + "\n";

        if (reader.getCertificationLevel() == 1)
        {
            documentoPdf.setEstado("Cerrado");
            contenido = contenido + "Estado: Cerrado\n";
        }
        else
        {
            documentoPdf.setEstado("Abierto");
            contenido = contenido + "Estado: Abierto\n";
        }

        for (int k = 0; k < names.size(); k++)
        {
            Firmante firmante = new Firmante();
            String name = names.get(k);
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] bb = new byte[8192];
            InputStream ip = af.extractRevision(name);
            int n = 0;

            while ((n = ip.read(bb)) > 0)
                bout.write(bb, 0, n);
            ip.close();

            PdfPKCS7 pk = af.verifySignature(name);
            firmante.setNombrefirma(name);
            contenido = contenido + "Nombre de la firma : " + name + "\n";
            X509Certificate x509Certificate = pk.getSigningCertificate();
            firmante.setFirmante(certInfo.getCN(x509Certificate));
            contenido = contenido + "Firmante : " + firmante.getFirmante() + "\n";
            firmante.setCargo(certInfo.getTitulo(x509Certificate));
            contenido = contenido + "Cargo : " + firmante.getCargo() + "\n";
            firmante.setEmpresa(certInfo.getEmpresa(x509Certificate));
            contenido = contenido + "Empresa : " + firmante.getEmpresa() + "\n";
            firmante.setMotivo(pk.getReason());
            contenido = contenido + "Motivo : " + firmante.getMotivo() + "\n";
            firmante.setLocacion(pk.getLocation());
            contenido = contenido + "Locación : " + firmante.getLocacion() + "\n";
            firmante.setFechaordenador(pk.getSignDate().getTime().toString());
            contenido = contenido + "Fecha desde ordenador : " + pk.getSignDate().getTime() + "\n";
            Date fechainicial = pk.getSignDate().getTime();


            Calendar cal = pk.getSignDate();
            Certificate[] pkc = pk.getCertificates();

            List<VerificationException> fails = CertificateVerification.verifyCertificates(pkc, ks, null, pk.getSignDate());
            estadodecertificados = (fails == null);
            estadodocumento = pk.verify();
            estadorevocacion = pk.isRevocationValid();
            algoritmoresumen = pk.getDigestAlgorithm();
            hashalgoritmo = pk.getHashAlgorithm();
            contenido = contenido + "--------------------------------------------------------------\n";

            Certificate[] theCerts = pk.getSignCertificateChain();

            for (Certificate c : theCerts)
            {
                if (pk.getSignName() != null)
                    ks.setCertificateEntry(pk.getSignName(), c);
            }

       /* List<VerificationException> errors = CertificateVerification.verifyCertificates(theCerts, ks, null, pk.getSignDate());

        if (!errors.isEmpty())
        {
            for (VerificationException ver : errors)
                verificationErrors += ver.toString() + "\n";

            isValid = false;
        }*/

            Certificado[] certificados = new Certificado[theCerts.length];
            String errorMessages = "";

            for(int i = 0; i < theCerts.length; i++)
            {
                try
                {
                    certificados[i] = certInfo.validateCerts((X509Certificate)theCerts[i], "", false, i);
                }
                catch (CertificateParsingException | StreamParsingException | InvalidNameException ex)
                {
                    errorMessages += " " + ex.getMessage();
                }

                certificados[i].errores = errorMessages;
            }

            firmante.setCertificado(certificados);

            TimeStampToken ts = pk.getTimeStampToken();

            if (ts != null)
            {
                SellodeTiempo sello = new SellodeTiempo();
                firmante.setFechasellotiempo(ts.getTimeStampInfo().getGenTime().toString());
                sello.setHashAlgorithm(ts.getTimeStampInfo().getHashAlgorithm().getAlgorithm().toString());

                if (ts.getTimeStampInfo().getTsa() != null)
                    sello.setTsa(ts.getTimeStampInfo().getTsa().getName().toString());

                sello.setSerialNumber(ts.getTimeStampInfo().getSerialNumber());
                sello.setHexSerialNumber(ts.getTimeStampInfo().getSerialNumber().toString(16));


                firmante.setConSellodeTiempo(true);
                firmante.setSellodeTiempo(sello);
            }
            else
            {

                firmante.setSellodeTiempo(null);
                firmante.setFechasellotiempo(null);
                firmante.setConSellodeTiempo(false);
            }

            listaFirmante.add(firmante);
        }

        documentoPdf.setNumerototalfirmas("" + totfirmasregistradas);
        contenido = contenido + "Número de Firmas registradas : " + totfirmasregistradas + "\n";
        documentoPdf.setNumerototalsello("" + totsellosregistrados);
        contenido = contenido + "Números de sellos de tiempos registrados: " + totsellosregistrados + "\n";
        if (!estadodocumento) {
            contenido = contenido + "El documento fue alterado: SI\n";
            documentoPdf.setDocumentoalterado("SI");
            documentoPdf.setMensaje((documentoPdf.getMensaje().length() == 0) ? "El documento est&aacute; alterado." : "\nEl documento est&aacute; alterado.");
        } else {
            contenido = contenido + "El documento fue alterado: NO\n";
            documentoPdf.setDocumentoalterado("NO");
        }
        if (estadorevocacion) {
            documentoPdf.setDocumentorevocado("SI");
            contenido = contenido + "El certificado está revocado: SI\n";
            documentoPdf.setMensaje((documentoPdf.getMensaje().length() == 0) ? "El certificado est&aacute; revocado." : "\nEl certificado est&aacute; alterado.");
        } else {
            documentoPdf.setDocumentorevocado("NO");
            contenido = contenido + "El certificado está revocado: NO\n";
        }
        if (estadodecertificados) {
            documentoPdf.setEstadocertificado("no vigente");
            contenido = contenido + "Estado del certificado: no vigente\n";
            documentoPdf.setMensaje((documentoPdf.getMensaje().length() == 0) ? "El certificado no est&aacute; vigente." : "\nEl certificado no est&aacute; vigente.");
        } else {
            documentoPdf.setEstadocertificado("vigente");
            contenido = contenido + "Estado del certificado: vigente\n";
        }
        documentoPdf.setAlgorithmoresumen(algoritmoresumen);
        contenido = contenido + "Algoritmo de resumen: " + algoritmoresumen + "\n";
        documentoPdf.setAlgorithmohash(hashalgoritmo);
        contenido = contenido + "Algorimto hash: " + hashalgoritmo + "\n";

        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");

        if (documento != null) {
            Path path = Paths.get(documento.toURI());
            digest = sha1.digest(Files.readAllBytes(path));
        } else {
            digest = sha1.digest(bytesdocumento);
        }
        DERObjectIdentifier sha1oid_ = new DERObjectIdentifier("1.3.14.3.2.26");
        AlgorithmIdentifier sha1aid_ = new AlgorithmIdentifier(sha1oid_, null);
        String resumendocumento = bytes2String(digest);
        documentoPdf.setResumendocumento(resumendocumento);
        contenido = contenido + "Resumen documento:\t" + resumendocumento + "\n";
        DigestInfo di = new DigestInfo(sha1aid_, digest);
        String resumenfirma = bytes2String(di.getDigest());
        documentoPdf.setResumenfirma(resumenfirma);
        contenido = contenido + "Resumen firma:\t\t" + resumenfirma + "\n";

        if (resumenfirma.equals(resumendocumento)) {
            documentoPdf.setMensaje((documentoPdf.getMensaje().length() == 0) ? "El contenido del documento coincide con lo reflejado en la firma." : documentoPdf.getMensaje());
            contenido = contenido + "El contenido del documento coincide con lo reflejado en la firma\n";
        } else {
            documentoPdf.setMensaje("El contenido del documento no coincide con lo reflejado en la firma." + documentoPdf.getMensaje());
            contenido = contenido + "El contenido del documento no coincide con lo reflejado en la firma\n";
        }
        documentoPdf.setFirmante(listaFirmante);
        documentoPdf.setContenido(contenido);

        return documentoPdf;
    }

    String bytes2String(byte[] bytes) {
        StringBuilder string = new StringBuilder();
        for (byte b : bytes) {
            String hexString = Integer.toHexString(0xFF & b);
            string.append((hexString.length() == 1) ? ("0" + hexString) : hexString);
        }
        return string.toString();
    }
}

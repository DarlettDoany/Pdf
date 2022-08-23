package org.saeta.controller;


import org.bouncycastle.asn1.x509.X509Name;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;

public class Certificado {
    X509Certificate cert;
    private String serialNumber;
    private Date notBefore;
    private Date notAfter;
    private String signature;
    private String cn;
    private String titulo;
    private String Empresa;
    private String Area;
    private String infoCrl;
    private String infoCert;
    private String estadoCert;

    private Date TimeStampDate;
    private boolean esDelSellodeTIempo = false;

    public HashMap<String, String> DatosCertificadoIssuer = new HashMap<>();
    public HashMap<String, String> DatosCertificadoSubject = new HashMap<>();
    private LinkedList<String> usoscertificdo = new LinkedList<>();
    public String errores = "";

    EnumSet<CertificateIntoFunctions.valoresVerificacion> v_result = EnumSet.of(CertificateIntoFunctions.valoresVerificacion.None);

    public LinkedList<String> getUsoscertificdo()
    {
        return usoscertificdo;
    }

    public void setUsoscertificdo(LinkedList<String> usoscertificdo)
    {
        this.usoscertificdo = usoscertificdo;
    }

    public void addCertValuesIssuer(X509Name xDN) throws IOException, InvalidNameException
    {
        String dn = xDN.toString();
        LdapName ln = new LdapName(dn);

        for(Rdn rdn : ln.getRdns())
        {
            if(CertificateIntoFunctions.DistinguishedNames.containsKey(rdn.getType()))
                DatosCertificadoIssuer.put(CertificateIntoFunctions.DistinguishedNames.get(rdn.getType()), rdn.getValue().toString());
        }
    }

    public void addCertValuesSubject(X509Name xDN) throws IOException, InvalidNameException
    {
        String dn = xDN.toString();
        LdapName ln = new LdapName(dn);

        for(Rdn rdn : ln.getRdns())
        {
            if(CertificateIntoFunctions.DistinguishedNames.containsKey(rdn.getType()))
                DatosCertificadoSubject.put(CertificateIntoFunctions.DistinguishedNames.get(rdn.getType()), rdn.getValue().toString());
        }
    }

    public void addValidityDates(Date NotBefore, Date NotAfter)
    {
        DatosCertificadoSubject.put(CertificateIntoFunctions.DistinguishedNames.get("NotAfter"), NotAfter.toString());
        DatosCertificadoSubject.put(CertificateIntoFunctions.DistinguishedNames.get("NotBefore"), NotBefore.toString());
    }

    public Date getTimeStampDate()
    {
        return TimeStampDate;
    }

    public void setTimeStampDate(Date TimeStampDate)
    {
        this.TimeStampDate = TimeStampDate;
    }

    public boolean isEsDelSellodeTIempo()
    {
        return esDelSellodeTIempo;
    }

    public void setEsDelSellodeTIempo(boolean esDelSellodeTIempo)
    {
        this.esDelSellodeTIempo = esDelSellodeTIempo;
    }

    public String getEstadoCert()
    {
        return estadoCert;
    }

    public void setEstadoCert(String estadoCert)
    {
        this.estadoCert = estadoCert;
    }

    public String getInfoCert()
    {
        return infoCert;
    }

    public void setInfoCert(String infoCert)
    {
        this.infoCert = infoCert;
    }

    public void setCn(String cn)
    {
        this.cn = cn;
    }

    public void setTitulo(String titulo)
    {
        this.titulo = titulo;
    }

    public void setEmpresa(String Empresa)
    {
        this.Empresa = Empresa;
    }

    public void setArea(String Area)
    {
        this.Area = Area;
    }

    public void setInfoCrl(String infoCrl)
    {
        this.infoCrl = infoCrl;
    }



    public String getInfoCrl()
    {
        return infoCrl;
    }


    public X509Certificate getCert()
    {
        return cert;
    }

    public void setCert(X509Certificate cert)
    {
        this.cert = cert;
    }

    public String getCn()
    {
        return cn;
    }

    public String getTitulo()
    {
        return titulo;
    }

    public String getEmpresa()
    {
        return Empresa;
    }

    public String getArea()
    {
        return Area;
    }

    public String getSerialNumber() { return serialNumber; }

    public void setSerialNumber(String value) { this.serialNumber = value; }


    public Date getNotBefore() { return notBefore; }

    public void setNotBefore(Date value) { this.notBefore = value; }


    public Date getNotAfter() { return notAfter; }

    public void setNotAfter(Date value) { this.notAfter = value; }

    public String getSignature() { return signature; }

    public void setSignature(String value) { this.signature = value; }

}

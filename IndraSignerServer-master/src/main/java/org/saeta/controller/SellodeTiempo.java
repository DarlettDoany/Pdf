package org.saeta.controller;
import java.math.BigInteger;
import java.util.Date;
public class SellodeTiempo {
    Date FechaSello;
    String validez;
    String hashAlgorithm;
    String tsa;

    BigInteger serialNumber;
    String hexSerialNumber;

    public BigInteger getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(BigInteger serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    public String getHexSerialNumber()
    {
        return hexSerialNumber;
    }

    public void setHexSerialNumber(String hexSerialNumber)
    {
        this.hexSerialNumber = hexSerialNumber;
    }

    public String getTsa()
    {
        return tsa;
    }

    public void setTsa(String tsa)
    {
        this.tsa = tsa;
    }

    public Date getFechaSello()
    {
        return FechaSello;
    }

    public void setFechaSello(Date FechaSello)
    {
        this.FechaSello = FechaSello;
    }

    public String getValidez()
    {
        return validez;
    }

    public void setValidez(String validez)
    {
        this.validez = validez;
    }

    public String getHashAlgorithm()
    {
        return hashAlgorithm;
    }

    public void setHashAlgorithm(String hashAlgorithm)
    {
        this.hashAlgorithm = hashAlgorithm;
    }

}

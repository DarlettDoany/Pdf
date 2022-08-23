package org.saeta.controller;

public class Firmante {
    private String nombrefirma;

    private String firmante;

    private String cargo;

    private String empresa;

    private String motivo;

    private String locacion;

    private String fechaordenador;

    private String fechasellotiempo;

    private Boolean conSellodeTiempo;

    private SellodeTiempo sellodeTiempo;

    private Certificado[] certificado;

    public Certificado[] getCertificado()
    {
        return certificado;
    }

    public void setCertificado(Certificado[] certificado)
    {
        this.certificado = certificado;
    }

    public SellodeTiempo getSellodeTiempo()
    {
        return sellodeTiempo;
    }

    public void setSellodeTiempo(SellodeTiempo sellodeTiempo){this.sellodeTiempo=sellodeTiempo;}



    public Boolean getConSellodeTiempo()
    {
        return conSellodeTiempo;
    }

    public void setConSellodeTiempo(Boolean conSellodeTiempo)
    {
        this.conSellodeTiempo = conSellodeTiempo;
    }



    public String getNombrefirma() {
        return this.nombrefirma;
    }

    public void setNombrefirma(String nombrefirma) {
        this.nombrefirma = nombrefirma;
    }

    public String getFirmante() {
        return this.firmante;
    }

    public void setFirmante(String firmante) {
        this.firmante = firmante;
    }

    public String getCargo() {
        return this.cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getMotivo() {
        return this.motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getLocacion() {
        return this.locacion;
    }

    public void setLocacion(String locacion) {
        this.locacion = locacion;
    }

    public String getFechaordenador() {
        return this.fechaordenador;
    }

    public void setFechaordenador(String fechaordenador) {
        this.fechaordenador = fechaordenador;
    }

    public String getFechasellotiempo() {
        return this.fechasellotiempo;
    }

    public void setFechasellotiempo(String fechasellotiempo) {
        if (fechasellotiempo == null) {
            this.fechasellotiempo = "-";
        } else {
            this.fechasellotiempo = fechasellotiempo;
        }
    }
}

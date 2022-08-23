package org.saeta.controller;

import java.util.List;
import org.saeta.controller.SellodeTiempo;

public class DocumentoPdf {
    private String contenido;

    private String nombre;

    private String ruta;

    private String estado;

    private String nombrefirma;

    private List<Firmante> firmante;

    private String numerototalfirmas;

    private String numerototalsello;

    private String documentoalterado;

    private String documentorevocado;

    private String estadocertificado;

    private String algorithmoresumen;

    private String algorithmohash;

    private String resumendocumento;

    private String resumenfirma;

    private String mensaje;

    private String infoException;

    private SellodeTiempo sellodeTiempo;


    public String getInfoException()
    {
        return infoException;
    }

    public void setInfoException(String infoException)
    {
        this.infoException = infoException;
    }


    public String getContenido() {
        return this.contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return this.ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombrefirma() {
        return this.nombrefirma;
    }

    public void setNombrefirma(String nombrefirma) {
        this.nombrefirma = nombrefirma;
    }

    public List<Firmante> getFirmante() {
        return this.firmante;
    }

    public void setFirmante(List<Firmante> firmante) {
        this.firmante = firmante;
    }

    public String getNumerototalfirmas() {
        return this.numerototalfirmas;
    }

    public void setNumerototalfirmas(String numerototalfirmas) {
        this.numerototalfirmas = numerototalfirmas;
    }

    public String getNumerototalsello() {
        return this.numerototalsello;
    }

    public void setNumerototalsello(String numerototalsello) {
        this.numerototalsello = numerototalsello;
    }

    public String getDocumentoalterado() {
        return this.documentoalterado;
    }

    public void setDocumentoalterado(String documentoalterado) {
        this.documentoalterado = documentoalterado;
    }

    public String getDocumentorevocado() {
        return this.documentorevocado;
    }

    public void setDocumentorevocado(String documentorevocado) {
        this.documentorevocado = documentorevocado;
    }

    public String getEstadocertificado() {
        return this.estadocertificado;
    }

    public void setEstadocertificado(String estadocertificado) {
        this.estadocertificado = estadocertificado;
    }

    public String getAlgorithmoresumen() {
        return this.algorithmoresumen;
    }

    public void setAlgorithmoresumen(String algorithmoresumen) {
        this.algorithmoresumen = algorithmoresumen;
    }

    public String getAlgorithmohash() {
        return this.algorithmohash;
    }

    public void setAlgorithmohash(String algorithmohash) {
        this.algorithmohash = algorithmohash;
    }

    public String getResumendocumento() {
        return this.resumendocumento;
    }

    public void setResumendocumento(String resumendocumento) {
        this.resumendocumento = resumendocumento;
    }

    public String getResumenfirma() {
        return this.resumenfirma;
    }

    public void setResumenfirma(String resumenfirma) {
        this.resumenfirma = resumenfirma;
    }

    public String getMensaje() {
        return this.mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public org.saeta.controller.SellodeTiempo getSellodeTiempo() {
        return sellodeTiempo;
    }

    public void setSellodeTiempo(org.saeta.controller.SellodeTiempo sellodeTiempo) {
        this.sellodeTiempo = sellodeTiempo;
    }
}

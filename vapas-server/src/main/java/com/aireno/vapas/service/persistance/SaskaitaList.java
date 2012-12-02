package com.aireno.vapas.service.persistance;

import com.aireno.vapas.service.base.EntityBase;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class SaskaitaList extends EntityBase implements Serializable
{
    private String tiekejas;
    private String imone;
    private SaskaitaStatusas statusas;
    private String numeris;
    private Date data;
    private boolean arSaskaita;
    private BigDecimal sumaSuPvm;

    public String getTiekejas() {
        return tiekejas;
    }

    public void setTiekejas(String tiekejas) {
        this.tiekejas = tiekejas;
    }

    public String getImone() {
        return imone;
    }

    public void setImone(String imone) {
        this.imone = imone;
    }

    public SaskaitaStatusas getStatusas() {
        return statusas;
    }

    public void setStatusas(SaskaitaStatusas statusas) {
        this.statusas = statusas;
    }

    public String getNumeris() {
        return numeris;
    }

    public void setNumeris(String numeris) {
        this.numeris = numeris;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public boolean isArSaskaita() {
        return arSaskaita;
    }

    public void setArSaskaita(boolean arSaskaita) {
        this.arSaskaita = arSaskaita;
    }

    public BigDecimal getSumaSuPvm() {
        return sumaSuPvm;
    }

    public void setSumaSuPvm(BigDecimal sumaSuPvm) {
        this.sumaSuPvm = sumaSuPvm;
    }
}

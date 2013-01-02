package com.aireno.vapas.service.persistance;

import com.aireno.vapas.service.base.EntityBase;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class Saskaita extends EntityBase implements Serializable {
    private Long tiekejasId;
    private long imoneId;
    private Date data;
    private SaskaitaStatusas statusas;
    private boolean arSaskaita;
    private BigDecimal sumaSuPvm = new BigDecimal(0);
    private BigDecimal sumaBePvm = new BigDecimal(0);
    private BigDecimal sumaPvm = new BigDecimal(0);
    private int prekiuKiekis;

    private String numeris;

    public String getNumeris() {
        return numeris;
    }

    public void setNumeris(String numeris) {
        this.numeris = numeris;
    }

    public Long getTiekejasId() {
        return tiekejasId;
    }

    public void setTiekejasId(Long tiekejasId) {
        this.tiekejasId = tiekejasId;
    }

    public long getImoneId() {
        return imoneId;
    }

    public void setImoneId(long imoneId) {
        this.imoneId = imoneId;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public SaskaitaStatusas getStatusas() {
        return statusas;
    }

    public void setStatusas(SaskaitaStatusas statusas) {
        this.statusas = statusas;
    }

    public boolean isArSaskaita() {
        return arSaskaita;
    }

    public void setArSaskaita(boolean arSaskaita) {
        this.arSaskaita = arSaskaita;
    }

    public int getPrekiuKiekis() {
        return prekiuKiekis;
    }

    public void setPrekiuKiekis(int prekiuKiekis) {
        this.prekiuKiekis = prekiuKiekis;
    }

    public BigDecimal getSumaSuPvm() {
        return sumaSuPvm;
    }

    public void setSumaSuPvm(BigDecimal sumaSuPvm) {
        this.sumaSuPvm = sumaSuPvm;
    }

    public BigDecimal getSumaBePvm() {
        return sumaBePvm;
    }

    public void setSumaBePvm(BigDecimal sumaBePvm) {
        this.sumaBePvm = sumaBePvm;
    }

    public BigDecimal getSumaPvm() {
        return sumaPvm;
    }

    public void setSumaPvm(BigDecimal sumaPvm) {
        this.sumaPvm = sumaPvm;
    }
}

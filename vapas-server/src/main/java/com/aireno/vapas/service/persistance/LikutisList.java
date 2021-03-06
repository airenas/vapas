package com.aireno.vapas.service.persistance;

import com.aireno.vapas.service.base.EntityBase;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class LikutisList extends EntityBase implements Serializable {
    protected String matavimoVienetas;
    protected String imone;
    private BigDecimal kiekis;
    private BigDecimal pajamuota;
    protected String preke;
    private BigDecimal nurasyta;
    private String serija;
    private Date data;

    public String getMatavimoVienetas() {
        return matavimoVienetas;
    }

    public void setMatavimoVienetas(String matavimoVienetas) {
        this.matavimoVienetas = matavimoVienetas;
    }

    public String getImone() {
        return imone;
    }

    public void setImone(String imone) {
        this.imone = imone;
    }

    public BigDecimal getKiekis() {
        return kiekis;
    }

    public void setKiekis(BigDecimal kiekis) {
        this.kiekis = kiekis;
    }

    public BigDecimal getPajamuota() {
        return pajamuota;
    }

    public void setPajamuota(BigDecimal pajamuota) {
        this.pajamuota = pajamuota;
    }

    public String getPreke() {
        return preke;
    }

    public void setPreke(String preke) {
        this.preke = preke;
    }

    public BigDecimal getNurasyta() {
        return nurasyta;
    }

    public void setNurasyta(BigDecimal nurasyta) {
        this.nurasyta = nurasyta;
    }

    public String getSerija() {
        return serija;
    }

    public void setSerija(String serija) {
        this.serija = serija;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}

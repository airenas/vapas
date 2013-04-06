package com.aireno.dto;

import com.aireno.base.DtoBase;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 16.25
 * To change this template use File | Settings | File Templates.
 */
public class LikutisListDto extends DtoBase {
    protected String matavimoVienetas;
    protected String imone;
    private BigDecimal kiekis;
    private BigDecimal pajamuota;
    protected String preke;
    private BigDecimal nurasyta;
    private String serija;
    private String data;

    public String getImone() {
        return imone;
    }

    public void setImone(String imone) {
        this.imone = imone;
    }

    public String getMatavimoVienetas() {
        return matavimoVienetas;
    }

    public void setMatavimoVienetas(String matavimoVienetas) {
        this.matavimoVienetas = matavimoVienetas;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

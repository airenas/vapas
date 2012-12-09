package com.aireno.vapas.service.persistance;

import com.aireno.vapas.service.base.EntityBase;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class Likutis extends EntityBase implements Serializable {

    private long matavimoVienetasId;
    private long prekeId;
    private String serija;
    private BigDecimal kiekis;
    private Date galiojaIki;
    private Date data;
    private String dokumentas;
    private long irasoId;
    private boolean arSaskaita;

    public boolean isArSaskaita() {
        return arSaskaita;
    }

    public void setArSaskaita(boolean arSaskaita) {
        this.arSaskaita = arSaskaita;
    }

    public long getMatavimoVienetasId() {
        return matavimoVienetasId;
    }

    public void setMatavimoVienetasId(long matavimoVienetasId) {
        this.matavimoVienetasId = matavimoVienetasId;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDokumentas() {
        return dokumentas;
    }

    public void setDokumentas(String dokumentas) {
        this.dokumentas = dokumentas;
    }

    public long getIrasoId() {
        return irasoId;
    }

    public void setIrasoId(long irasoId) {
        this.irasoId = irasoId;
    }

    public long getPrekeId() {
        return prekeId;
    }

    public void setPrekeId(long prekeId) {
        this.prekeId = prekeId;
    }

    public String getSerija() {
        return serija;
    }

    public void setSerija(String serija) {
        this.serija = serija;
    }

    public BigDecimal getKiekis() {
        return kiekis;
    }

    public void setKiekis(BigDecimal kiekis) {
        this.kiekis = kiekis;
    }

    public Date getGaliojaIki() {
        return galiojaIki;
    }

    public void setGaliojaIki(Date galiojaIki) {
        this.galiojaIki = galiojaIki;
    }
}

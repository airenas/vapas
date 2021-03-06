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
    private long imoneId;
    private boolean arSaskaita;
    private Long zurnaloId;
    private Long zurnaloVaistoId;
    private Long saskaitosId;
    private Long saskaitosPrekesId;
    private Long pirminisId;

    private Long nurasymoId;
    private Long nurasymoPrekesId;

    public Long getNurasymoId() {
        return nurasymoId;
    }

    public void setNurasymoId(Long nurasymoId) {
        this.nurasymoId = nurasymoId;
    }

    public Long getNurasymoPrekesId() {
        return nurasymoPrekesId;
    }

    public void setNurasymoPrekesId(Long nurasymoPrekesId) {
        this.nurasymoPrekesId = nurasymoPrekesId;
    }

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

    public long getImoneId() {
        return imoneId;
    }

    public void setImoneId(long imoneId) {
        this.imoneId = imoneId;
    }

    public Long getZurnaloId() {
        return zurnaloId;
    }

    public void setZurnaloId(Long zurnaloId) {
        this.zurnaloId = zurnaloId;
    }

    public Long getZurnaloVaistoId() {
        return zurnaloVaistoId;
    }

    public void setZurnaloVaistoId(Long zurnaloVaistoId) {
        this.zurnaloVaistoId = zurnaloVaistoId;
    }

    public Long getSaskaitosId() {
        return saskaitosId;
    }

    public void setSaskaitosId(Long saskaitosId) {
        this.saskaitosId = saskaitosId;
    }

    public Long getSaskaitosPrekesId() {
        return saskaitosPrekesId;
    }

    public void setSaskaitosPrekesId(Long saskaitosPrekesId) {
        this.saskaitosPrekesId = saskaitosPrekesId;
    }

    public Long getPirminisId() {
        return pirminisId;
    }

    public void setPirminisId(Long pirminisId) {
        this.pirminisId = pirminisId;
    }
}

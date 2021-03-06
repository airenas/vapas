package com.aireno.vapas.service.persistance;

import com.aireno.vapas.service.base.EntityBase;

import java.io.Serializable;
import java.lang.String;
import java.util.Date;


public class GydomuGyvunuZurnalas extends EntityBase implements Serializable {
    private long eilesNumeris;
    private long imoneId;
    private String laikytojas;
    private Date registracijosData;
    private Date pabaigosData;
    private Date pirmuPozymiuData;
    private String diagnoze;
    private String gyvunuSarasas;
    private String gydymas;
    private boolean arNurasymas;

    private Date islaukaPienui;
    private Date islaukaMesai;

    public Date getPabaigosData() {
        return pabaigosData;
    }

    public boolean isArNurasymas() {
        return arNurasymas;
    }

    public void setArNurasymas(boolean arNurasymas) {
        this.arNurasymas = arNurasymas;
    }

    public void setPabaigosData(Date pabaigosData) {
        this.pabaigosData = pabaigosData;
    }

    public Date getIslaukaPienui() {
        return islaukaPienui;
    }

    public void setIslaukaPienui(Date islaukaPienui) {
        this.islaukaPienui = islaukaPienui;
    }

    public Date getIslaukaMesai() {
        return islaukaMesai;
    }

    public void setIslaukaMesai(Date islaukaMesai) {
        this.islaukaMesai = islaukaMesai;
    }

    public long getEilesNumeris() {
        return eilesNumeris;
    }

    public void setEilesNumeris(long eilesNumeris) {
        this.eilesNumeris = eilesNumeris;
    }

    public long getImoneId() {
        return imoneId;
    }

    public void setImoneId(long imoneId) {
        this.imoneId = imoneId;
    }

    public Date getRegistracijosData() {
        return registracijosData;
    }

    public void setRegistracijosData(Date registracijosData) {
        this.registracijosData = registracijosData;
    }

    public Date getPirmuPozymiuData() {
        return pirmuPozymiuData;
    }

    public void setPirmuPozymiuData(Date pirmuPozymiuData) {
        this.pirmuPozymiuData = pirmuPozymiuData;
    }

    public String getDiagnoze() {
        return diagnoze;
    }

    public void setDiagnoze(String diagnoze) {
        this.diagnoze = diagnoze;
    }

    public String getGyvunuSarasas() {
        return gyvunuSarasas;
    }

    public void setGyvunuSarasas(String gyvunuSarasas) {
        this.gyvunuSarasas = gyvunuSarasas;
    }

    public String getGydymas() {
        return gydymas;
    }

    public void setGydymas(String gydymas) {
        this.gydymas = gydymas;
    }

    public String getLaikytojas() {
        return laikytojas;
    }

    public void setLaikytojas(String laikytojas) {
        this.laikytojas = laikytojas;
    }
}

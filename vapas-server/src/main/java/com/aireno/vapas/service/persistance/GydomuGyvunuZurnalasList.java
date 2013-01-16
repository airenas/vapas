package com.aireno.vapas.service.persistance;

import com.aireno.vapas.service.base.EntityBase;

import java.io.Serializable;
import java.lang.String;
import java.util.Date;


public class GydomuGyvunuZurnalasList extends EntityBase implements Serializable {
    private long eilesNumeris;
    private String imone;
    private String laikytojas;
    private Date registracijosData;
    private String gyvunuSarasas;
    private String gydymas;

    private Date islaukaPienui;
    private Date islaukaMesai;


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

    public String getImone() {
        return imone;
    }

    public void setImone(String imone) {
        this.imone = imone;
    }

    public String getLaikytojas() {
        return laikytojas;
    }

    public void setLaikytojas(String laikytojas) {
        this.laikytojas = laikytojas;
    }

    public Date getRegistracijosData() {
        return registracijosData;
    }

    public void setRegistracijosData(Date registracijosData) {
        this.registracijosData = registracijosData;
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
}

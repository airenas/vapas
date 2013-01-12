package com.aireno.vapas.service.persistance;

import com.aireno.vapas.service.base.EntityBase;

import java.io.Serializable;


public class vGyvunas extends EntityBase implements Serializable
{
    private String numeris;
    private String laikytojas;
    private long gyvunoRusisId;

    public long getGyvunoRusisId() {
        return gyvunoRusisId;
    }

    public void setGyvunoRusisId(long gyvunoRusisId) {
        this.gyvunoRusisId = gyvunoRusisId;
    }

    public String getNumeris() {
        return numeris;
    }

    public void setNumeris(String numeris) {
        this.numeris = numeris;
    }

    public String getLaikytojas() {
        return laikytojas;
    }

    public void setLaikytojas(String laikytojas) {
        this.laikytojas = laikytojas;
    }
}

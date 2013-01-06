package com.aireno.vapas.service.persistance;

import com.aireno.vapas.service.base.EntityBase;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;


public class ZurnaloGyvunas extends EntityBase implements Serializable
{
    private long zurnaloId;
    private long gyvunoRusisId;
    private String numeris;
    private String amzius;

    public long getZurnaloId() {
        return zurnaloId;
    }

    public void setZurnaloId(long zurnaloId) {
        this.zurnaloId = zurnaloId;
    }

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

    public String getAmzius() {
        return amzius;
    }

    public void setAmzius(String amzius) {
        this.amzius = amzius;
    }
}

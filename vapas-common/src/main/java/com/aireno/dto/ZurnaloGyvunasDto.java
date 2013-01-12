package com.aireno.dto;

import com.aireno.base.DtoBase;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 13.1.7
 * Time: 23.28
 * To change this template use File | Settings | File Templates.
 */
public class ZurnaloGyvunasDto  extends DtoBase {
    private long gyvunoRusisId;
    private String numeris;
    private String amzius;

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

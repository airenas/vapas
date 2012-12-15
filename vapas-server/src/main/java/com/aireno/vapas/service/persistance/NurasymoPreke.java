package com.aireno.vapas.service.persistance;

import com.aireno.vapas.service.base.EntityBase;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class NurasymoPreke extends EntityBase implements Serializable
{
    private String serija;
    private long prekeId;
    private long nurasymasId;
    private BigDecimal kiekis;
    private long matavimoVienetasId;
    private String matavimoVienetas;

    public String getSerija() {
        return serija;
    }

    public void setSerija(String serija) {
        this.serija = serija;
    }

    public long getPrekeId() {
        return prekeId;
    }

    public void setPrekeId(long prekeId) {
        this.prekeId = prekeId;
    }

    public long getNurasymasId() {
        return nurasymasId;
    }

    public void setNurasymasId(long nurasymasId) {
        this.nurasymasId = nurasymasId;
    }

    public BigDecimal getKiekis() {
        return kiekis;
    }

    public void setKiekis(BigDecimal kiekis) {
        this.kiekis = kiekis;
    }

    public long getMatavimoVienetasId() {
        return matavimoVienetasId;
    }

    public void setMatavimoVienetasId(long matavimoVienetasId) {
        this.matavimoVienetasId = matavimoVienetasId;
    }

    public String getMatavimoVienetas() {
        return matavimoVienetas;
    }

    public void setMatavimoVienetas(String matavimoVienetas) {
        this.matavimoVienetas = matavimoVienetas;
    }
}

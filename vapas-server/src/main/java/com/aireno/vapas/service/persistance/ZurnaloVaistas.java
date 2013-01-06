package com.aireno.vapas.service.persistance;

import com.aireno.vapas.service.base.EntityBase;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class ZurnaloVaistas extends EntityBase implements Serializable {
    private long zurnaloId;
    private long prekeId;
    private BigDecimal kiekis;
    private long matavimoVienetasId;
    private String receptas;

    public long getZurnaloId() {
        return zurnaloId;
    }

    public void setZurnaloId(long zurnaloId) {
        this.zurnaloId = zurnaloId;
    }

    public long getPrekeId() {
        return prekeId;
    }

    public void setPrekeId(long prekeId) {
        this.prekeId = prekeId;
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

    public String getReceptas() {
        return receptas;
    }

    public void setReceptas(String receptas) {
        this.receptas = receptas;
    }
}

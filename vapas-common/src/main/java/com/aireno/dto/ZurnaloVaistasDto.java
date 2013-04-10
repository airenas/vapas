package com.aireno.dto;

import com.aireno.base.DtoBase;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 13.1.7
 * Time: 23.28
 * To change this template use File | Settings | File Templates.
 */
public class ZurnaloVaistasDto extends DtoBase {

    private long prekeId;
    private BigDecimal kiekis;
    private long matavimoVienetasId;
    private String receptas;
    private String priezastis;

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

    public String getPriezastis() {
        return priezastis;
    }

    public void setPriezastis(String priezastis) {
        this.priezastis = priezastis;
    }
}

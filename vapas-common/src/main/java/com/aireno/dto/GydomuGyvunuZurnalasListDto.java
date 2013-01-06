package com.aireno.dto;

import com.aireno.base.DtoBase;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 16.25
 * To change this template use File | Settings | File Templates.
 */
public class GydomuGyvunuZurnalasListDto extends DtoBase {
    private long eilesNumeris;
    private String imone;
    private String laikytojas;
    private Date registracijosData;
    private String gyvunuSarasas;
    private String vaistai;

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

    public String getVaistai() {
        return vaistai;
    }

    public void setVaistai(String vaistai) {
        this.vaistai = vaistai;
    }
}

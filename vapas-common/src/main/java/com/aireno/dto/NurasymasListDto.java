package com.aireno.dto;

import com.aireno.base.DtoBase;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 16.25
 * To change this template use File | Settings | File Templates.
 */
public class NurasymasListDto extends DtoBase {
    protected String numeris;
    protected Date data;
    protected String tiekejas;
    protected String imone;
    protected BigDecimal suma;
    protected String statusas;

    public String getNumeris() {
        return numeris;
    }

    public void setNumeris(String numeris) {
        this.numeris = numeris;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getTiekejas() {
        return tiekejas;
    }

    public void setTiekejas(String tiekejas) {
        this.tiekejas = tiekejas;
    }

    public String getImone() {
        return imone;
    }

    public void setImone(String imone) {
        this.imone = imone;
    }

    public BigDecimal getSuma() {
        return suma;
    }

    public void setSuma(BigDecimal suma) {
        this.suma = suma;
    }

    public String getStatusas() {
        return statusas;
    }

    public void setStatusas(String statusas) {
        this.statusas = statusas;
    }
}

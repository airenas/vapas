package com.aireno.vapas.service.persistance;

import com.aireno.vapas.service.base.EntityBase;

import java.io.Serializable;
import java.util.Date;


public class NurasymasList extends EntityBase implements Serializable {
    private String imone;
    private SaskaitaStatusas statusas;
    private String numeris;
    private Date data;
    private SaskaitaStatusas statusasNotNull;

    public String getImone() {
        return imone;
    }

    public void setImone(String imone) {
        this.imone = imone;
    }

    public SaskaitaStatusas getStatusas() {
        return statusas;
    }

    public void setStatusas(SaskaitaStatusas statusas) {
        this.statusas = statusas;
    }

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

    public SaskaitaStatusas getStatusasNotNull() {
        if (statusas == null) {
            return SaskaitaStatusas.Pildoma;
        }
        return statusas;
    }
}

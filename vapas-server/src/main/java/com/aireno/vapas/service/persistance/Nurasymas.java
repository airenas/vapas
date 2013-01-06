package com.aireno.vapas.service.persistance;

import com.aireno.vapas.service.base.EntityBase;

import java.io.Serializable;
import java.util.Date;


public class Nurasymas extends EntityBase implements Serializable {
    private String numeris;
    private long imoneId;
    private Date data;
    private SaskaitaStatusas statusas;
    private int prekiuKiekis;

    public String getNumeris() {
        return numeris;
    }

    public void setNumeris(String numeris) {
        this.numeris = numeris;
    }

    public long getImoneId() {
        return imoneId;
    }

    public void setImoneId(long imoneId) {
        this.imoneId = imoneId;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public SaskaitaStatusas getStatusas() {
        return statusas;
    }

    public SaskaitaStatusas getStatusasNotNull() {
        if (statusas == null) {
            return SaskaitaStatusas.Pildoma;
        }
        return statusas;
    }

    public void setStatusas(SaskaitaStatusas statusas) {
        this.statusas = statusas;
    }

    public int getPrekiuKiekis() {
        return prekiuKiekis;
    }

    public void setPrekiuKiekis(int prekiuKiekis) {
        this.prekiuKiekis = prekiuKiekis;
    }
}

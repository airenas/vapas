package com.aireno.vapas.service.persistance;

import com.aireno.vapas.service.base.EntityBase;

import java.io.Serializable;


public class Preke extends EntityBase implements Serializable, LookupEntityDto
{
    public long getMatVienetasId() {
        return matVienetasId;
    }

    public void setMatVienetasId(long matVienetasId) {
        this.matVienetasId = matVienetasId;
    }

    private long matVienetasId;
    private String pavadinimas;

    public String getPastaba() {
        return pastaba;
    }

    public void setPastaba(String pastaba) {
        this.pastaba = pastaba;
    }

    private String pastaba;

    public Preke()
    {
    }

    public String getPavadinimas() {
        return pavadinimas;
    }

    public void setPavadinimas(String pavadinimas) {
        this.pavadinimas = pavadinimas;
    }

}

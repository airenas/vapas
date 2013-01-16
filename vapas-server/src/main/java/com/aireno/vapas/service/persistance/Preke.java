package com.aireno.vapas.service.persistance;

import com.aireno.vapas.service.base.EntityBase;

import java.io.Serializable;


public class Preke extends EntityBase implements Serializable, LookupEntityDto
{
    private Long islaukaPienui;
    private Long islaukaMesai;

    public Long getIslaukaPienui() {
        return islaukaPienui;
    }

    public void setIslaukaPienui(Long islaukaPienui) {
        this.islaukaPienui = islaukaPienui;
    }

    public Long getIslaukaMesai() {
        return islaukaMesai;
    }

    public void setIslaukaMesai(Long islaukaMesai) {
        this.islaukaMesai = islaukaMesai;
    }

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

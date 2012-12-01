package com.aireno.vapas.service.persistance;

import com.aireno.vapas.service.base.EntityBase;

import java.io.Serializable;


public class Tiekejas extends EntityBase implements Serializable
{
    private String pavadinimas;

    public String getAdresas() {
        return adresas;
    }

    public void setAdresas(String adresas) {
        this.adresas = adresas;
    }

    public String getTelefonas() {
        return telefonas;
    }

    public void setTelefonas(String telefonas) {
        this.telefonas = telefonas;
    }

    private String adresas;
    private String telefonas;

    public String getPastaba() {
        return pastaba;
    }

    public void setPastaba(String pastaba) {
        this.pastaba = pastaba;
    }

    private String pastaba;

    public Tiekejas()
    {
    }

    public String getPavadinimas() {
        return pavadinimas;
    }

    public void setPavadinimas(String pavadinimas) {
        this.pavadinimas = pavadinimas;
    }
}

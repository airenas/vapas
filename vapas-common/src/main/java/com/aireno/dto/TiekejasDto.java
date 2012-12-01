package com.aireno.dto;

import com.aireno.base.DtoBase;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 16.25
 * To change this template use File | Settings | File Templates.
 */
public class TiekejasDto extends DtoBase {
    public String getPavadinimas() {
        return pavadinimas;
    }

    public void setPavadinimas(String pavadinimas) {
        this.pavadinimas = pavadinimas;
    }

    protected String pavadinimas;

    protected String telefonas;

    public String getTelefonas() {
        return telefonas;
    }

    public void setTelefonas(String telefonas) {
        this.telefonas = telefonas;
    }

    public String getAdresas() {
        return adresas;
    }

    public void setAdresas(String adresas) {
        this.adresas = adresas;
    }

    protected String adresas;

    public String getPastaba() {
        return pastaba;
    }

    public void setPastaba(String pastaba) {
        this.pastaba = pastaba;
    }

    protected String pastaba;
}

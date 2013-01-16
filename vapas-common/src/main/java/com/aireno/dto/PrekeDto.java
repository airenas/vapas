package com.aireno.dto;

import com.aireno.base.DtoBase;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 16.25
 * To change this template use File | Settings | File Templates.
 */
public class PrekeDto extends DtoBase {

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

    public String getPavadinimas() {
        return pavadinimas;
    }

    public void setPavadinimas(String pavadinimas) {
        this.pavadinimas = pavadinimas;
    }


    protected String pavadinimas;

    public String getPastaba() {
        return pastaba;
    }

    public void setPastaba(String pastaba) {
        this.pastaba = pastaba;
    }

    protected String pastaba;

    public long getMatVienetasId() {
        return matVienetasId;
    }

    public void setMatVienetasId(long matVienetasId) {
        this.matVienetasId = matVienetasId;
    }

    long matVienetasId;
}

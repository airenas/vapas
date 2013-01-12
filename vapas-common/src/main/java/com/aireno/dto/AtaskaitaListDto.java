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
public class AtaskaitaListDto extends DtoBase {
    protected String failas;
    protected Date data;
    protected String pavadinimas;

    public String getFailas() {
        return failas;
    }

    public void setFailas(String failas) {
        this.failas = failas;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getPavadinimas() {
        return pavadinimas;
    }

    public void setPavadinimas(String pavadinimas) {
        this.pavadinimas = pavadinimas;
    }
}

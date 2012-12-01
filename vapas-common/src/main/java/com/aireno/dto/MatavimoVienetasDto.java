package com.aireno.dto;

import com.aireno.base.DtoBase;
import com.aireno.base.LookupDto;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 16.25
 * To change this template use File | Settings | File Templates.
 */
public class MatavimoVienetasDto extends DtoBase implements LookupDto {
    protected String kodas;

    public String getKodas() {
        return kodas;
    }

    public void setKodas(String kodas) {
        this.kodas = kodas;
    }

    public String getPavadinimas() {
        return pavadinimas;
    }

    public void setPavadinimas(String pavadinimas) {
        this.pavadinimas = pavadinimas;
    }

    protected String pavadinimas;
}

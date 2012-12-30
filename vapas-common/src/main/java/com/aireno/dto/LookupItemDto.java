package com.aireno.dto;

import com.aireno.base.DtoBase;
import com.aireno.base.LookupDto;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.12.29
 * Time: 11.40
 * To change this template use File | Settings | File Templates.
 */
public class LookupItemDto extends DtoBase implements LookupDto {
    public String getPavadinimas() {
        return pavadinimas;
    }

    public void setPavadinimas(String pavadinimas) {
        this.pavadinimas = pavadinimas;
    }

    protected String pavadinimas;
}

package com.aireno.dto;

import com.aireno.base.DtoBase;
import com.aireno.base.LookupDto;
import com.aireno.base.StringLookupDto;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.12.29
 * Time: 11.40
 * To change this template use File | Settings | File Templates.
 */
public class StringLookupItemDto implements StringLookupDto {

    public StringLookupItemDto(String id) {
        this.id = id;
    }

    public String getPavadinimas() {
        return pavadinimas;
    }

    public void setPavadinimas(String pavadinimas) {
        this.pavadinimas = pavadinimas;
    }

    protected String pavadinimas;
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

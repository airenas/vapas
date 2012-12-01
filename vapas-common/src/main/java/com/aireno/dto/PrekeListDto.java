package com.aireno.dto;

import com.aireno.base.DtoBase;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 16.25
 * To change this template use File | Settings | File Templates.
 */
public class PrekeListDto extends DtoBase {

    public String getPavadinimas() {
        return pavadinimas;
    }

    public void setPavadinimas(String pavadinimas) {
        this.pavadinimas = pavadinimas;
    }

    protected String pavadinimas;

    public String getMatVienetas() {
        return matVienetas;
    }

    public void setMatVienetas(String matVienetas) {
        this.matVienetas = matVienetas;
    }

    protected String matVienetas;
}

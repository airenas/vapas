package com.aireno.vapas.service.persistance;

import com.aireno.vapas.service.base.EntityBase;

import java.io.Serializable;


public class PrekeList extends EntityBase implements Serializable
{
    public String getMatVienetas() {
        return matVienetas;
    }

    public void setMatVienetas(String matVienetas) {
        this.matVienetas = matVienetas;
    }

    private String matVienetas;
    private String pavadinimas;

    public PrekeList()
    {
    }

    public String getPavadinimas() {
        return pavadinimas;
    }

    public void setPavadinimas(String pavadinimas) {
        this.pavadinimas = pavadinimas;
    }
}

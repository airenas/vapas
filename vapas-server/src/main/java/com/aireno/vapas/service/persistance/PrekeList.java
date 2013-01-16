package com.aireno.vapas.service.persistance;

import com.aireno.vapas.service.base.EntityBase;

import java.io.Serializable;


public class PrekeList extends EntityBase implements Serializable {

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

    public String getMatVienetas() {
        return matVienetas;
    }

    public void setMatVienetas(String matVienetas) {
        this.matVienetas = matVienetas;
    }

    private String matVienetas;
    private String pavadinimas;

    public PrekeList() {
    }

    public String getPavadinimas() {
        return pavadinimas;
    }

    public void setPavadinimas(String pavadinimas) {
        this.pavadinimas = pavadinimas;
    }
}

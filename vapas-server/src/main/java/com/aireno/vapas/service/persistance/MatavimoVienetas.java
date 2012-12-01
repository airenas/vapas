package com.aireno.vapas.service.persistance;

import com.aireno.vapas.service.base.EntityBase;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;


public class MatavimoVienetas extends EntityBase implements Serializable
{
    public String getKodas() {
        return kodas;
    }

    public void setKodas(String kodas) {
        this.kodas = kodas;
    }

    private String kodas;
    private String pavadinimas;

    public String getPastaba() {
        return pastaba;
    }

    public void setPastaba(String pastaba) {
        this.pastaba = pastaba;
    }

    private String pastaba;

    public MatavimoVienetas()
    {
    }

    public String getPavadinimas() {
        return pavadinimas;
    }

    public void setPavadinimas(String pavadinimas) {
        this.pavadinimas = pavadinimas;
    }
}

package com.aireno.dto;

import com.aireno.base.DtoBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.17
 * Time: 16.25
 * To change this template use File | Settings | File Templates.
 */
public class NurasymasDto extends DtoBase {
    private long imoneId;
    private Date data;
    private String statusas;
    private int prekiuKiekis;
    private List<NurasymoPrekeDto> prekes;

    public String getNumeris() {
        return numeris;
    }

    public void setNumeris(String numeris) {
        this.numeris = numeris;
    }

    private String numeris;

    public long getImoneId() {
        return imoneId;
    }

    public void setImoneId(long imoneId) {
        this.imoneId = imoneId;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getStatusas() {
        return statusas;
    }

    public void setStatusas(String statusas) {
        this.statusas = statusas;
    }

    public int getPrekiuKiekis() {
        return prekiuKiekis;
    }

    public void setPrekiuKiekis(int prekiuKiekis) {
        this.prekiuKiekis = prekiuKiekis;
    }

    public List<NurasymoPrekeDto> getPrekes() {
        if (prekes == null)
            prekes = new ArrayList<NurasymoPrekeDto>();
        return prekes;
    }
}

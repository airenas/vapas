package com.aireno.dto;

import com.aireno.base.DtoBase;

import java.math.BigDecimal;
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
public class SaskaitaDto extends DtoBase {
    private long tiekejasId;
    private long imoneId;
    private Date data;
    private String statusas;
    private boolean arSaskaita;
    private BigDecimal sumaSuPvm;
    private BigDecimal sumaBePvm;
    private BigDecimal sumaPvm;
    private int prekiuKiekis;
    private List<SaskaitosPrekeDto> prekes;

    public String getNumeris() {
        return numeris;
    }

    public void setNumeris(String numeris) {
        this.numeris = numeris;
    }

    private String numeris;

    public long getTiekejasId() {
        return tiekejasId;
    }

    public void setTiekejasId(long tiekejasId) {
        this.tiekejasId = tiekejasId;
    }

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

    public boolean isArSaskaita() {
        return arSaskaita;
    }

    public void setArSaskaita(boolean arSaskaita) {
        this.arSaskaita = arSaskaita;
    }

    public BigDecimal getSumaSuPvm() {
        return sumaSuPvm;
    }

    public void setSumaSuPvm(BigDecimal sumaSuPvm) {
        this.sumaSuPvm = sumaSuPvm;
    }

    public BigDecimal getSumaBePvm() {
        return sumaBePvm;
    }

    public void setSumaBePvm(BigDecimal sumaBePvm) {
        this.sumaBePvm = sumaBePvm;
    }

    public BigDecimal getSumaPvm() {
        return sumaPvm;
    }

    public void setSumaPvm(BigDecimal sumaPvm) {
        this.sumaPvm = sumaPvm;
    }

    public int getPrekiuKiekis() {
        return prekiuKiekis;
    }

    public void setPrekiuKiekis(int prekiuKiekis) {
        this.prekiuKiekis = prekiuKiekis;
    }

    public List<SaskaitosPrekeDto> getPrekes() {
        if (prekes == null)
            prekes = new ArrayList<SaskaitosPrekeDto>();
        return prekes;
    }

    public void setPrekes(List<SaskaitosPrekeDto> prekes) {
        this.prekes = prekes;
    }
}

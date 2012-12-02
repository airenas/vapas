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
public class SaskaitosPrekeDto extends DtoBase {
    private String serija;
    private long prekeId;
    private BigDecimal kiekis;
    private long matVienetasId;
    private Date galiojaIki;
    private BigDecimal kaina;
    private BigDecimal nuolaidosProc;
    private BigDecimal pvm;

    public String getSerija() {
        return serija;
    }

    public void setSerija(String serija) {
        this.serija = serija;
    }

    public long getPrekeId() {
        return prekeId;
    }

    public void setPrekeId(long prekeId) {
        this.prekeId = prekeId;
    }

    public BigDecimal getKiekis() {
        return kiekis;
    }

    public void setKiekis(BigDecimal kiekis) {
        this.kiekis = kiekis;
    }

    public long getMatVienetasId() {
        return matVienetasId;
    }

    public void setMatVienetasId(long matVienetasId) {
        this.matVienetasId = matVienetasId;
    }

    public Date getGaliojaIki() {
        return galiojaIki;
    }

    public void setGaliojaIki(Date galiojaIki) {
        this.galiojaIki = galiojaIki;
    }

    public BigDecimal getKaina() {
        return kaina;
    }

    public void setKaina(BigDecimal kaina) {
        this.kaina = kaina;
    }

    public BigDecimal getNuolaidosProc() {
        return nuolaidosProc;
    }

    public void setNuolaidosProc(BigDecimal nuolaidosProc) {
        this.nuolaidosProc = nuolaidosProc;
    }

    public BigDecimal getPvm() {
        return pvm;
    }

    public void setPvm(BigDecimal pvm) {
        this.pvm = pvm;
    }
}

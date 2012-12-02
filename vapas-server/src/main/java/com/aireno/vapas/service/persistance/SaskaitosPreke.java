package com.aireno.vapas.service.persistance;

import com.aireno.vapas.service.base.EntityBase;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class SaskaitosPreke extends EntityBase implements Serializable {
    private String serija;
    private long prekeId;
    private long saskaitaId;
    private BigDecimal kiekis;
    private long matavimoVienetasId;
    private String matavimoVienetas;
    private Date galiojaIki;
    private BigDecimal kainaBePvm;
    private BigDecimal nuolaidosProc;
    private BigDecimal pvm;
    private BigDecimal sumaSuPvm;
    private BigDecimal sumaBePvm;
    private BigDecimal sumaPvm;

    public BigDecimal getSumaPvm() {
        return sumaPvm;
    }

    public void setSumaPvm(BigDecimal sumaPvm) {
        this.sumaPvm = sumaPvm;
    }

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

    public long getMatavimoVienetasId() {
        return matavimoVienetasId;
    }

    public void setMatavimoVienetasId(long matavimoVienetasId) {
        this.matavimoVienetasId = matavimoVienetasId;
    }

    public String getMatavimoVienetas() {
        return matavimoVienetas;
    }

    public void setMatavimoVienetas(String matavimoVienetas) {
        this.matavimoVienetas = matavimoVienetas;
    }

    public Date getGaliojaIki() {
        return galiojaIki;
    }

    public void setGaliojaIki(Date galiojaIki) {
        this.galiojaIki = galiojaIki;
    }

    public BigDecimal getKainaBePvm() {
        return kainaBePvm;
    }

    public void setKainaBePvm(BigDecimal kainaBePvm) {
        this.kainaBePvm = kainaBePvm;
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

    public long getSaskaitaId() {
        return saskaitaId;
    }

    public void setSaskaitaId(long saskaitaId) {
        this.saskaitaId = saskaitaId;
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
}

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
public class NurasymoPrekeDto extends DtoBase {
    private String serija;
    private long prekeId;
    private BigDecimal kiekis;
    private long matVienetasId;
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
}

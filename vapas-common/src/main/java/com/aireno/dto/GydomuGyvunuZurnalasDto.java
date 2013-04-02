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
public class GydomuGyvunuZurnalasDto extends DtoBase {
    private long eilesNumeris;
    private long imoneId;
    private String laikytojas;
    private Date registracijosData;
    private Date pabaigosData;
    private Date pirmuPozymiuData;
    private String diagnoze;
    private String gyvunuSarasas;
    private String gydymas;
    private List<ZurnaloVaistasDto> vaistai;
    private List<ZurnaloGyvunasDto> gyvunai;
    private Date islaukaPienui;
    private Date islaukaMesai;


    public Date getPabaigosData() {
        return pabaigosData;
    }

    public void setPabaigosData(Date pabaigosData) {
        this.pabaigosData = pabaigosData;
    }

    public Date getIslaukaPienui() {
        return islaukaPienui;
    }

    public void setIslaukaPienui(Date islaukaPienui) {
        this.islaukaPienui = islaukaPienui;
    }

    public Date getIslaukaMesai() {
        return islaukaMesai;
    }

    public void setIslaukaMesai(Date islaukaMesai) {
        this.islaukaMesai = islaukaMesai;
    }

    public List<ZurnaloVaistasDto> getVaistai() {
        if (vaistai == null) {
            vaistai = new ArrayList<ZurnaloVaistasDto>();
        }
        return vaistai;
    }

    public List<ZurnaloGyvunasDto> getGyvunai() {
        if (gyvunai == null) {
            gyvunai = new ArrayList<ZurnaloGyvunasDto>();
        }
        return gyvunai;
    }


    public long getEilesNumeris() {
        return eilesNumeris;
    }

    public void setEilesNumeris(long eilesNumeris) {
        this.eilesNumeris = eilesNumeris;
    }

    public long getImoneId() {
        return imoneId;
    }

    public void setImoneId(long imoneId) {
        this.imoneId = imoneId;
    }

    public String getLaikytojas() {
        return laikytojas;
    }

    public void setLaikytojas(String laikytojas) {
        this.laikytojas = laikytojas;
    }

    public Date getRegistracijosData() {
        return registracijosData;
    }

    public void setRegistracijosData(Date registracijosData) {
        this.registracijosData = registracijosData;
    }

    public Date getPirmuPozymiuData() {
        return pirmuPozymiuData;
    }

    public void setPirmuPozymiuData(Date pirmuPozymiuData) {
        this.pirmuPozymiuData = pirmuPozymiuData;
    }

    public String getDiagnoze() {
        return diagnoze;
    }

    public void setDiagnoze(String diagnoze) {
        this.diagnoze = diagnoze;
    }

    public String getGyvunuSarasas() {
        return gyvunuSarasas;
    }

    public void setGyvunuSarasas(String gyvunuSarasas) {
        this.gyvunuSarasas = gyvunuSarasas;
    }

    public String getGydymas() {
        return gydymas;
    }

    public void setGydymas(String gydymas) {
        this.gydymas = gydymas;
    }
}

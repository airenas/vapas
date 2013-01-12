package com.aireno.vapas.gui.gydymozurnalas;

import com.aireno.base.DtoBase;
import com.aireno.dto.ZurnaloGyvunasDto;
import com.aireno.dto.ZurnaloVaistasDto;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

public class ZurnaloGyvunasGui extends DtoBase {
    private final SimpleStringProperty numeris = new SimpleStringProperty();
    private final SimpleStringProperty amzius = new SimpleStringProperty();
    private final SimpleLongProperty gyvunoRusisId = new SimpleLongProperty();

    public ZurnaloGyvunasGui() {
    }

    public ZurnaloGyvunasGui(ZurnaloGyvunasDto copy) {
        super(copy);
        setAmzius(copy.getAmzius());
        setGyvunoRusisId(copy.getGyvunoRusisId());
        setNumeris(copy.getNumeris());
    }



    public SimpleLongProperty gyvunoRusisIdProperty() {
        return gyvunoRusisId;
    }

    public Long getGyvunoRusisId() {
        return gyvunoRusisId.get();
    }

    public void setGyvunoRusisId(Long gyvunoRusisId) {
        this.gyvunoRusisId.set(gyvunoRusisId);
    }

    public SimpleStringProperty numerisProperty() {
        return numeris;
    }

    public SimpleStringProperty amziusProperty() {
        return amzius;
    }

    public String getAmzius() {
        return amzius.get();
    }

    public void setAmzius(String amzius) {
        this.amzius.set(amzius);
    }

    public String getNumeris() {
        return numeris.get();
    }

    public void setNumeris(String numeris) {
        this.numeris.set(numeris);
    }

    public ZurnaloGyvunasDto toDto() {
        ZurnaloGyvunasDto result = new ZurnaloGyvunasDto();
        result.setGyvunoRusisId(getGyvunoRusisId());
        result.setNumeris(getNumeris());
        result.setAmzius(getAmzius());

        result.setId(getId());
        return result;
    }
}

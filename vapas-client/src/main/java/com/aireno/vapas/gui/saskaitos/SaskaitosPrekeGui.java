package com.aireno.vapas.gui.saskaitos;

import com.aireno.base.DtoBase;
import com.aireno.dto.SaskaitosPrekeDto;
import com.aireno.dto.ZurnaloVaistasDto;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;
import java.util.Date;

public class SaskaitosPrekeGui extends DtoBase {
    private final SimpleStringProperty serija = new SimpleStringProperty();
    private final SimpleLongProperty prekeId = new SimpleLongProperty();
    private final ObjectProperty<BigDecimal> kiekis = new SimpleObjectProperty<BigDecimal>(this, "kiekis");
    private final ObjectProperty<Date> galiojaIki = new SimpleObjectProperty<Date>(this, "galiojaIki");
    private final ObjectProperty<BigDecimal> kaina = new SimpleObjectProperty<BigDecimal>(this, "kaina");

    public SaskaitosPrekeGui() {
    }

    public SaskaitosPrekeGui(SaskaitosPrekeDto copy) {
        super(copy);
        setKiekis(copy.getKiekis());
        setPrekeId(copy.getPrekeId());
        setSerija(copy.getSerija());
        setGaliojaIki(copy.getGaliojaIki());
        setKaina(copy.getKaina());
    }



    public SimpleStringProperty serijaProperty() {
        return serija;
    }

    public SimpleLongProperty prekeIdProperty() {
        return prekeId;
    }

    public ObjectProperty<BigDecimal> kiekisProperty() {
        return kiekis;
    }

    public ObjectProperty<Date> galiojaIkiProperty() {
        return galiojaIki;
    }

    public ObjectProperty<BigDecimal> kainaProperty() {
        return kaina;
    }

    public String getSerija() {
        return serija.getValue();
    }

    public void setSerija(String serija) {
        this.serija.setValue(serija);
    }

    public long getPrekeId() {
        return prekeId.getValue();
    }

    public void setPrekeId(long prekeId) {
        this.prekeId.setValue(prekeId);
    }

    public BigDecimal getKiekis() {
        return kiekis.getValue();
    }

    public void setKiekis(BigDecimal kiekis) {
        this.kiekis.setValue(kiekis);
    }

    public Date getGaliojaIki() {
        return galiojaIki.getValue();
    }

    public void setGaliojaIki(Date galiojaIki) {
        this.galiojaIki.setValue(galiojaIki);
    }

    public BigDecimal getKaina() {
        return kaina.getValue();
    }

    public void setKaina(BigDecimal kaina) {
        this.kaina.setValue(kaina);
    }


    public SaskaitosPrekeDto toDto() {
        SaskaitosPrekeDto result = new SaskaitosPrekeDto();
        result.setKiekis(getKiekis());
        result.setPrekeId(getPrekeId());
        result.setKaina(getKaina());
        result.setSerija(getSerija());
        result.setGaliojaIki(getGaliojaIki());

        result.setId(getId());
        return result;
    }
}

package com.aireno.dto;

import com.aireno.base.DtoBase;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

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
    private final SimpleStringProperty serija = new SimpleStringProperty();
    private final SimpleLongProperty prekeId = new SimpleLongProperty();

    public SimpleStringProperty serijaProperty() {
        return serija;
    }

    public SimpleLongProperty prekeIdProperty() {
        return prekeId;
    }

    private BigDecimal kiekis;
    private long matVienetasId;

    public NurasymoPrekeDto() {
    }

    public NurasymoPrekeDto(NurasymoPrekeDto copy) {
        super(copy);
        serija.set(copy.serija.get());
        prekeId.set(copy.prekeId.get());
        kiekis = copy.kiekis;
    }

    public String getSerija() {
        return serija.get();
    }

    public void setSerija(String serija) {
        this.serija.set(serija);
    }

    public long getPrekeId() {
        return prekeId.get();
    }

    public void setPrekeId(long prekeId) {
        this.prekeId.set(prekeId);
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

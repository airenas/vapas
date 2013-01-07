package com.aireno.vapas.gui.gydymozurnalas;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.DtoBase;
import com.aireno.base.LookupDto;
import com.aireno.dto.GydomuGyvunuZurnalasDto;
import com.aireno.dto.NurasymoPrekeDto;
import com.aireno.dto.StringLookupItemDto;
import com.aireno.dto.ZurnaloVaistasDto;
import com.aireno.utils.ADateUtils;
import com.aireno.utils.ANumberUtils;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.EditFieldDefinition;
import com.aireno.vapas.gui.base.EntityPresenterBase;
import com.aireno.vapas.gui.base.GuiPresenter;
import com.aireno.vapas.gui.base.ListDefinition;
import com.aireno.vapas.gui.controls.FilterLookup;
import com.aireno.vapas.gui.controls.StringFilterLookup;
import com.aireno.vapas.gui.tablefields.DecimalFieldDefinition;
import com.aireno.vapas.gui.tablefields.LookupFieldDefinitionCB;
import com.aireno.vapas.service.GydomuGyvunuZurnalasService;
import com.aireno.vapas.service.LookupService;
import com.panemu.tiwulfx.form.DateControl;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ZurnaloVaistasGui extends DtoBase {
    private final SimpleStringProperty receptas = new SimpleStringProperty();
    private final SimpleLongProperty prekeId = new SimpleLongProperty();
    private final ObjectProperty<BigDecimal> kiekis = new SimpleObjectProperty<BigDecimal>(this, "kiekis");
    private final SimpleLongProperty matavimoVienetasId = new SimpleLongProperty();

    public ZurnaloVaistasGui() {
    }

    public ZurnaloVaistasGui(ZurnaloVaistasDto copy) {
        super(copy);
        setKiekis(copy.getKiekis());
        setPrekeId(copy.getPrekeId());
        setMatavimoVienetasId(copy.getMatavimoVienetasId());
        setReceptas(copy.getReceptas());
    }



    public SimpleLongProperty matavimoVienetasIdProperty() {
        return matavimoVienetasId;
    }

    public Long getMatavimoVienetasId() {
        return matavimoVienetasId.get();
    }

    public void setMatavimoVienetasId(Long matavimoVienetasId) {
        this.matavimoVienetasId.set(matavimoVienetasId);
    }

    public SimpleStringProperty receptasProperty() {
        return receptas;
    }

    public SimpleLongProperty prekeIdProperty() {
        return prekeId;
    }

    public String getReceptas() {
        return receptas.get();
    }

    public void setReceptas(String receptas) {
        this.receptas.set(receptas);
    }

    public long getPrekeId() {
        return prekeId.get();
    }

    public void setPrekeId(long prekeId) {
        this.prekeId.set(prekeId);
    }

    public BigDecimal getKiekis() {
        return kiekis.getValue();
    }

    public void setKiekis(BigDecimal kiekis) {
        this.kiekis.setValue(kiekis);
    }

    public ZurnaloVaistasDto toDto() {
        ZurnaloVaistasDto result = new ZurnaloVaistasDto();
        result.setKiekis(getKiekis());
        result.setPrekeId(getPrekeId());
        result.setMatavimoVienetasId(getMatavimoVienetasId());
        result.setReceptas(getReceptas());
        result.setId(getId());
        return result;
    }
}

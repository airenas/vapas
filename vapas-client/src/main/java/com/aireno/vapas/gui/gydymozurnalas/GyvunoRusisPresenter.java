package com.aireno.vapas.gui.gydymozurnalas;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.LookupDto;
import com.aireno.dto.LookupItemDto;
import com.aireno.dto.NurasymasDto;
import com.aireno.dto.NurasymoPrekeDto;
import com.aireno.dto.StringLookupItemDto;
import com.aireno.utils.ADateUtils;
import com.aireno.utils.ANumberUtils;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.EditFieldDefinition;
import com.aireno.vapas.gui.base.EntityPresenterBase;
import com.aireno.vapas.gui.base.GuiPresenter;
import com.aireno.vapas.gui.base.ListDefinition;
import com.aireno.vapas.gui.controls.FilterLookup;
import com.aireno.vapas.gui.tablefields.DecimalFieldDefinition;
import com.aireno.vapas.gui.tablefields.LookupFieldDefinitionCB;
import com.aireno.vapas.gui.tablefields.StringLookupFieldDefinitionCB;
import com.aireno.vapas.service.GydomuGyvunuZurnalasService;
import com.aireno.vapas.service.LookupService;
import com.aireno.vapas.service.NurasymasService;
import com.panemu.tiwulfx.form.DateControl;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class GyvunoRusisPresenter extends EntityPresenterBase<LookupItemDto> {
    @FXML
    private Node root;
    @FXML
    private Node bPane;

    @FXML
    private TextField pavadinimas;
    @FXML
    private Button bSaugoti;

    public Node getView() {
        return root;
    }

    public GydomuGyvunuZurnalasService getService() {
        return ApplicationContextProvider.getProvider().getBean(GydomuGyvunuZurnalasService.class);
    }

    @Override
    public boolean init() throws Exception {
        initializing = true;

        try {

            if (id > 0) {
                try {
                    item = getService().gautiGyvunoRusi(id);
                    this.setText("Gauta");
                    pavadinimas.setText(item.getPavadinimas());

                } catch (Exception e) {
                    this.setText(e.getLocalizedMessage());
                    return false;
                }
            } else {
                item = new LookupItemDto();
                pavadinimas.setText("");
            }
        } finally {
            initializing = false;
        }

        return true;
    }

    @Override
    public Node getButtonView() {
        return bPane;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void saugotiInt() throws Exception {
        LookupItemDto dto = new LookupItemDto();
        dto.setPavadinimas(pavadinimas.getText());
        dto.setId(getId());
        item = getService().saugotiGyvunoRusi(dto);
        update();
    }

    @Override
    public void updateControls() {
        boolean pakeista = pakeista();
        bSaugoti.setDisable(!pakeista);
    }

    public String getTitle() {
        return "Gyvūno rūšis " + (getId() == 0 ? "nauja" : pavadinimas.getText());
    }

    @Override
    protected boolean pakeista() {

        if (!StringUtils.equals(StringUtils.defaultString(item.getPavadinimas()), pavadinimas.getText()))
            return true;
        return false;
    }
}

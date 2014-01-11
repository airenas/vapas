package com.aireno.vapas.gui.gydymozurnalas;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.LookupDto;
import com.aireno.dto.GydomuGyvunuZurnalasDto;
import com.aireno.dto.ZurnaloVaistasDto;
import com.aireno.utils.ADateUtils;
import com.aireno.utils.ANumberUtils;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.EditFieldDefinition;
import com.aireno.vapas.gui.base.EntityPresenterBase;
import com.aireno.vapas.gui.base.ListDefinition;
import com.aireno.vapas.gui.controls.FilterLookup;
import com.aireno.vapas.gui.tablefields.DecimalFieldDefinition;
import com.aireno.vapas.gui.tablefields.LookupFieldDefinitionCB;
import com.aireno.vapas.gui.tablefields.TextFieldDefinition;
import com.aireno.vapas.service.GydomuGyvunuZurnalasService;
import com.aireno.vapas.service.LookupService;
import com.panemu.tiwulfx.form.DateControl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GydymoZurnalasNurasymuiPresenter extends EntityPresenterBase<GydomuGyvunuZurnalasDto> {
    @FXML
    private Node root;
    @FXML
    private Node bPane;

    @FXML
    private DateControl data;
    @FXML
    private FilterLookup<LookupDto> imone;
    @FXML
    private TableView<ZurnaloVaistasGui> prekes;
    @FXML
    private Button bSaugoti;
    @FXML
    private Button bPridetiPreke;

    ObservableList<ZurnaloVaistasGui> prekesList;

    public Node getView() {
        return root;
    }

    public GydomuGyvunuZurnalasService getService() {
        return ApplicationContextProvider.getProvider().getBean(GydomuGyvunuZurnalasService.class);
    }

    @Override
    protected void initOnce() throws Exception {
        prekesList = FXCollections.observableArrayList();
        data.dateFormatProperty().setValue(new SimpleDateFormat(com.aireno.Constants.DATE_FORMAT));
        data.getInputComponent().selectedDateProperty()
                .addListener(new ChangeListener<Date>() {
                    @Override
                    public void changed(ObservableValue<? extends Date> observableValue, Date date, Date date2) {
                        if (initializing) {
                            return;
                        }
                        update();
                    }
                });
        imone.setEditable(true);
        imone.setData(getLookupService().
                sarasas(new LookupService.LookupRequest(com.aireno.Constants.LOOKUP_IMONE)));
        imone.valueProperty().addListener(new ChangeListener<LookupDto>() {
            @Override
            public void changed(ObservableValue<? extends LookupDto> observableValue, LookupDto lookupDto, LookupDto lookupDto2) {
                if (initializing) {
                    return;
                }
                update();

            }
        });
        MListDefinition def = new MListDefinition();
        prekes.setEditable(true);
        def.InitTable(prekes);
    }

    @Override
    protected void initInternal() throws Exception {
        if (id > 0) {
            item = getService().gauti(id);
        } else {
            item = new GydomuGyvunuZurnalasDto();
            item.setRegistracijosData(new Date());
        }
        data.setValue(item.getRegistracijosData());
        imone.setValueId(item.getImoneId());

        prekesList.clear();
        for (ZurnaloVaistasDto i : item.getVaistai()) {
            prekesList.add(new ZurnaloVaistasGui(i));
        }

        prekes.setItems(prekesList);

    }

    @Override
    public Node getButtonView() {
        return bPane;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void saugotiInt() throws Exception {
        GydomuGyvunuZurnalasDto dto = new GydomuGyvunuZurnalasDto();
        //dto.setNumeris(numeris.getText());
        dto.setRegistracijosData(data.getValue());
        dto.setArNurasymas(true);

        dto.setImoneId(imone.getValueId());

        dto.setId(getId());
        dto.getVaistai().clear();
        for (ZurnaloVaistasGui item : prekesList) {
            if (item.getPrekeId() > 0) {
                dto.getVaistai().add(item.toDto());
            }
        }
        dto.getGyvunai().clear();
        item = getService().saugotiNurasyma(dto);
        initializing = true;
        try {
            prekesList.clear();
            for (ZurnaloVaistasDto i : item.getVaistai()) {
                prekesList.add(new ZurnaloVaistasGui(i));
            }
            this.setText("Išsaugota");
        } finally {
            initializing = false;
        }
        update();
    }

    class MListDefinition extends ListDefinition<ZurnaloVaistasGui> {
        MListDefinition() {
            fields.add(new LookupFieldDefinitionCB<ZurnaloVaistasGui, LookupDto>("Vaistas", 250,
                    new PropertyValueFactory<ZurnaloVaistasGui, Long>("prekeId"),
                    new EditFieldDefinition.ChangeEvent<ZurnaloVaistasGui, Long>() {

                        @Override
                        public void handle(ChangeEventParam<ZurnaloVaistasGui, Long> param) {
                            if (initializing) {
                                return;
                            }
                            if (param.item.getPrekeId() != param.value) {
                                param.item.setPrekeId(param.value);
                                param.item.setReceptas(null);

                                // param.item.setMatavimoVienetasId(getLookupService().gautiPrekesMatavimoVieneta(param.value));
                                update();
                            }
                        }
                    }
                    , new LookupFieldDefinitionCB.DataProvider<LookupDto>() {

                @Override
                public List<LookupDto> getItems() throws Exception {
                    return getLookupService().sarasas(new LookupService.LookupRequest(com.aireno.Constants.LOOKUP_PREKE));
                }
            }
            ));

            fields.add(new DecimalFieldDefinition<ZurnaloVaistasGui>("Kiekis", 100,
                    new PropertyValueFactory<ZurnaloVaistasGui, BigDecimal>("kiekis"),
                    new EditFieldDefinition.ChangeEvent<ZurnaloVaistasGui, BigDecimal>() {
                        @Override
                        public void handle(ChangeEventParam<ZurnaloVaistasGui, BigDecimal> param) {
                            param.item.setKiekis(param.value);
                            update();
                        }
                    })
            );
            fields.add(new TextFieldDefinition("Priežastis", 600, new PropertyValueFactory<ZurnaloVaistasGui, String>("priezastis"),
                    new EditFieldDefinition.ChangeEvent<ZurnaloVaistasGui, String>() {
                        @Override
                        public void handle(ChangeEventParam<ZurnaloVaistasGui, String> param) {
                            param.item.setPriezastis(param.value);
                            update();
                        }
                    }));
        }
    }

    @Override
    public void updateControls() {
        boolean pakeista = pakeista();
        bSaugoti.setDisable(!pakeista);
    }

    public String getTitle() {
        return "Nurašymas " + (getId() == 0 ? "naujas" : "");
    }

    @Override
    protected boolean pakeista() {

        if (!ADateUtils.equalDate(item.getRegistracijosData(), data.getValue()))
            return true;
        if (item.getImoneId() != imone.getValueId())
            return true;

        for (ZurnaloVaistasGui dto : prekesList) {
            if (pakeistaPreke(dto, item.getVaistai())) {
                return true;
            }
        }


        return false;
    }

    private boolean pakeistaPreke(ZurnaloVaistasGui dto, List<ZurnaloVaistasDto> prekes) {
        ZurnaloVaistasDto preke = rask(prekes, dto.getId());
        if (preke == null)
            return true;
        if (!StringUtils.equals(dto.getPriezastis(), preke.getPriezastis()))
            return true;
        if (dto.getPrekeId() != preke.getPrekeId())
            return true;
        if (!ANumberUtils.equal(dto.getKiekis(), preke.getKiekis()))
            return true;
        return false;

    }

    public void pridetiPreke() {
        prekesList.add(new ZurnaloVaistasGui());
    }


    @Override
    public void save() throws Exception {
        saugotiInt();
    }

    public void saugoti(ActionEvent event) {
        try {

            super.saugoti(event);
        } catch (Exception e) {
            this.setText("Klaida: " + e.getLocalizedMessage());
        }
    }
}

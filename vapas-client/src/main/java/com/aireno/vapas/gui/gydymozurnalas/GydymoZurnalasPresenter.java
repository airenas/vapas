package com.aireno.vapas.gui.gydymozurnalas;

import com.aireno.base.ApplicationContextProvider;
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
import com.aireno.vapas.gui.tablefields.StringLookupFieldDefinitionCB;
import com.aireno.vapas.service.GydomuGyvunuZurnalasService;
import com.aireno.vapas.service.LookupService;
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
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class GydymoZurnalasPresenter extends EntityPresenterBase<GydomuGyvunuZurnalasDto> implements Initializable, GuiPresenter {
    @FXML
    private Node root;
    @FXML
    private Node bPane;

    @FXML
    private StringFilterLookup laikytojas;
    @FXML
    private StringFilterLookup diagnoze;
    @FXML
    private DateControl data;
    @FXML
    private FilterLookup<LookupDto> imone;
    @FXML
    private TableView<NurasymoPrekeDto> gyvunai;
    @FXML
    private TableView<ZurnaloVaistasGui> prekes;
    @FXML
    private Button bSaugoti;
    @FXML
    private Button bTvirtinti;
    @FXML
    private Button bPridetiPreke;
    @FXML
    private Button bAtaskaita;

    ObservableList<ZurnaloVaistasGui> prekesList;

    ObservableList<NurasymoPrekeDto> gyvunaiList;

    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

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
            prekesList = FXCollections.observableArrayList();
            data.setDateFormat(new SimpleDateFormat(Constants.DATE_FORMAT));
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
            laikytojas.setProvider(new StringFilterLookup.DataProvider() {
                @Override
                public List<StringLookupItemDto> getDataList(String sId) throws Exception {
                    return getService().sarasasLaikytoju(null);
                }
            });
            laikytojas.valueProperty().addListener(new ChangeListener<StringLookupItemDto>() {
                @Override
                public void changed(ObservableValue<? extends StringLookupItemDto> observableValue,
                                    StringLookupItemDto lookupDto, StringLookupItemDto lookupDto2) {
                    if (initializing) {
                        return;
                    }
                    update();

                }
            });
            laikytojas.setAllowNewItem(true);
            diagnoze.setAllowNewItem(true);
            diagnoze.setProvider(new StringFilterLookup.DataProvider() {
                @Override
                public List<StringLookupItemDto> getDataList(String sId) throws Exception {
                    return getService().sarasasDiagnozes(null);
                }
            });
            diagnoze.valueProperty().addListener(new ChangeListener<StringLookupItemDto>() {
                @Override
                public void changed(ObservableValue<? extends StringLookupItemDto> observableValue,
                                    StringLookupItemDto lookupDto, StringLookupItemDto lookupDto2) {
                    if (initializing) {
                        return;
                    }
                    update();

                }
            });
            if (id > 0) {
                try {
                    item = getService().gauti(id);
                    this.setText("Gauta");
                } catch (Exception e) {
                    this.setText(e.getLocalizedMessage());
                    return false;
                }
            } else {
                item = new GydomuGyvunuZurnalasDto();
            }
            data.setValue(item.getRegistracijosData());
            imone.setValueId(item.getImoneId());
            diagnoze.setStringValue(item.getDiagnoze());
            laikytojas.setStringValue(item.getLaikytojas());
            prekesList.clear();
            for (ZurnaloVaistasDto i : item.getVaistai()) {
                prekesList.add(new ZurnaloVaistasGui(i));
            }

            MListDefinition def = new MListDefinition();
            prekes.setEditable(true);
            def.InitTable(prekes);
            prekes.setItems(prekesList);
        } finally {
            initializing = false;
        }

        return true;  //To change body of implemented methods use File | Settings | File Templates.
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
        dto.setImoneId(imone.getValueId());
        dto.setLaikytojas(laikytojas.getStringValue());
        dto.setDiagnoze(diagnoze.getStringValue());
        dto.setId(getId());
        dto.getVaistai().clear();
        for (ZurnaloVaistasGui item : prekesList) {
            if (item.getPrekeId() > 0) {
                dto.getVaistai().add(item.toDto());
            }
        }

        item = getService().saugoti(dto);
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

    public void tvirtinti(ActionEvent event) {
        try {
            //getService().tvirtinti(getId());
            this.setText("Patvirtinta");
            item = getService().gauti(getId());
            update();
        } catch (Exception e) {
            this.setError("Klaida tvirtinant: ", e);
        }
    }

    public void generuotiAtaskaita(ActionEvent event) {
        try {
            // getService().generuotiAtaskaita(getId());
            this.setText("Sugeneruota");
        } catch (Exception e) {
            this.setError("Klaida generuojant ataskaita: ", e);
        }
    }

    class MListDefinition extends ListDefinition<ZurnaloVaistasGui> {
        MListDefinition() {
            fields.add(new LookupFieldDefinitionCB<ZurnaloVaistasGui, LookupDto>("Prekė", 150,
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

            fields.add(new StringLookupFieldDefinitionCB("Receptas", 400, new PropertyValueFactory<ZurnaloVaistasGui, String>("receptas"),
                    new EditFieldDefinition.ChangeEvent<ZurnaloVaistasGui, String>() {
                        @Override
                        public void handle(ChangeEventParam<ZurnaloVaistasGui, String> param) {
                            param.item.setReceptas(param.value);
                            update();
                        }
                    },
                    new StringLookupFieldDefinitionCB.DataProvider<ZurnaloVaistasGui>() {

                        @Override
                        public List<StringLookupItemDto> getDataList(ZurnaloVaistasGui item, String sId) throws Exception {
                            List<StringLookupItemDto> result = getService()
                                    .sarasasReceptai(new GydomuGyvunuZurnalasService.ReceptaiRequest(item.getPrekeId()));
                            return result;
                        }
                    }
            ));
        }
    }

    @Override
    public void updateControls() {
        boolean pakeista = pakeista();
        bSaugoti.setDisable(!pakeista);
//        bTvirtinti.setDisable(pakeista );
//        bPridetiPreke.setDisable(true);
//        bAtaskaita.setDisable(true);
    }

    public String getTitle() {
        return "Žurnalo įrašas " + (getId() == 0 ? "naujas" : laikytojas.getStringValue());
    }

    @Override
    protected boolean pakeista() {

        if (!ADateUtils.equalDate(item.getRegistracijosData(), data.getValue()))
            return true;
            /* if (!StringUtils.equals(StringUtils.defaultString(item.getNumeris()), numeris.getText()))
            return true;
        if (item.getImoneId() != imone.getValueId())
            return true;

        for (NurasymoPrekeDto dto : prekesList) {
            if (pakeistaPreke(dto, item.getPrekes())) {
                return true;
            }
        }*/
        return false;
    }

    private boolean pakeistaPreke(NurasymoPrekeDto dto, List<NurasymoPrekeDto> prekes) {
        NurasymoPrekeDto preke = raskPreke(prekes, dto.getId());
        if (preke == null)
            return true;
        if (!StringUtils.equals(dto.getSerija(), preke.getSerija()))
            return true;
        if (dto.getPrekeId() != preke.getPrekeId())
            return true;
        if (!ANumberUtils.equal(dto.getKiekis(), preke.getKiekis()))
            return true;
        return false;

    }

    private NurasymoPrekeDto raskPreke(List<NurasymoPrekeDto> prekes, long id) {
        if (prekes == null)
            return null;
        for (NurasymoPrekeDto dto : prekes) {
            if (dto.getId() == id) {
                return dto;
            }
        }
        return null;
    }

    public void pridetiPreke() {
        prekesList.add(new ZurnaloVaistasGui());
    }

    public void pridetiGyvuna() {
        //prekesList.add(new ZurnaloVaistasGui());
    }
}

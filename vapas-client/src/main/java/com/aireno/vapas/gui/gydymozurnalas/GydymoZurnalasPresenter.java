package com.aireno.vapas.gui.gydymozurnalas;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.LookupDto;
import com.aireno.dto.*;
import com.aireno.utils.ADateUtils;
import com.aireno.utils.ANumberUtils;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.EditFieldDefinition;
import com.aireno.vapas.gui.base.EntityPresenterBase;
import com.aireno.vapas.gui.base.ListDefinition;
import com.aireno.vapas.gui.controls.FilterLookup;
import com.aireno.vapas.gui.controls.StringFilterLookup;
import com.aireno.vapas.gui.controls.StringNewLookup;
import com.aireno.vapas.gui.tablefields.DecimalFieldDefinition;
import com.aireno.vapas.gui.tablefields.LookupFieldDefinitionCB;
import com.aireno.vapas.gui.tablefields.StringLookupFieldDefinitionCB;
import com.aireno.vapas.service.GydomuGyvunuZurnalasService;
import com.aireno.vapas.service.LookupService;
import com.mytdev.javafx.scene.control.AutoCompleteTextField;
import com.panemu.tiwulfx.form.DateControl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class GydymoZurnalasPresenter extends EntityPresenterBase<GydomuGyvunuZurnalasDto> {
    @FXML
    private Node root;
    @FXML
    private Node bPane;

    @FXML
    private StringNewLookup laikytojas;
    @FXML
    private StringNewLookup diagnoze;
    @FXML
    private DateControl data;
    @FXML
    private FilterLookup<LookupDto> imone;
    @FXML
    private TableView<ZurnaloGyvunasGui> gyvunai;
    @FXML
    private TableView<ZurnaloVaistasGui> prekes;
    @FXML
    private Button bSaugoti;
    @FXML
    private Button bPridetiPreke;

    ObservableList<ZurnaloVaistasGui> prekesList;

    ObservableList<ZurnaloGyvunasGui> gyvunaiList;

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
            gyvunaiList = FXCollections.observableArrayList();
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

           // List<StringLookupItemDto> getLDataList = getService().sarasasLaikytoju(null);
            //ObservableList<String> laikytojuList =
              //      FXCollections.observableArrayList();
           /* for (StringLookupItemDto v : getLDataList) {
                laikytojuList.add(v.getId());
            }
            laikytojas.setItems(laikytojuList);*/
            laikytojas.setEditable(true);

            laikytojas.setProvider(new StringNewLookup.DataProvider() {
                @Override
                public List<String> getDataList(String sId) throws Exception {
                    return getService().sarasasLaikytoju(null);
                }
            });
            laikytojas.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue,
                                    String lookupDto, String lookupDto2) {
                    if (initializing) {
                        return;
                    }
                    update();

                }
            });

            diagnoze.setProvider(new StringNewLookup.DataProvider() {
                @Override
                public List<String> getDataList(String sId) throws Exception {
                    return getService().sarasasDiagnozes(null);
                }
            });
            diagnoze.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue,
                                    String lookupDto, String lookupDto2) {
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
                item.setRegistracijosData(new Date());
            }
            data.setValue(item.getRegistracijosData());
            imone.setValueId(item.getImoneId());
            diagnoze.setStringValue(item.getDiagnoze());
            laikytojas.setStringValue(item.getLaikytojas());
            prekesList.clear();
            for (ZurnaloVaistasDto i : item.getVaistai()) {
                prekesList.add(new ZurnaloVaistasGui(i));
            }
            gyvunaiList.clear();
            for (ZurnaloGyvunasDto i : item.getGyvunai()) {
                gyvunaiList.add(new ZurnaloGyvunasGui(i));
            }

            MListDefinition def = new MListDefinition();
            prekes.setEditable(true);
            def.InitTable(prekes);
            prekes.setItems(prekesList);

            MListDefinitionG defG = new MListDefinitionG();
            gyvunai.setEditable(true);
            defG.InitTable(gyvunai);
            gyvunai.setItems(gyvunaiList);
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
        dto.setLaikytojas((String)laikytojas.getValue());
        dto.setDiagnoze(diagnoze.getValue());
        dto.setId(getId());
        dto.getVaistai().clear();
        for (ZurnaloVaistasGui item : prekesList) {
            if (item.getPrekeId() > 0) {
                dto.getVaistai().add(item.toDto());
            }
        }
        dto.getGyvunai().clear();
        for (ZurnaloGyvunasGui item : gyvunaiList) {
            if (item.getGyvunoRusisId() > 0) {
                dto.getGyvunai().add(item.toDto());
            }
        }

        item = getService().saugoti(dto);
        initializing = true;
        try {
            prekesList.clear();
            for (ZurnaloVaistasDto i : item.getVaistai()) {
                prekesList.add(new ZurnaloVaistasGui(i));
            }
            gyvunaiList.clear();
            for (ZurnaloGyvunasDto i : item.getGyvunai()) {
                gyvunaiList.add(new ZurnaloGyvunasGui(i));
            }
            this.setText("Išsaugota");
        } finally {
            initializing = false;
        }
        update();
    }

    class MListDefinition extends ListDefinition<ZurnaloVaistasGui> {
        MListDefinition() {
            fields.add(new LookupFieldDefinitionCB<ZurnaloVaistasGui, LookupDto>("Vaistas", 150,
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

            fields.add(new StringLookupFieldDefinitionCB("Receptas", 550, new PropertyValueFactory<ZurnaloVaistasGui, String>("receptas"),
                    new EditFieldDefinition.ChangeEvent<ZurnaloVaistasGui, String>() {
                        @Override
                        public void handle(ChangeEventParam<ZurnaloVaistasGui, String> param) {
                            param.item.setReceptas(param.value);
                            update();
                        }
                    },
                    new StringLookupFieldDefinitionCB.DataProvider<ZurnaloVaistasGui>() {

                        @Override
                        public List<String> getDataList(ZurnaloVaistasGui item, String sId) throws Exception {
                            List<String> result = getService()
                                    .sarasasReceptai(new GydomuGyvunuZurnalasService.ReceptaiRequest(item.getPrekeId()));
                            return result;
                        }
                    }
            ));
        }
    }

    class MListDefinitionG extends ListDefinition<ZurnaloGyvunasGui> {
        MListDefinitionG() {
            fields.add(new LookupFieldDefinitionCB<ZurnaloGyvunasGui, LookupDto>("Rūšis", 150,
                    new PropertyValueFactory<ZurnaloGyvunasGui, Long>("gyvunoRusisId"),
                    new EditFieldDefinition.ChangeEvent<ZurnaloGyvunasGui, Long>() {

                        @Override
                        public void handle(ChangeEventParam<ZurnaloGyvunasGui, Long> param) {
                            if (initializing) {
                                return;
                            }
                            if (param.item.getGyvunoRusisId() != param.value) {
                                param.item.setGyvunoRusisId(param.value);
                                param.item.setNumeris(null);
                                param.item.setAmzius(null);
                                update();
                            }
                        }
                    }
                    , new LookupFieldDefinitionCB.DataProvider<LookupDto>() {

                @Override
                public List<LookupDto> getItems() throws Exception {
                    return getLookupService().sarasas(new LookupService.LookupRequest(com.aireno.Constants.LOOKUP_GYVUNO_RUSIS));
                }
            }
            ));

           fields.add(new StringLookupFieldDefinitionCB("Numeris", 400, new PropertyValueFactory<ZurnaloGyvunasGui, String>("numeris"),
                    new EditFieldDefinition.ChangeEvent<ZurnaloGyvunasGui, String>() {
                        @Override
                        public void handle(ChangeEventParam<ZurnaloGyvunasGui, String> param) {
                            param.item.setNumeris(param.value);
                            update();
                        }
                    },
                    new StringLookupFieldDefinitionCB.DataProvider<ZurnaloGyvunasGui>() {

                        @Override
                        public List<String> getDataList(ZurnaloGyvunasGui item, String sId) throws Exception {
                            List<String> result = getService()
                                    .sarasasNumeriai(new GydomuGyvunuZurnalasService.NumeriaiRequest(laikytojas.getValue(), item.getGyvunoRusisId()));
                            return result;
                        }
                    }
            ));
            fields.add(new StringLookupFieldDefinitionCB("Amžius", 70, new PropertyValueFactory<ZurnaloGyvunasGui, String>("amzius"),
                    new EditFieldDefinition.ChangeEvent<ZurnaloGyvunasGui, String>() {
                        @Override
                        public void handle(ChangeEventParam<ZurnaloGyvunasGui, String> param) {
                            param.item.setAmzius(param.value);
                            update();
                        }
                    },
                    new StringLookupFieldDefinitionCB.DataProvider<ZurnaloGyvunasGui>() {

                        @Override
                        public List<String> getDataList(ZurnaloGyvunasGui item, String sId) throws Exception {
                            List<String> result = getService()
                                    .sarasasAmzius(new GydomuGyvunuZurnalasService.AmziusRequest(item.getGyvunoRusisId()));
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
        return "Žurnalo įrašas " + (getId() == 0 ? "naujas" : (String)laikytojas.getValue());
    }

    @Override
    protected boolean pakeista() {

        if (!ADateUtils.equalDate(item.getRegistracijosData(), data.getValue()))
            return true;
        if (item.getImoneId() != imone.getValueId())
            return true;
        if (!StringUtils.equals(StringUtils.defaultString(item.getDiagnoze()), diagnoze.getValue()))
            return true;
        if (!StringUtils.equals(StringUtils.defaultString(item.getLaikytojas()), (String)laikytojas.getValue()))
            return true;
       for (ZurnaloVaistasGui dto : prekesList) {
            if (pakeistaPreke(dto, item.getVaistai())) {
                return true;
            }
        }

        for (ZurnaloGyvunasGui dto : gyvunaiList) {
            if (pakeistasGyvunas(dto, item.getGyvunai())) {
                return true;
            }
        }
        return false;
    }

    private boolean pakeistaPreke(ZurnaloVaistasGui dto, List<ZurnaloVaistasDto> prekes) {
        ZurnaloVaistasDto preke = rask(prekes, dto.getId());
        if (preke == null)
            return true;
        if (!StringUtils.equals(dto.getReceptas(), preke.getReceptas()))
            return true;
        if (dto.getPrekeId() != preke.getPrekeId())
            return true;
        if (!ANumberUtils.equal(dto.getKiekis(), preke.getKiekis()))
            return true;
        return false;

    }

    private boolean pakeistasGyvunas(ZurnaloGyvunasGui dto, List<ZurnaloGyvunasDto> prekes) {
        ZurnaloGyvunasDto preke = rask(prekes, dto.getId());
        if (preke == null)
            return true;
        if (!StringUtils.equals(dto.getNumeris(), preke.getNumeris()))
            return true;
        if (!StringUtils.equals(dto.getAmzius(), preke.getAmzius()))
            return true;
        if (dto.getGyvunoRusisId() != preke.getGyvunoRusisId())
            return true;
        return false;

    }

    public void pridetiPreke() {
        prekesList.add(new ZurnaloVaistasGui());
    }

    public void pridetiGyvuna() {
        gyvunaiList.add(new ZurnaloGyvunasGui());
    }
}

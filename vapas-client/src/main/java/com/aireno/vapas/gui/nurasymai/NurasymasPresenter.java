package com.aireno.vapas.gui.nurasymai;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.LookupDto;
import com.aireno.dto.NurasymasDto;
import com.aireno.dto.NurasymoPrekeDto;
import com.aireno.dto.StringLookupItemDto;
import com.aireno.utils.ADateUtils;
import com.aireno.utils.ANumberUtils;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.*;
import com.aireno.vapas.gui.controls.FilterLookup;
import com.aireno.vapas.gui.tablefields.DecimalFieldDefinition;
import com.aireno.vapas.gui.tablefields.LookupFieldDefinitionCB;
import com.aireno.vapas.gui.tablefields.StringLookupFieldDefinitionCB;
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
import java.util.*;

public class NurasymasPresenter extends EntityPresenterBase<NurasymasDto> implements Initializable, GuiPresenter {
    @FXML
    private Node root;
    @FXML
    private Node bPane;

    @FXML
    private TextField numeris;
    @FXML
    private DateControl data;
    @FXML
    private FilterLookup<LookupDto> imone;
    @FXML
    private TableView<NurasymoPrekeDto> prekes;
    @FXML
    private Button bSaugoti;
    @FXML
    private Button bTvirtinti;
    @FXML
    private Button bPridetiPreke;
    @FXML
    private Button bAtaskaita;

    ObservableList<NurasymoPrekeDto> prekesList;

    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public Node getView() {
        return root;
    }

    public NurasymasService getService() {
        return ApplicationContextProvider.getProvider().getBean(NurasymasService.class);
    }

    @Override
    public boolean init() throws Exception {
        initializing = true;

        try {
            prekesList = FXCollections.observableArrayList();
            data.dateFormatProperty().setValue(new SimpleDateFormat(com.aireno.Constants.DATE_FORMAT));
            data.getInputComponent().selectedDateProperty()
                    .addListener(new ChangeListener<Date>() {
                        @Override
                        public void changed(ObservableValue<? extends Date> observableValue, Date date, Date date2) {
                            update();
                        }
                    });
            imone.setData(getLookupService().
                    sarasas(new LookupService.LookupRequest(com.aireno.Constants.LOOKUP_IMONE)));
            imone.valueProperty().addListener(new ChangeListener<LookupDto>() {
                @Override
                public void changed(ObservableValue<? extends LookupDto> observableValue, LookupDto lookupDto, LookupDto lookupDto2) {
                    update();
                }
            });
            if (id > 0) {
                try {
                    item = getService().gauti(id);
                    this.setText("Gauta");
                    numeris.setText(item.getNumeris());
                    data.setValue(item.getData());
                    imone.setValueId(item.getImoneId());
                    for (NurasymoPrekeDto i : item.getPrekes())
                        prekesList.add(new NurasymoPrekeDto(i));

                } catch (Exception e) {
                    this.setText(e.getLocalizedMessage());
                    return false;
                }
            } else {
                item = new NurasymasDto();
                numeris.setText("");
                data.setValue(Calendar.getInstance().getTime());
                imone.setValueId(0);
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
        NurasymasDto dto = new NurasymasDto();
        dto.setNumeris(numeris.getText());
        dto.setData(data.getValue());
        dto.setImoneId(imone.getValueId());
        dto.setId(getId());
        dto.getPrekes().clear();
        for (NurasymoPrekeDto item : prekesList) {
            if (StringUtils.isNotEmpty(item.getSerija())) {
                dto.getPrekes().add(item);
            }
        }

        item = getService().saugoti(dto);
        initializing = true;
        try {
            prekesList.clear();
            for (NurasymoPrekeDto i : item.getPrekes())
                prekesList.add(new NurasymoPrekeDto(i));
            this.setText("Išsaugota");
        } finally {
            initializing = false;
        }
        update();
    }

    public void tvirtinti(ActionEvent event) {
        try {
            getService().tvirtinti(getId());
            this.setText("Patvirtinta");
            item = getService().gauti(getId());
            update();
        } catch (Exception e) {
            this.setError("Klaida tvirtinant: ", e);
        }
    }

    public void generuotiAtaskaita(ActionEvent event) {
        try {
            getService().generuotiAtaskaita(getId());
            this.setText("Sugeneruota");
        } catch (Exception e) {
            this.setError("Klaida generuojant ataskaita: ", e);
        }
    }

    class MListDefinition extends ListDefinition<NurasymoPrekeDto> {
        MListDefinition() {
            fields.add(new LookupFieldDefinitionCB<NurasymoPrekeDto, LookupDto>("Prekė", 150,
                    new PropertyValueFactory<NurasymoPrekeDto, Long>("prekeId"),
                    new EditFieldDefinition.ChangeEvent<NurasymoPrekeDto, Long>() {

                        @Override
                        public void handle(ChangeEventParam<NurasymoPrekeDto, Long> param) {
                            if (initializing) {
                                return;
                            }
                            if (param.item.getPrekeId() != param.value) {
                                param.item.setPrekeId(param.value);
                                param.item.setSerija(null);
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

            /*fields.add(new StringLookupFieldDefinitionCB("Serija", 100, new PropertyValueFactory<NurasymoPrekeDto, String>("serija"),
                    new EditFieldDefinition.ChangeEvent<NurasymoPrekeDto, String>() {
                        @Override
                        public void handle(ChangeEventParam<NurasymoPrekeDto, String> param) {
                            param.item.setSerija(param.value);
                            update();
                        }
                    },
                    new StringLookupFieldDefinitionCB.DataProvider<NurasymoPrekeDto>() {

                        @Override
                        public List<StringLookupItemDto> getDataList(NurasymoPrekeDto item, String sId) throws Exception {
                            List<StringLookupItemDto> result = getService().sarasasLaisvuPrekiuSeriju(new NurasymasService.LaisvuSerijuRequest(item.getPrekeId(), imone.getValueId()));
                            return result;
                        }
                    }
            ));*/
            fields.add(new DecimalFieldDefinition<NurasymoPrekeDto>("Kiekis", 100,
                    new PropertyValueFactory<NurasymoPrekeDto, BigDecimal>("kiekis"),
                    new EditFieldDefinition.ChangeEvent<NurasymoPrekeDto, BigDecimal>() {
                        @Override
                        public void handle(ChangeEventParam<NurasymoPrekeDto, BigDecimal> param) {
                            param.item.setKiekis(param.value);
                            update();
                        }
                    })
            );
        }
    }

    @Override
    public void updateControls() {
        boolean pakeista = pakeista();
        bSaugoti.setDisable(!pakeista || StringUtils.equals(item.getStatusas(), "Patvirtinta"));
        bTvirtinti.setDisable(pakeista || StringUtils.equals(item.getStatusas(), "Patvirtinta"));
        bPridetiPreke.setDisable(StringUtils.equals(item.getStatusas(), "Patvirtinta"));
        bAtaskaita.setDisable(pakeista || !StringUtils.equals(item.getStatusas(), "Patvirtinta"));
    }

    public String getTitle() {
        return "Nurašymas " + (getId() == 0 ? "naujas" : numeris.getText());
    }

    @Override
    protected boolean pakeista() {

        if (!StringUtils.equals(StringUtils.defaultString(item.getNumeris()), numeris.getText()))
            return true;
        if (item.getImoneId() != imone.getValueId())
            return true;
        if (!ADateUtils.equalDate(item.getData(), data.getValue()))
            return true;
        for (NurasymoPrekeDto dto : prekesList) {
            if (pakeistaPreke(dto, item.getPrekes())) {
                return true;
            }
        }
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
        prekesList.add(new NurasymoPrekeDto());
    }
}

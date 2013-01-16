package com.aireno.vapas.gui.saskaitos;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.LookupDto;
import com.aireno.dto.SaskaitaDto;
import com.aireno.dto.SaskaitosPrekeDto;
import com.aireno.utils.ADateUtils;
import com.aireno.utils.ANumberUtils;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.*;
import com.aireno.vapas.gui.controls.FilterLookup;
import com.aireno.vapas.gui.gydymozurnalas.ZurnaloVaistasGui;
import com.aireno.vapas.gui.tablefields.*;
import com.aireno.vapas.service.LookupService;
import com.aireno.vapas.service.SaskaitaService;
import com.panemu.tiwulfx.form.DateControl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class SaskaitaPresenter extends EntityPresenterBase<SaskaitaDto> {
    @FXML
    private Node root;
    @FXML
    private Node bPane;

    @FXML
    private TextField numeris;
    @FXML
    private DateControl data;
    @FXML
    private FilterLookup<LookupDto> tiekejas;
    @FXML
    private FilterLookup<LookupDto> imone;
    @FXML
    private TableView<SaskaitosPrekeGui> prekes;
    @FXML
    private Button bSaugoti;
   /* @FXML
    private Button bTvirtinti;*/
    @FXML
    private Button bPridetiPreke;

    ObservableList<SaskaitosPrekeGui> prekesList;

    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public Node getView() {
        return root;
    }

    public SaskaitaService getService() {
        return ApplicationContextProvider.getProvider().getBean(SaskaitaService.class);
    }

    @Override
    public boolean init() throws Exception {
        imone.setData(getLookupService().sarasas(
                new LookupService.LookupRequest(com.aireno.Constants.LOOKUP_IMONE)));
        tiekejas.setData(getLookupService().sarasas(
                new LookupService.LookupRequest(com.aireno.Constants.LOOKUP_TIEKEJAS)));
        data.setDateFormat(new SimpleDateFormat(Constants.DATE_FORMAT));
        data.getInputComponent().selectedDateProperty().addListener(new ChangeListener<Date>() {
            @Override
            public void changed(ObservableValue<? extends Date> observableValue, Date date, Date date2) {
                update();
            }
        });
        prekesList = FXCollections.observableArrayList();
        prekesList.addListener(new ListChangeListener<SaskaitosPrekeGui>() {
            @Override
            public void onChanged(Change<? extends SaskaitosPrekeGui> change) {
                update();
            }
        });
        if (id > 0) {
            try {
                SaskaitaDto dto = getService().gauti(id);
                this.setText("Gauta");
                numeris.setText(dto.getNumeris());

                data.setValue(dto.getData());
                tiekejas.setValueId(dto.getTiekejasId());
                imone.setValueId(dto.getImoneId());
                for (SaskaitosPrekeDto i : dto.getPrekes())
                    prekesList.add(new SaskaitosPrekeGui(i));
                item = dto;

            } catch (Exception e) {
                this.setText(e.getLocalizedMessage());
                return false;
            }
        } else {
            numeris.setText("");
            data.setValue(new Date());
            tiekejas.setValueId(0);
            imone.setValueId(0);
            item = new SaskaitaDto();
        }

        MListDefinition def = new MListDefinition();
        prekes.setEditable(true);
        def.InitTable(prekes);
        prekes.setItems(prekesList);

        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private String dateToString(Date data) {
        return new SimpleDateFormat(Constants.DATE_FORMAT).format(data);
    }

    @Override
    public Node getButtonView() {
        return bPane;  //To change body of implemented methods use File | Settings | File Templates.
    }

    DateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT);

    public void saugotiInt() throws Exception {

        SaskaitaDto dto = new SaskaitaDto();
        dto.setNumeris(numeris.getText());
        dto.setData(data.getValue());
        dto.setImoneId(imone.getValueId());
        dto.setTiekejasId(tiekejas.getValueId());

        dto.setId(getId());

        dto.getPrekes().clear();
        for (SaskaitosPrekeGui item : prekesList) {
            if (StringUtils.isNotEmpty(item.getSerija())) {
                dto.getPrekes().add(item.toDto());
            }
        }

        dto = getService().saugoti(dto);
        this.setText("Išsaugota");
        item = dto;
        prekesList.clear();
        for (SaskaitosPrekeDto i : dto.getPrekes()) {
            prekesList.add(new SaskaitosPrekeGui(i));
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

    class MListDefinition extends ListDefinition<SaskaitosPrekeGui> {
        MListDefinition() {

            fields.add(new LookupFieldDefinitionCB<SaskaitosPrekeGui, LookupDto>("Prekė", 250,
                    new PropertyValueFactory<SaskaitosPrekeGui, Long>("prekeId"),
                    new EditFieldDefinition.ChangeEvent<SaskaitosPrekeGui, Long>() {
                        @Override
                        public void handle(ChangeEventParam<SaskaitosPrekeGui, Long> param) {
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
                    return getLookupService().sarasas(new LookupService.LookupRequest(com.aireno.Constants.LOOKUP_PREKE));  //To change body of implemented methods use File | Settings | File Templates.
                }
            }
            ));
            fields.add(new StringLookupFieldDefinitionCB("Serija", 200,
                    new PropertyValueFactory<SaskaitosPrekeGui, String>("serija"),
                    new EditFieldDefinition.ChangeEvent<SaskaitosPrekeGui, String>() {
                        @Override
                        public void handle(ChangeEventParam<SaskaitosPrekeGui, String> param) {
                            param.item.setSerija(param.value);
                            update();
                        }
                    },
                    new StringLookupFieldDefinitionCB.DataProvider<SaskaitosPrekeGui>() {

                        @Override
                        public List<String> getDataList(SaskaitosPrekeGui item, String sId) throws Exception {
                            List<String> result = getService()
                                    .sarasasSerijos(new SaskaitaService.SerijosRequest(item.getPrekeId()));
                            return result;
                        }
                    }
            ));
            fields.add(new DecimalFieldDefinition<SaskaitosPrekeGui>("Kiekis", 100,
                    new PropertyValueFactory<SaskaitosPrekeGui, BigDecimal>("kiekis"),
                    new EditFieldDefinition.ChangeEvent<SaskaitosPrekeGui, BigDecimal>() {
                        @Override
                        public void handle(ChangeEventParam<SaskaitosPrekeGui, BigDecimal> param) {
                            param.item.setKiekis(param.value);
                            update();
                        }
                    })
            );
            fields.add(new DateFieldDefinition<SaskaitosPrekeGui>("GaliojaIki", 120,
                    new PropertyValueFactory<SaskaitosPrekeGui, Date>("galiojaIki"),
                    new EditFieldDefinition.ChangeEvent<SaskaitosPrekeGui, Date>() {
                        @Override
                        public void handle(ChangeEventParam<SaskaitosPrekeGui, Date> param) {
                            param.item.setGaliojaIki(param.value);
                            update();
                        }
                    }));
            fields.add(new DecimalFieldDefinition<SaskaitosPrekeGui>("Kaina", 100,
                    new PropertyValueFactory<SaskaitosPrekeGui, BigDecimal>("kaina"),
                    new EditFieldDefinition.ChangeEvent<SaskaitosPrekeGui, BigDecimal>() {
                        @Override
                        public void handle(ChangeEventParam<SaskaitosPrekeGui, BigDecimal> param) {
                            param.item.setKaina(param.value);
                            update();
                        }
                    }));
        }
    }

    @Override
    public void updateControls() {
        boolean pakeista = pakeista();
        bSaugoti.setDisable(!pakeista);
        //bTvirtinti.setDisable(pakeista || StringUtils.equals(item.getStatusas(), "Patvirtinta"));
        //bPridetiPreke.setDisable(StringUtils.equals(item.getStatusas(), "Patvirtinta"));
    }

    public String getTitle() {
        return "Sąskaita " + (getId() == 0 ? "nauja" : numeris.getText());
    }

    @Override
    protected boolean pakeista() {
        if (!StringUtils.equals(StringUtils.defaultString(item.getNumeris()), numeris.getText()))
            return true;
        if (item.getImoneId() != imone.getValueId())
            return true;
        if (item.getTiekejasId() != tiekejas.getValueId())
            return true;
        if (!ADateUtils.equalDate(item.getData(), data.getValue()))
            return true;
        for (SaskaitosPrekeGui dto : prekesList) {
            if (pakeistaPreke(dto, item.getPrekes())) {
                return true;
            }
        }
        return false;
    }

    private boolean pakeistaPreke(SaskaitosPrekeGui dto, List<SaskaitosPrekeDto> prekes) {
        SaskaitosPrekeDto preke = raskPreke(prekes, dto.getId());
        if (preke == null)
            return true;
        if (!StringUtils.equals(dto.getSerija(), preke.getSerija()))
            return true;
        if (!ADateUtils.equalDate(dto.getGaliojaIki(), preke.getGaliojaIki()))
            return true;
        if (dto.getPrekeId() != preke.getPrekeId())
            return true;
        if (!ANumberUtils.equal(dto.getKaina(), preke.getKaina()))
            return true;
        if (!ANumberUtils.equal(dto.getKiekis(), preke.getKiekis()))
            return true;
        return false;

    }

    private SaskaitosPrekeDto raskPreke(List<SaskaitosPrekeDto> prekes, long id) {
        if (prekes == null)
            return null;
        for (SaskaitosPrekeDto dto : prekes) {
            if (dto.getId() == id) {
                return dto;
            }
        }
        return null;
    }

    public void pridetiPreke() {
        prekesList.add(new SaskaitosPrekeGui());
    }
}

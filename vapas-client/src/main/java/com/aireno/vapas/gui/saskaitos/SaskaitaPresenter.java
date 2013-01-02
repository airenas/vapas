package com.aireno.vapas.gui.saskaitos;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.LookupDto;
import com.aireno.dto.NurasymoPrekeDto;
import com.aireno.dto.SaskaitaDto;
import com.aireno.dto.SaskaitosPrekeDto;
import com.aireno.utils.ADateUtils;
import com.aireno.utils.ANumberUtils;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.*;
import com.aireno.vapas.gui.controls.FilterLookup;
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
    private FilterLookup tiekejas;
    @FXML
    private FilterLookup<LookupDto> imone;
    @FXML
    private TableView<SaskaitosPrekeDto> prekes;
    @FXML
    private Button bSaugoti;
    @FXML
    private Button bTvirtinti;
    @FXML
    private Button bPridetiPreke;

    ObservableList<SaskaitosPrekeDto> prekesList;

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
        prekesList.addListener(new ListChangeListener<SaskaitosPrekeDto>() {
            @Override
            public void onChanged(Change<? extends SaskaitosPrekeDto> change) {
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
                    prekesList.add(new SaskaitosPrekeDto(i));
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
        for (SaskaitosPrekeDto item : prekesList) {
            if (StringUtils.isNotEmpty(item.getSerija())) {
                dto.getPrekes().add(item);
            }
        }

        dto = getService().saugoti(dto);
        this.setText("Išsaugota");
        item = dto;
        prekesList.clear();
        for (SaskaitosPrekeDto i : dto.getPrekes()) {
            prekesList.add(new SaskaitosPrekeDto(i));
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

    class MListDefinition extends ListDefinition<SaskaitosPrekeDto> {
        MListDefinition() {
            fields.add(new TextFieldDefinition("Serija", 100, new PropertyValueFactory<SaskaitosPrekeDto, String>("serija"),
                    new EditFieldDefinition.ChangeEvent<SaskaitosPrekeDto, String>() {
                        @Override
                        public void handle(ChangeEventParam<SaskaitosPrekeDto, String> param) {
                            param.item.setSerija(param.value);
                            update();
                        }
                    })
            );
            fields.add(new LookupFieldDefinitionCB<SaskaitosPrekeDto, LookupDto>("Prekė", 150,
                    new PropertyValueFactory<SaskaitosPrekeDto, Long>("prekeId"),
                    new EditFieldDefinition.ChangeEvent<SaskaitosPrekeDto, Long>() {
                        @Override
                        public void handle(ChangeEventParam<SaskaitosPrekeDto, Long> param) {
                            param.item.setPrekeId(param.value);
                            update();
                        }
                    }
                    , new LookupFieldDefinitionCB.DataProvider<LookupDto>() {

                @Override
                public List<LookupDto> getItems() throws Exception {
                    return getLookupService().sarasas(new LookupService.LookupRequest(com.aireno.Constants.LOOKUP_PREKE));  //To change body of implemented methods use File | Settings | File Templates.
                }
            }
            ));
            fields.add(new DecimalFieldDefinition<SaskaitosPrekeDto>("Kiekis", 100,
                    new PropertyValueFactory<SaskaitosPrekeDto, BigDecimal>("kiekis"),
                    new EditFieldDefinition.ChangeEvent<SaskaitosPrekeDto, BigDecimal>() {
                        @Override
                        public void handle(ChangeEventParam<SaskaitosPrekeDto, BigDecimal> param) {
                            param.item.setKiekis(param.value);
                            update();
                        }
                    })
            );
            fields.add(new DateFieldDefinition<SaskaitosPrekeDto>("GaliojaIki", 120, new PropertyValueFactory<SaskaitosPrekeDto, Date>("galiojaIki"),
                    new EditFieldDefinition.ChangeEvent<SaskaitosPrekeDto, Date>() {
                        @Override
                        public void handle(ChangeEventParam<SaskaitosPrekeDto, Date> param) {
                            param.item.setGaliojaIki(param.value);
                            update();
                        }
                    }));
            fields.add(new DecimalFieldDefinition<SaskaitosPrekeDto>("Kaina", 100,
                    new PropertyValueFactory<SaskaitosPrekeDto, BigDecimal>("kaina"),
                    new EditFieldDefinition.ChangeEvent<SaskaitosPrekeDto, BigDecimal>() {
                        @Override
                        public void handle(ChangeEventParam<SaskaitosPrekeDto, BigDecimal> param) {
                            param.item.setKaina(param.value);
                            update();
                        }
                    }));
        }
    }

    @Override
    public void updateControls() {
        boolean pakeista = pakeista();
        bSaugoti.setDisable(!pakeista || StringUtils.equals(item.getStatusas(), "Patvirtinta"));
        bTvirtinti.setDisable(pakeista || StringUtils.equals(item.getStatusas(), "Patvirtinta"));
        bPridetiPreke.setDisable(StringUtils.equals(item.getStatusas(), "Patvirtinta"));
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
        for (SaskaitosPrekeDto dto : prekesList) {
            if (pakeistaPreke(dto, item.getPrekes())) {
                return true;
            }
        }
        return false;
    }

    private boolean pakeistaPreke(SaskaitosPrekeDto dto, List<SaskaitosPrekeDto> prekes) {
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
        prekesList.add(new SaskaitosPrekeDto());
    }
}

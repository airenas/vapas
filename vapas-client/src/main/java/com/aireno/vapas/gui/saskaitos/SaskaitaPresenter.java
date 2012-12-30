package com.aireno.vapas.gui.saskaitos;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.LookupDto;
import com.aireno.dto.SaskaitaDto;
import com.aireno.dto.SaskaitosPrekeDto;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.*;
import com.aireno.vapas.gui.controls.FilterLookup;
import com.aireno.vapas.service.LookupService;
import com.aireno.vapas.service.SaskaitaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import jfxtras.labs.scene.control.CalendarTextField;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class SaskaitaPresenter extends EntityPresenterBase<SaskaitaDto> {
    @FXML
    private Node root;
    @FXML
    private Node bPane;

    @FXML
    private TextField numeris;
    @FXML
    private CalendarTextField data;
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
        prekesList = FXCollections.observableArrayList();
        if (id > 0) {
            try {
                SaskaitaDto dto = getService().gauti(id);
                this.setText("Gauta");
                numeris.setText(dto.getNumeris());
                Calendar cal = Calendar.getInstance();
                cal.setTime(dto.getData());
                data.setValue(cal);
                tiekejas.setValueId(dto.getTiekejasId());
                imone.setValueId(dto.getImoneId());
                for (SaskaitosPrekeDto i : dto.getPrekes())
                    prekesList.add(i);
                for (int i = 0; i < 2; i++)
                    prekesList.add(new SaskaitosPrekeDto());
                item = dto;

            } catch (Exception e) {
                this.setText(e.getLocalizedMessage());
                return false;
            }
        } else {
            numeris.setText("");
            data.setValue(Calendar.getInstance());
            tiekejas.setValueId(0);
            imone.setValueId(0);
            for (int i = 0; i < 2; i++)
                prekesList.add(new SaskaitosPrekeDto());
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
        try {
            SaskaitaDto dto = new SaskaitaDto();
            dto.setNumeris(numeris.getText());
            dto.setData(data.getValue().getTime());
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
        } catch (Exception e) {
            this.setError("Klaida saugant: ", e);
        }

    }

    public void tvirtinti(ActionEvent event) {
        try {
            getService().tvirtinti(getId());
            this.setText("Patvirtinta");
        } catch (Exception e) {
            this.setError("Klaida tvirtinant: ", e);
        }
    }

    class MListDefinition extends ListDefinition<SaskaitosPrekeDto> {
        MListDefinition() {
            fields.add(new TextFieldDefinition("Serija", 100, new PropertyValueFactory<SaskaitosPrekeDto, String>("serija"),
                    new EditFieldDefinition.ChangeEvent<SaskaitosPrekeDto, String>() {
                        @Override
                        public void handle(SaskaitosPrekeDto item, String value) {
                            item.setSerija(value);
                        }
                    })
                    );
            fields.add(new LongFieldDefinition<SaskaitosPrekeDto>("Prekė", 150, new PropertyValueFactory<SaskaitosPrekeDto, Long>("prekeId"),
                    new EditFieldDefinition.ChangeEvent<SaskaitosPrekeDto, Long>() {
                        @Override
                        public void handle(SaskaitosPrekeDto item, Long value) {
                            item.setPrekeId(value);
                        }
                    }));
            fields.add(new DecimalFieldDefinition<SaskaitosPrekeDto>("Kiekis", 100, new PropertyValueFactory<SaskaitosPrekeDto, BigDecimal>("kiekis"),
                    new EditFieldDefinition.ChangeEvent<SaskaitosPrekeDto, BigDecimal>() {
                        @Override
                        public void handle(SaskaitosPrekeDto item, BigDecimal value) {
                            item.setKiekis(value);
                        }
                    })
                    );
            fields.add(new DateFieldDefinition<SaskaitosPrekeDto>("GaliojaIki", 120, new PropertyValueFactory<SaskaitosPrekeDto, Date>("galiojaIki"),
                    new EditFieldDefinition.ChangeEvent<SaskaitosPrekeDto, Date>() {
                        @Override
                        public void handle(SaskaitosPrekeDto item, Date value) {
                            item.setGaliojaIki(value);
                        }
                    }));
            fields.add(new DecimalFieldDefinition<SaskaitosPrekeDto>("Kaina", 100, new PropertyValueFactory<SaskaitosPrekeDto, BigDecimal>("kaina"),
                    new EditFieldDefinition.ChangeEvent<SaskaitosPrekeDto, BigDecimal>() {
                        @Override
                        public void handle(SaskaitosPrekeDto item, BigDecimal value) {
                            item.setKaina(value);
                        }
                    }));
/*            fields.add(new EditFieldDefinition<SaskaitosPrekeDto, BigDecimal>("Kiekis", 100, new PropertyValueFactory<SaskaitosPrekeDto, BigDecimal>("kiekis"),
            new EventHandler<TableColumn.CellEditEvent<SaskaitosPrekeDto, BigDecimal>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<SaskaitosPrekeDto, BigDecimal> t) {
                    ((SaskaitosPrekeDto) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setKiekis(t.getNewValue());
                }
            }));
            fields.add(new EditFieldDefinition<SaskaitosPrekeDto, Date>("Galioja iki", 100, new PropertyValueFactory<SaskaitosPrekeDto, Date>("galiojaIki"),
            new EventHandler<TableColumn.CellEditEvent<SaskaitosPrekeDto, Date>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<SaskaitosPrekeDto, Date> t) {
                    ((SaskaitosPrekeDto) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setGaliojaIki(t.getNewValue());
                }
            }));
            fields.add(new EditFieldDefinition<SaskaitosPrekeDto, BigDecimal>("Kaina", 100, new PropertyValueFactory<SaskaitosPrekeDto, BigDecimal>("kaina"),
            new EventHandler<TableColumn.CellEditEvent<SaskaitosPrekeDto, BigDecimal>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<SaskaitosPrekeDto, BigDecimal> t) {
                    ((SaskaitosPrekeDto) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setKaina(t.getNewValue());
                }
            }));
            fields.add(new EditFieldDefinition<SaskaitosPrekeDto, BigDecimal>("Suma", 100, new PropertyValueFactory<SaskaitosPrekeDto, BigDecimal>("sumaSuPvm"),
            new EventHandler<TableColumn.CellEditEvent<SaskaitosPrekeDto, BigDecimal>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<SaskaitosPrekeDto, BigDecimal> t) {
                    ((SaskaitosPrekeDto) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setSuma(t.getNewValue());
                }
            }));*/
        }
    }

    @Override
    public void updateControls() {
        boolean pakeista = pakeista();
        bSaugoti.setDisable(!pakeista || StringUtils.equals(item.getStatusas(), "Patvirtinta"));
        bTvirtinti.setDisable(StringUtils.equals(item.getStatusas(), "Patvirtinta"));
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
        return false;
    }
}

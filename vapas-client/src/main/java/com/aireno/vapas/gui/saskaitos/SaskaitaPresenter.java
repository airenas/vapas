package com.aireno.vapas.gui.saskaitos;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.dto.SaskaitaDto;
import com.aireno.dto.SaskaitosPrekeDto;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.*;
import com.aireno.vapas.service.SaskaitaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class SaskaitaPresenter extends PresenterBase implements Initializable, GuiPresenter {
    @FXML
    private Node root;
    @FXML
    private Node bPane;

    @FXML
    private TextField numeris;
    @FXML
    private TextField data;
    @FXML
    private TextField tiekejas;
    @FXML
    private TextField imone;
    @FXML
    private TableView<SaskaitosPrekeDto> prekes;

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
    public boolean init() {
        prekesList = FXCollections.observableArrayList();
        if (id > 0) {
            try {
                SaskaitaDto dto = getService().gauti(id);
                this.setText("Gauta");
                numeris.setText(dto.getNumeris());
                data.setText(dateToString(dto.getData()));
                tiekejas.setText(Long.toString(dto.getTiekejasId()));
                imone.setText(Long.toString(dto.getImoneId()));
                for (SaskaitosPrekeDto i : dto.getPrekes())
                    prekesList.add(i);
                for (int i = 0; i < 10; i++)
                    prekesList.add(new SaskaitosPrekeDto());

            } catch (Exception e) {
                this.setText(e.getLocalizedMessage());
                return false;
            }
        } else {
            numeris.setText("");
            data.setText(dateToString(new Date()));
            tiekejas.setText("1");
            imone.setText("1");
            for (int i = 0; i < 10; i++)
                prekesList.add(new SaskaitosPrekeDto());
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

    public void saugoti(ActionEvent event) throws ParseException {
        try {
            SaskaitaDto dto = new SaskaitaDto();
            dto.setNumeris(numeris.getText());
            dto.setData(formatter.parse(data.getText()));
            dto.setImoneId(Long.valueOf(imone.getText()));
            dto.setTiekejasId(Long.valueOf(tiekejas.getText()));
            dto.setId(id);
            dto.getPrekes().clear();
            for (SaskaitosPrekeDto item: prekesList)
            {
                if (StringUtils.isNotEmpty(item.getSerija()))
                {
                    dto.getPrekes().add(item);
                }
            }

            getService().saugoti(dto);
            this.setText("Išsaugota");
        } catch (Exception e) {
            this.setError("Klaida saugant: ", e);
        }

    }

    public void iseiti(ActionEvent event) {
        this.goBack();
    }

    public void tvirtinti(ActionEvent event) {
        try {
            getService().tvirtinti(id);
            this.setText("Patvirtinta");
        } catch (Exception e) {
            this.setError("Klaida tvirtinant: ", e);
        }
    }

    class MListDefinition extends ListDefinition<SaskaitosPrekeDto> {
        MListDefinition() {
            fields.add(new TextFieldDefinition("Serija", 100, new PropertyValueFactory<SaskaitosPrekeDto, String>("serija"),
                    new EventHandler<TableColumn.CellEditEvent<SaskaitosPrekeDto, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<SaskaitosPrekeDto, String> t) {
                            ((SaskaitosPrekeDto) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setSerija(t.getNewValue());
                        }
                    }));
            fields.add(new LongFieldDefinition<SaskaitosPrekeDto>("Prekė", 50, new PropertyValueFactory<SaskaitosPrekeDto, Long>("prekeId"),
                    new EventHandler<TableColumn.CellEditEvent<SaskaitosPrekeDto, Long>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<SaskaitosPrekeDto, Long> t) {
                            ((SaskaitosPrekeDto) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setPrekeId(t.getNewValue());
                        }
                    }));
            fields.add(new DecimalFieldDefinition<SaskaitosPrekeDto>("Kiekis", 100, new PropertyValueFactory<SaskaitosPrekeDto, BigDecimal>("kiekis"),
                    new EventHandler<TableColumn.CellEditEvent<SaskaitosPrekeDto, BigDecimal>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<SaskaitosPrekeDto, BigDecimal> t) {
                            ((SaskaitosPrekeDto) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setKiekis(t.getNewValue());
                        }
                    }));
            fields.add(new DateFieldDefinition<SaskaitosPrekeDto>("GaliojaIki", 100, new PropertyValueFactory<SaskaitosPrekeDto, Date>("galiojaIki"),
                    new EventHandler<TableColumn.CellEditEvent<SaskaitosPrekeDto, Date>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<SaskaitosPrekeDto, Date> t) {
                            ((SaskaitosPrekeDto) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setGaliojaIki(t.getNewValue());
                        }
                    }));
            fields.add(new DecimalFieldDefinition<SaskaitosPrekeDto>("Kaina", 100, new PropertyValueFactory<SaskaitosPrekeDto, BigDecimal>("kaina"),
                    new EventHandler<TableColumn.CellEditEvent<SaskaitosPrekeDto, BigDecimal>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<SaskaitosPrekeDto, BigDecimal> t) {
                            ((SaskaitosPrekeDto) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setKaina(t.getNewValue());
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

    public String getTitle() {
        return "Sąskaita";
    }


//    public void trytable() {
//        prekes.setEditable(true);
//        Callback<TableColumn, TableCell> cellFactory =
//                new Callback<TableColumn, TableCell>() {
//                    public TableCell call(TableColumn p) {
//                        return new EditingCell();
//                    }
//                };
//
//        TableColumn firstNameCol = new TableColumn("Serija");
//        firstNameCol.setMinWidth(100);
//        firstNameCol.setCellValueFactory(
//                new PropertyValueFactory<SaskaitosPrekeDto, String>("serija"));
//        firstNameCol.setCellFactory(cellFactory);
//        firstNameCol.setOnEditCommit(
//                new EventHandler<TableColumn.CellEditEvent<SaskaitosPrekeDto, String>>() {
//                    @Override
//                    public void handle(TableColumn.CellEditEvent<SaskaitosPrekeDto, String> t) {
//                        ((SaskaitosPrekeDto) t.getTableView().getItems().get(
//                                t.getTablePosition().getRow())
//                        ).setSerija(t.getNewValue());
//                    }
//                }
//        );
//
//        SaskaitosPrekeDto dto = new SaskaitosPrekeDto();
//        dto.setSerija("olia");
//        prekesList =
//                FXCollections.observableArrayList(
//                        dto,
//                        new SaskaitosPrekeDto());
//
//        prekes.setItems(prekesList);
//        prekes.getColumns().addAll(firstNameCol);
//    }


}

package com.aireno.vapas.gui.nurasymai;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.dto.NurasymoPrekeDto;
import com.aireno.dto.NurasymasDto;
import com.aireno.dto.NurasymoPrekeDto;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.*;
import com.aireno.vapas.service.NurasymasService;
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

public class NurasymasPresenter extends PresenterBase implements Initializable, GuiPresenter {
    @FXML
    private Node root;
    @FXML
    private Node bPane;

    @FXML
    private TextField numeris;
    @FXML
    private TextField data;
    @FXML
    private TextField imone;
    @FXML
    private TableView<NurasymoPrekeDto> prekes;

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
    public boolean init() {
        prekesList = FXCollections.observableArrayList();
        if (id > 0) {
            try {
                NurasymasDto dto = getService().gauti(id);
                this.setText("Gauta");
                numeris.setText(dto.getNumeris());
                data.setText(dateToString(dto.getData()));
                imone.setText(Long.toString(dto.getImoneId()));
                for (NurasymoPrekeDto i : dto.getPrekes())
                    prekesList.add(i);
                for (int i = 0; i < 10; i++)
                    prekesList.add(new NurasymoPrekeDto());

            } catch (Exception e) {
                this.setText(e.getLocalizedMessage());
                return false;
            }
        } else {
            numeris.setText("");
            data.setText(dateToString(new Date()));
            imone.setText("1");
            for (int i = 0; i < 10; i++)
                prekesList.add(new NurasymoPrekeDto());
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
            NurasymasDto dto = new NurasymasDto();
            dto.setNumeris(numeris.getText());
            dto.setData(formatter.parse(data.getText()));
            dto.setImoneId(Long.valueOf(imone.getText()));
            dto.setId(id);
            dto.getPrekes().clear();
            for (NurasymoPrekeDto item: prekesList)
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

    class MListDefinition extends ListDefinition<NurasymoPrekeDto> {
        MListDefinition() {
            fields.add(new LongFieldDefinition<NurasymoPrekeDto>("Prekė", 50, new PropertyValueFactory<NurasymoPrekeDto, Long>("prekeId"),
                    new EventHandler<TableColumn.CellEditEvent<NurasymoPrekeDto, Long>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<NurasymoPrekeDto, Long> t) {
                            ((NurasymoPrekeDto) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setPrekeId(t.getNewValue());
                        }
                    }));

            fields.add(new TextFieldDefinition("Serija", 100, new PropertyValueFactory<NurasymoPrekeDto, String>("serija"),
                    new EventHandler<TableColumn.CellEditEvent<NurasymoPrekeDto, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<NurasymoPrekeDto, String> t) {
                            ((NurasymoPrekeDto) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setSerija(t.getNewValue());
                        }
                    }));
            fields.add(new DecimalFieldDefinition<NurasymoPrekeDto>("Kiekis", 100, new PropertyValueFactory<NurasymoPrekeDto, BigDecimal>("kiekis"),
                    new EventHandler<TableColumn.CellEditEvent<NurasymoPrekeDto, BigDecimal>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<NurasymoPrekeDto, BigDecimal> t) {
                            ((NurasymoPrekeDto) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setKiekis(t.getNewValue());
                        }
                    }));
        }
    }

    public String getTitle() {
        return "Nurašymas";
    }
}

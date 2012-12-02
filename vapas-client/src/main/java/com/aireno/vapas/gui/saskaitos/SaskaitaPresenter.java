package com.aireno.vapas.gui.saskaitos;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.dto.SaskaitaDto;
import com.aireno.dto.SaskaitaListDto;
import com.aireno.dto.SaskaitosPrekeDto;
import com.aireno.vapas.gui.base.*;
import com.aireno.vapas.service.SaskaitaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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
                data.setText(dto.getData().toString());
                tiekejas.setText(Long.toString(dto.getTiekejasId()));
                imone.setText(Long.toString(dto.getImoneId()));
                for (SaskaitosPrekeDto i: dto.getPrekes())
                    prekesList.add(i);
                for (int i = 0; i < 10; i++)
                    prekesList.add(new SaskaitosPrekeDto());

            } catch (Exception e) {
                this.setText(e.getLocalizedMessage());
                return false;
            }
        } else {
            numeris.setText("");
            data.setText(formatter.format(new Date()));
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

    @Override
    public Node getButtonView() {
        return bPane;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void search(ActionEvent event) {

    }

    DateFormat formatter = new SimpleDateFormat("dd-MM-yy");

    public void saugoti(ActionEvent event) throws ParseException {
        try {
            SaskaitaDto dto = new SaskaitaDto();
            dto.setNumeris(numeris.getText());
            dto.setData(formatter.parse(data.getText()));
            dto.setImoneId(Long.valueOf(imone.getText()));
            dto.setTiekejasId(Long.valueOf(tiekejas.getText()));
            dto.setId(id);

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
        this.goBack();
    }

    class MListDefinition extends ListDefinition<SaskaitosPrekeDto> {
        MListDefinition() {
            fields.add(new EditFieldDefinition<SaskaitosPrekeDto, String>("Serija", 100, new PropertyValueFactory<SaskaitosPrekeDto, String>("serija")));
            fields.add(new EditFieldDefinition<SaskaitosPrekeDto, String>("PrekeId", 200, new PropertyValueFactory<SaskaitosPrekeDto, String>("prekeId")));
            fields.add(new EditFieldDefinition<SaskaitosPrekeDto, String>("Kiekis", 200, new PropertyValueFactory<SaskaitosPrekeDto, String>("kiekis")));
            fields.add(new EditFieldDefinition<SaskaitosPrekeDto, Date>("Galioja iki", 50, new PropertyValueFactory<SaskaitosPrekeDto, Date>("galiojaIki")));
            fields.add(new EditFieldDefinition<SaskaitosPrekeDto, BigDecimal>("Kaina", 50, new PropertyValueFactory<SaskaitosPrekeDto, BigDecimal>("kaina")));
            fields.add(new EditFieldDefinition<SaskaitosPrekeDto, String>("Suma", 50, new PropertyValueFactory<SaskaitosPrekeDto, String>("sumaSuPvm")));
        }
    }

    public String getTitle() {
        return "Sąskaita";
    }
}

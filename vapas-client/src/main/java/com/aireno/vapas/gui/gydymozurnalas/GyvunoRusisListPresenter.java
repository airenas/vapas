package com.aireno.vapas.gui.gydymozurnalas;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.LookupDto;
import com.aireno.dto.NurasymasListDto;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.*;
import com.aireno.vapas.service.GydymoZurnalasService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GyvunoRusisListPresenter extends PresenterBase implements Initializable, GuiPresenter {
    @FXML
    private Node root;
    @FXML
    private Node bPane;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<LookupDto> resultsList;


    ObservableList<LookupDto> data;

    public GydymoZurnalasService getService() {
        return ApplicationContextProvider.getProvider().getBean(GydymoZurnalasService.class);
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public Node getView() {
        return root;
    }

    @Override
    public boolean init() throws GuiException {
        MListDefinition def = new MListDefinition(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                    redaguoti(null);
                }
            }
        });
        def.InitTable(resultsList);
        data = FXCollections.observableArrayList();
        resultsList.setItems(data);
        search(null);
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getTitle() {
        return "Gyvūnų rūšys";
    }

    @Override
    public Node getButtonView() {
        return bPane;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void search(ActionEvent event) throws GuiException {
        try {
            List<LookupDto> items = getService().gyvunoRusys(null);
            data.clear();
            for (LookupDto v : items) {
                data.add(v);
            }
        } catch (Exception e) {
            throw new GuiException("Klaida gaunant sąskaitas: ", e);
        }
    }

    public void naujas(ActionEvent event) {
        show(Constants.GYVUNORUSIS_PRESENTER, 0);
    }

    public void redaguoti(ActionEvent event) {
        LookupDto item = resultsList.getSelectionModel().getSelectedItem();
        if (item == null) {
            this.setText("Parinkite įrašą");
            return;
        }
        show(Constants.GYVUNORUSIS_PRESENTER, item.getId());
    }

    public void trinti(ActionEvent event) {

    }

    class MListDefinition extends ListDefinition<LookupDto> {
        MListDefinition(EventHandler onClick) {
            fields.add(new FieldDefinition<LookupDto, String>("Rušis", 200, new PropertyValueFactory<LookupDto, String>("pavadinimas"))
                    .addCellFactory(new GenericCellFactory<LookupDto, String>(onClick)));
        }
    }
}

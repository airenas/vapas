package com.aireno.vapas.gui.gydymozurnalas;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.dto.GydomuGyvunuZurnalasListDto;
import com.aireno.dto.PrekeListDto;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.*;
import com.aireno.vapas.service.GydomuGyvunuZurnalasService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.util.Date;
import java.util.List;

public class GydymoZurnalasListPresenter extends PresenterBase {
    @FXML
    private Node root;
    @FXML
    private Node bPane;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<GydomuGyvunuZurnalasListDto> resultsList;

    ObservableList<GydomuGyvunuZurnalasListDto> data;

    public GydomuGyvunuZurnalasService getService() {
        return ApplicationContextProvider.getProvider().getBean(GydomuGyvunuZurnalasService.class);
    }

    public Node getView() {
        return root;
    }

    @Override
    protected void initInternal() throws Exception {
        data = FXCollections.observableArrayList();
        resultsList.setItems(data);
        search(null);
    }

    @Override
    protected void initOnce() throws Exception {
        MListDefinition def = new MListDefinition(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                    redaguoti(null);
                }
            }
        });
        def.InitTable(resultsList);
    }

    public String getTitle() {
        return "Gydomų gyvūnų žurnalas";
    }

    @Override
    public Node getButtonView() {
        return bPane;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void search(ActionEvent event) throws GuiException {
        try {
            List<GydomuGyvunuZurnalasListDto> items = getService().sarasas(new GydomuGyvunuZurnalasService.SarasasRequest(false));
            data.clear();
            for (GydomuGyvunuZurnalasListDto v : items) {
                data.add(v);
            }
        } catch (Exception e) {
            throw new GuiException("Klaida gaunant žurnalą: ", e);
        }
    }

    public void naujas(ActionEvent event) {
        show(Constants.GYDYMOZURNALAS_PRESENTER, 0);
    }

    public void redaguoti(ActionEvent event) {
        GydomuGyvunuZurnalasListDto item = resultsList.getSelectionModel().getSelectedItem();
        if (item == null) {
            this.setText("Parinkite įrašą");
            return;
        }
        show(Constants.GYDYMOZURNALAS_PRESENTER, item.getId());
    }

    public void trinti(ActionEvent event) {

    }

    class MListDefinition extends ListDefinition<GydomuGyvunuZurnalasListDto> {
        MListDefinition(EventHandler onClick) {
            fields.add(new FieldDefinition<GydomuGyvunuZurnalasListDto, String>("Data", 200,
                    new PropertyValueFactory<GydomuGyvunuZurnalasListDto, String>("registracijosData"))
                    .addCellFactory(new GenericCellFactory<GydomuGyvunuZurnalasListDto, String>(onClick)));
            fields.add(new FieldDefinition<GydomuGyvunuZurnalasListDto, String>("Laikytojas", 200, new PropertyValueFactory<GydomuGyvunuZurnalasListDto, String>("laikytojas"))
                    .addCellFactory(new GenericCellFactory<GydomuGyvunuZurnalasListDto, String>(onClick)));
            fields.add(new FieldDefinition<GydomuGyvunuZurnalasListDto, String>("Gyvūnai", 200, new PropertyValueFactory<GydomuGyvunuZurnalasListDto, String>("gyvunuSarasas"))
                    .addCellFactory(new GenericCellFactory<GydomuGyvunuZurnalasListDto, String>(onClick)));
            fields.add(new FieldDefinition<GydomuGyvunuZurnalasListDto, String>("Vaistai", 200, new PropertyValueFactory<GydomuGyvunuZurnalasListDto, String>("vaistai"))
                    .addCellFactory(new GenericCellFactory<GydomuGyvunuZurnalasListDto, String>(onClick)));
            fields.add(new FieldDefinition<GydomuGyvunuZurnalasListDto, String>("Įmonė", 100,
                    new PropertyValueFactory<GydomuGyvunuZurnalasListDto, String>("imone"))
                    .addCellFactory(new GenericCellFactory<GydomuGyvunuZurnalasListDto, String>(onClick)));
            fields.add(new FieldDefinition<GydomuGyvunuZurnalasListDto, String>("Išlauka pien.", 100,
                    new PropertyValueFactory<GydomuGyvunuZurnalasListDto, String>("islaukaPienui"))
                    .addCellFactory(new GenericCellFactory<GydomuGyvunuZurnalasListDto, String>(onClick)));
            fields.add(new FieldDefinition<GydomuGyvunuZurnalasListDto, String>("Išlauka mėsai", 100,
                    new PropertyValueFactory<GydomuGyvunuZurnalasListDto, String>("islaukaMesai"))
                    .addCellFactory(new GenericCellFactory<GydomuGyvunuZurnalasListDto, String>(onClick)));
        }
    }
}

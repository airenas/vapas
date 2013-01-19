package com.aireno.vapas.gui.nurasymai;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.dto.NurasymasListDto;
import com.aireno.dto.SaskaitaListDto;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class NurasymasListPresenter extends PresenterBase implements Initializable, GuiPresenter {
    @FXML
    private Node root;
    @FXML
    private Node bPane;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<NurasymasListDto> resultsList;


    ObservableList<NurasymasListDto> data;

    public NurasymasService getService() {
        return ApplicationContextProvider.getProvider().getBean(NurasymasService.class);
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
        return "Nurašymai";
    }

    @Override
    public Node getButtonView() {
        return bPane;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void search(ActionEvent event) throws GuiException {
        try {
            List<NurasymasListDto> items = getService().sarasas(null);
            data.clear();
            for (NurasymasListDto v : items) {
                data.add(v);
            }
        } catch (Exception e) {
            throw new GuiException("Klaida gaunant sąskaitas: ", e);
        }
    }

    public void naujas(ActionEvent event) {
        show(Constants.NURASYMAS_PRESENTER, 0);
    }

    public void redaguoti(ActionEvent event) {
        NurasymasListDto item = resultsList.getSelectionModel().getSelectedItem();
        if (item == null) {
            this.setText("Parinkite įrašą");
            return;
        }
        show(Constants.NURASYMAS_PRESENTER, item.getId());
    }

    public void trinti(ActionEvent event) {

    }

    class MListDefinition extends ListDefinition<NurasymasListDto> {
        MListDefinition(EventHandler onClick) {
            fields.add(new FieldDefinition<NurasymasListDto, String>("Nr", 200, new PropertyValueFactory<NurasymasListDto, String>("numeris"))
                    .addCellFactory(new GenericCellFactory<NurasymasListDto, String>(onClick)));
            fields.add(new FieldDefinition<NurasymasListDto, String>("Įmonė", 200, new PropertyValueFactory<NurasymasListDto, String>("imone"))
                    .addCellFactory(new GenericCellFactory<NurasymasListDto, String>(onClick)));
            fields.add(new FieldDefinition<NurasymasListDto, String>("Data", 100,
                    new PropertyValueFactory<NurasymasListDto, String>("data"))
                    .addCellFactory(new GenericCellFactory<NurasymasListDto, String>(onClick)));
            /*fields.add(new FieldDefinition<NurasymasListDto, String>("Statusas", 70, new PropertyValueFactory<NurasymasListDto, String>("statusas"))
                    .addCellFactory(new GenericCellFactory<NurasymasListDto, String>(onClick)));*/
        }
    }
}

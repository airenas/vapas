package com.aireno.vapas.gui.prekes;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.dto.PrekeListDto;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.*;
import com.aireno.vapas.service.PrekeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PrekesListPresenter extends PresenterBase implements Initializable, GuiPresenter {
    @FXML
    private Node root;
    @FXML
    private Node bPane;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<PrekeListDto> resultsList;
    @FXML
    private Button bTrinti;

    ObservableList<PrekeListDto> data;

    public PrekeService getPrekeService() {
        return ApplicationContextProvider.getProvider().getBean(PrekeService.class);
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public Node getView() {
        return root;
    }

    @Override
    public boolean init() throws Exception {
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

    @Override
    public Node getButtonView() {
        return bPane;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void search(ActionEvent event) throws Exception {
        List<PrekeListDto> result = getPrekeService().sarasas(null);
        data.clear();
        for (PrekeListDto v : result) {
            data.add(v);
        }
    }

    public void naujas(ActionEvent event) {
        show(Constants.PREKES_PRESENTER, 0);
    }

    public void redaguoti(ActionEvent event) {
        PrekeListDto item = resultsList.getSelectionModel().getSelectedItem();
        if (item == null) {
            this.setText("Parinkite įrašą");
            return;
        }
        show(Constants.PREKES_PRESENTER, item.getId());
    }

    public void trinti(ActionEvent event) {

    }

    @Override
    public void updateControls() {
        bTrinti.setDisable(true);
    }

    public String getTitle() {
        return "Prekės";
    }

    class MListDefinition extends ListDefinition<PrekeListDto> {
        MListDefinition(EventHandler onClick) {
            fields.add(new FieldDefinition<PrekeListDto,
                    String>("Pavadinimas", 200, new PropertyValueFactory<PrekeListDto, String>("pavadinimas"))
                    .addCellFactory(new GenericCellFactory<PrekeListDto, String>(onClick)));

            fields.add(new FieldDefinition<PrekeListDto, String>("Mat. vienetas", 200, new PropertyValueFactory<PrekeListDto, String>("matVienetas"))
                    .addCellFactory(new GenericCellFactory<PrekeListDto, String>(onClick)));
            //fields.add(new FieldDefinition<PrekeListDto, Long>("Id", 50, new PropertyValueFactory<PrekeListDto,Long>("id")));
        }
    }
}

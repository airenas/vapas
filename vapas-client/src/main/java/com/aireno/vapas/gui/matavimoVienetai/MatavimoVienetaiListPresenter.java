package com.aireno.vapas.gui.matavimoVienetai;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.Processor;
import com.aireno.dto.MatavimoVienetaiListReq;
import com.aireno.dto.MatavimoVienetaiListResp;
import com.aireno.dto.MatavimoVienetasDto;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.*;
import com.aireno.vapas.service.MatavimoVienetaiService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
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

public class MatavimoVienetaiListPresenter extends PresenterBase {
    @FXML
    private Node root;
    @FXML
    private Node bPane;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<MatavimoVienetasDto> resultsList;
    @FXML
    private Button bTrinti;


    ObservableList<MatavimoVienetasDto> data;

    public MatavimoVienetaiService getMatavimoVienetaiService() {
        return ApplicationContextProvider.getProvider().getBean(MatavimoVienetaiService.class);
    }

    public Node getView() {
        return root;
    }

    @Override
    public boolean init() {
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
        updateControls();
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateControls() {
        bTrinti.setDisable(true);
    }

    public String getTitle() {
        return "Matavimo vienetai";
    }

    @Override
    public Node getButtonView() {
        return bPane;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void search(ActionEvent event) {
        String searchPhrase = null;
        final String[] keywords = searchPhrase != null ? searchPhrase.split("\\s+") : null;
        final Task<List<MatavimoVienetasDto>> searchTask = new Task<List<MatavimoVienetasDto>>() {
            protected List<MatavimoVienetasDto> call() throws Exception {
                Processor<MatavimoVienetaiListReq, MatavimoVienetaiListResp> p;
                p = getProcessor(MatavimoVienetaiListReq.class);
                MatavimoVienetaiListResp resp = p.process(new MatavimoVienetaiListReq());
                return resp.result;
            }
        };

        searchTask.stateProperty().addListener(new ChangeListener<Worker.State>() {
            public void changed(ObservableValue<? extends Worker.State> source, Worker.State oldState, Worker.State newState) {
                if (newState.equals(Worker.State.SUCCEEDED)) {
                    data.clear();
                    for (MatavimoVienetasDto v : searchTask.getValue()) {
                        data.add(v);
                    }
                    //resultsList.setItems(searchTask.getValue());
                } else if (newState.equals(Worker.State.FAILED)) {
                    searchTask.getException().printStackTrace();
                }
            }
        });

        new Thread(searchTask).start();
    }

    public void naujas(ActionEvent event) {
        show(Constants.MATAVIMOVIENETAI_PRESENTER, 0);
    }

    public void redaguoti(ActionEvent event) {
        MatavimoVienetasDto item = resultsList.getSelectionModel().getSelectedItem();
        if (item == null) {
            this.setText("Parinkite įrašą");
            return;
        }
        show(Constants.MATAVIMOVIENETAI_PRESENTER, item.getId());
    }

    public void trinti(ActionEvent event) {

    }

    class MListDefinition extends ListDefinition<MatavimoVienetasDto> {
        MListDefinition(EventHandler onClick) {
            fields.add(new FieldDefinition<MatavimoVienetasDto, String>("Kodas", 200, new PropertyValueFactory<MatavimoVienetasDto, String>("kodas"))
                    .addCellFactory(new GenericCellFactory<MatavimoVienetasDto, String>(onClick)));
            fields.add(new FieldDefinition<MatavimoVienetasDto, String>("Pavadinimas", 200,
                    new PropertyValueFactory<MatavimoVienetasDto, String>("pavadinimas"))
                    .addCellFactory(new GenericCellFactory<MatavimoVienetasDto, String>(onClick)));
            //fields.add(new FieldDefinition<MatavimoVienetasDto, Long>("Id", 50, new PropertyValueFactory<MatavimoVienetasDto,Long>("id")));
        }
    }
}

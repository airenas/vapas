package com.aireno.vapas.gui.prekes;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.Processor;
import com.aireno.dto.MatavimoVienetaiListReq;
import com.aireno.dto.MatavimoVienetaiListResp;
import com.aireno.dto.MatavimoVienetasDto;
import com.aireno.dto.PrekeListDto;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.FieldDefinition;
import com.aireno.vapas.gui.base.GuiPresenter;
import com.aireno.vapas.gui.base.ListDefinition;
import com.aireno.vapas.gui.base.PresenterBase;
import com.aireno.vapas.service.MatavimoVienetaiService;
import com.aireno.vapas.service.PrekeService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PrekesListPresenter extends PresenterBase implements Initializable, GuiPresenter
{
    @FXML private Node root;
    @FXML private Node bPane;
    @FXML private TextField searchField;
    @FXML private TableView<PrekeListDto> resultsList;

    ObservableList<PrekeListDto> data;

    public PrekeService getPrekeService() {
        return ApplicationContextProvider.getProvider().getBean(PrekeService.class);
    }

    public void initialize(URL url, ResourceBundle resourceBundle)
    {
    }

    public Node getView()
    {
        return root;
    }

    @Override
    public boolean init() {
        MListDefinition def = new MListDefinition();
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

    public void search(ActionEvent event)
    {
        String searchPhrase = null;
        final String[] keywords = searchPhrase != null ? searchPhrase.split("\\s+") : null;
        final Task<List<PrekeListDto>> searchTask = new Task<List<PrekeListDto>>()
        {
            protected List<PrekeListDto> call() throws Exception
            {
                return getPrekeService().sarasas(keywords);
            }
        };

        searchTask.stateProperty().addListener(new ChangeListener<Worker.State>()
        {
            public void changed(ObservableValue<? extends Worker.State> source, Worker.State oldState, Worker.State newState)
            {
                if (newState.equals(Worker.State.SUCCEEDED))
                {
                    data.clear();
                    for (PrekeListDto v: searchTask.getValue())
                    {
                        data.add(v);
                    }
                    //resultsList.setItems(searchTask.getValue());
                }
                else if (newState.equals(Worker.State.FAILED))
                {
                    searchTask.getException().printStackTrace();
                }
            }
        });

        new Thread(searchTask).start();
    }

    public void naujas(ActionEvent event)
    {
        show(Constants.PREKES_PRESENTER, 0);
    }

    public void redaguoti(ActionEvent event)
    {
        PrekeListDto item = resultsList.getSelectionModel().getSelectedItem();
        if (item == null){
            this.setText("Parinkite įrašą");
            return;
        }
        show(Constants.PREKES_PRESENTER, item.getId());
    }

    public void trinti(ActionEvent event)
    {

    }

   class MListDefinition extends ListDefinition<PrekeListDto>
   {
       MListDefinition() {
           fields.add(new FieldDefinition<PrekeListDto, String>("Pavadinimas", 200, new PropertyValueFactory<PrekeListDto,String>("pavadinimas")));

           fields.add(new FieldDefinition<PrekeListDto, String>("Mat. vienetas", 200, new PropertyValueFactory<PrekeListDto,String>("matVienetas")));
           fields.add(new FieldDefinition<PrekeListDto, Long>("Id", 50, new PropertyValueFactory<PrekeListDto,Long>("id")));
       }
   }
}

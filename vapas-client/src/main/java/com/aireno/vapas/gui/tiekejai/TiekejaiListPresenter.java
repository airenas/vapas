package com.aireno.vapas.gui.tiekejai;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.Processor;
import com.aireno.dto.MatavimoVienetaiListReq;
import com.aireno.dto.MatavimoVienetaiListResp;
import com.aireno.dto.MatavimoVienetasDto;
import com.aireno.dto.TiekejasDto;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.FieldDefinition;
import com.aireno.vapas.gui.base.GuiPresenter;
import com.aireno.vapas.gui.base.ListDefinition;
import com.aireno.vapas.gui.base.PresenterBase;
import com.aireno.vapas.service.MatavimoVienetaiService;
import com.aireno.vapas.service.TiekejasService;
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

public class TiekejaiListPresenter extends PresenterBase implements Initializable, GuiPresenter
{
    @FXML private Node root;
    @FXML private Node bPane;
    @FXML private TextField searchField;
    @FXML private TableView<TiekejasDto> resultsList;


    ObservableList<TiekejasDto> data;

    public TiekejasService getTiekejasService() {
        return ApplicationContextProvider.getProvider().getBean(TiekejasService.class);
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
        try {
            List<TiekejasDto>items = getTiekejasService().sarasas(null);
            data.clear();
            for (TiekejasDto v: items)
            {
                data.add(v);
            }
        } catch (Exception e) {
            this.setError("Klaida gaunant tiekėjus: ", e);
        }
    }

    public void naujas(ActionEvent event)
    {
        show(Constants.TIEKEJAI_PRESENTER, 0);
    }

    public void redaguoti(ActionEvent event)
    {
        TiekejasDto item = resultsList.getSelectionModel().getSelectedItem();
        if (item == null){
            this.setText("Parinkite įrašą");
            return;
        }
        show(Constants.TIEKEJAI_PRESENTER, item.getId());
    }

    public void trinti(ActionEvent event)
    {

    }

   class MListDefinition extends ListDefinition<TiekejasDto>
   {
       MListDefinition() {
           fields.add(new FieldDefinition<TiekejasDto, String>("Pavadinimas", 200, new PropertyValueFactory<TiekejasDto,String>("pavadinimas")));
           fields.add(new FieldDefinition<TiekejasDto, String>("Adresas", 200, new PropertyValueFactory<TiekejasDto,String>("adresas")));
           fields.add(new FieldDefinition<TiekejasDto, String>("Telefonas", 200, new PropertyValueFactory<TiekejasDto,String>("telefonas")));
           fields.add(new FieldDefinition<TiekejasDto, Long>("Id", 50, new PropertyValueFactory<TiekejasDto,Long>("id")));
       }
   }
}

package com.aireno.vapas.gui.imones;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.dto.ImoneDto;
import com.aireno.dto.TiekejasDto;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.*;
import com.aireno.vapas.service.ImoneService;
import com.aireno.vapas.service.TiekejasService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class ImonesListPresenter extends PresenterBase implements Initializable, GuiPresenter
{
    @FXML private Node root;
    @FXML private Node bPane;
    @FXML private TextField searchField;
    @FXML private TableView<ImoneDto> resultsList;


    ObservableList<ImoneDto> data;

    public ImoneService getService() {
        return ApplicationContextProvider.getProvider().getBean(ImoneService.class);
    }

    public void initialize(URL url, ResourceBundle resourceBundle)
    {
    }

    public Node getView()
    {
        return root;
    }

    @Override
    public boolean init() throws GuiException {
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

    public void search(ActionEvent event) throws GuiException {
        try {
            List<ImoneDto>items = getService().sarasas(null);
            data.clear();
            for (ImoneDto v: items)
            {
                data.add(v);
            }
        } catch (Exception e) {
            throw new GuiException("Klaida gaunant įmones: ", e);
        }
    }

    public void naujas(ActionEvent event)
    {
        show(Constants.IMONES_PRESENTER, 0);
    }

    public void redaguoti(ActionEvent event)
    {
        ImoneDto item = resultsList.getSelectionModel().getSelectedItem();
        if (item == null){
            this.setText("Parinkite įrašą");
            return;
        }
        show(Constants.IMONES_PRESENTER, item.getId());
    }

    public void trinti(ActionEvent event)
    {

    }

   class MListDefinition extends ListDefinition<ImoneDto>
   {
       MListDefinition() {
           fields.add(new FieldDefinition<ImoneDto, String>("Pavadinimas", 200, new PropertyValueFactory<ImoneDto,String>("pavadinimas")));
           fields.add(new FieldDefinition<ImoneDto, String>("Adresas", 200, new PropertyValueFactory<ImoneDto,String>("adresas")));
           //fields.add(new FieldDefinition<ImoneDto, String>("Telefonas", 200, new PropertyValueFactory<ImoneDto,String>("telefonas")));
           fields.add(new FieldDefinition<ImoneDto, Long>("Id", 50, new PropertyValueFactory<ImoneDto,Long>("id")));
       }
   }
}

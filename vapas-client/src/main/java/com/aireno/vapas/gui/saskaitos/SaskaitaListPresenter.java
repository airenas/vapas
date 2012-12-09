package com.aireno.vapas.gui.saskaitos;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.dto.ImoneDto;
import com.aireno.dto.SaskaitaListDto;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.*;
import com.aireno.vapas.service.ImoneService;
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
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class SaskaitaListPresenter extends PresenterBase implements Initializable, GuiPresenter
{
    @FXML private Node root;
    @FXML private Node bPane;
    @FXML private TextField searchField;
    @FXML private TableView<SaskaitaListDto> resultsList;


    ObservableList<SaskaitaListDto> data;

    public SaskaitaService getService() {
        return ApplicationContextProvider.getProvider().getBean(SaskaitaService.class);
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

    public String getTitle() {
        return "Sąskaitos";
    }

    @Override
    public Node getButtonView() {
        return bPane;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void search(ActionEvent event) throws GuiException {
        try {
            List<SaskaitaListDto>items = getService().sarasas(null);
            data.clear();
            for (SaskaitaListDto v: items)
            {
                data.add(v);
            }
        } catch (Exception e) {
            throw new GuiException("Klaida gaunant sąskaitas: ", e);
        }
    }

    public void naujas(ActionEvent event)
    {
        show(Constants.SASKAITA_PRESENTER, 0);
    }

    public void redaguoti(ActionEvent event)
    {
        SaskaitaListDto item = resultsList.getSelectionModel().getSelectedItem();
        if (item == null){
            this.setText("Parinkite įrašą");
            return;
        }
        show(Constants.SASKAITA_PRESENTER, item.getId());
    }

    public void trinti(ActionEvent event)
    {

    }

   class MListDefinition extends ListDefinition<SaskaitaListDto>
   {
       MListDefinition() {
           fields.add(new FieldDefinition<SaskaitaListDto, String>("Nr", 200, new PropertyValueFactory<SaskaitaListDto,String>("numeris")));
           fields.add(new FieldDefinition<SaskaitaListDto, String>("Tiekėjas", 200, new PropertyValueFactory<SaskaitaListDto,String>("tiekejas")));
           fields.add(new FieldDefinition<SaskaitaListDto, String>("Įmonė", 200, new PropertyValueFactory<SaskaitaListDto,String>("imone")));
           fields.add(new FieldDefinition<SaskaitaListDto, Date>("Data", 100, new PropertyValueFactory<SaskaitaListDto,Date>("data")));
           fields.add(new FieldDefinition<SaskaitaListDto, BigDecimal>("Suma", 50, new PropertyValueFactory<SaskaitaListDto,BigDecimal>("suma")));
           fields.add(new FieldDefinition<SaskaitaListDto, String>("Statusas", 70, new PropertyValueFactory<SaskaitaListDto,String>("statusas")));
       }
   }
}

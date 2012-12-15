package com.aireno.vapas.gui.likuciai;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.dto.LikutisListDto;
import com.aireno.dto.SaskaitaListDto;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.*;
import com.aireno.vapas.service.LikutisService;
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

public class LikutisListPresenter extends PresenterBase implements Initializable, GuiPresenter
{
    @FXML private Node root;
    @FXML private Node bPane;
    @FXML private TextField searchField;
    @FXML private TableView<LikutisListDto> resultsList;


    ObservableList<LikutisListDto> data;

    public LikutisService getService() {
        return ApplicationContextProvider.getProvider().getBean(LikutisService.class);
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
            List<LikutisListDto>items = getService().sarasas(null);
            data.clear();
            for (LikutisListDto v: items)
            {
                data.add(v);
            }
        } catch (Exception e) {
            throw new GuiException("Klaida gaunant sąskaitas: ", e);
        }
    }

   class MListDefinition extends ListDefinition<LikutisListDto>
   {
       MListDefinition() {
           //fields.add(new FieldDefinition<LikutisListDto, String>("Serija", 200, new PropertyValueFactory<LikutisListDto,String>("serija")));
           fields.add(new FieldDefinition<LikutisListDto, String>("Prekė", 200, new PropertyValueFactory<LikutisListDto,String>("preke")));
           fields.add(new FieldDefinition<LikutisListDto, String>("Įmonė", 200, new PropertyValueFactory<LikutisListDto,String>("imone")));
           fields.add(new FieldDefinition<LikutisListDto, Date>("Data", 100, new PropertyValueFactory<LikutisListDto,Date>("data")));
           fields.add(new FieldDefinition<LikutisListDto, BigDecimal>("Likutis", 50, new PropertyValueFactory<LikutisListDto,BigDecimal>("kiekis")));
           fields.add(new FieldDefinition<LikutisListDto, BigDecimal>("Pajamuota", 50, new PropertyValueFactory<LikutisListDto,BigDecimal>("pajamuota")));
       }
   }
}

package com.aireno.vapas.gui.likuciai;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.dto.LikutisListDto;
import com.aireno.vapas.gui.base.*;
import com.aireno.vapas.service.LikutisService;
import com.aireno.vapas.service.NustatymasService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LikutisListPresenter extends PresenterBase implements Initializable, GuiPresenter {
    public CheckBox cbFilterTeigiamiLikuciai;
    @FXML
    private Node root;
    @FXML
    private Node bPane;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<LikutisListDto> resultsList;


    ObservableList<LikutisListDto> data;

    public LikutisService getService() {
        return ApplicationContextProvider.getProvider().getBean(LikutisService.class);
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public Node getView() {
        return root;
    }

    @Override
    protected void initInternal() throws Exception {
        MListDefinition def = new MListDefinition();
        def.InitTable(resultsList);
        data = FXCollections.observableArrayList();
        resultsList.setItems(data);
        search(null);
    }

    @Override
    protected void initOnce() throws Exception {
        cbFilterTeigiamiLikuciai.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean lookupDto,
                                Boolean lookupDto2) {
                if (!initializing) {
                    runAndExpectError(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getNustatymasService()
                                        .saugotiBoolean(new NustatymasService.NustatymasSaugotiRequest<Boolean>("LIKUTIS_LIST_TIK_TEIGIAMI",
                                                cbFilterTeigiamiLikuciai.isSelected()));

                                search(null);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, "Atidaromi likučiai");
                }
            }
        });
        cbFilterTeigiamiLikuciai.setSelected(getNustatymasService()
                .gautiBoolean(new NustatymasService.NustatymasRequest<Boolean>("LIKUTIS_LIST_TIK_TEIGIAMI", false))
                .result);
    }

    public String getTitle() {
        return "Likučiai";
    }

    @Override
    public Node getButtonView() {
        return bPane;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void search(ActionEvent event) throws GuiException {
        try {
            LikutisService.SarasasRequest request = new LikutisService.SarasasRequest();
            request.arRodytiTikTeigiamus = cbFilterTeigiamiLikuciai.isSelected();
            List<LikutisListDto> items = getService().sarasas(request);
            data.clear();
            for (LikutisListDto v : items) {
                data.add(v);
            }
        } catch (Exception e) {
            throw new GuiException("Klaida gaunant likučius: ", e);
        }
    }

    class MListDefinition extends ListDefinition<LikutisListDto> {
        MListDefinition() {
            //fields.add(new FieldDefinition<LikutisListDto, String>("Serija", 200, new PropertyValueFactory<LikutisListDto,String>("serija")));
            fields.add(new FieldDefinition<LikutisListDto, String>("Prekė", 200, new PropertyValueFactory<LikutisListDto, String>("preke")));
            fields.add(new FieldDefinition<LikutisListDto, String>("Įmonė", 200, new PropertyValueFactory<LikutisListDto, String>("imone")));
            fields.add(new FieldDefinition<LikutisListDto, String>("Data", 100, new PropertyValueFactory<LikutisListDto, String>("data")));
            fields.add(new FieldDefinition<LikutisListDto, BigDecimal>("Likutis", 100, new PropertyValueFactory<LikutisListDto, BigDecimal>("kiekis"))
                    .addCellFactory(new DecimalCellFactory<LikutisListDto>(null)));
            fields.add(new FieldDefinition<LikutisListDto, BigDecimal>("Pajamuota", 100,
                    new PropertyValueFactory<LikutisListDto, BigDecimal>("pajamuota"))
                    .addCellFactory(new DecimalCellFactory<LikutisListDto>(null)));
        }
    }
}

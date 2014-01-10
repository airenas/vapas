package com.aireno.vapas.gui.ataskaitos;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.dto.AtaskaitaListDto;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.*;
import com.aireno.vapas.service.AtaskaitaService;
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

public class AtaskaitaListPresenter extends PresenterBase {
    @FXML
    private Node root;
    @FXML
    private Node bPane;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<AtaskaitaListDto> resultsList;


    ObservableList<AtaskaitaListDto> data;

    public AtaskaitaService getService() {
        return ApplicationContextProvider.getProvider().getBean(AtaskaitaService.class);
    }

    public Node getView() {
        return root;
    }

    @Override
    protected void initInternal() throws Exception {
        search(null);
    }

    @Override
    protected void initOnce() throws Exception {
        MListDefinition def = new MListDefinition(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                    atidaryti();
                }
            }
        });
        def.InitTable(resultsList);
        data = FXCollections.observableArrayList();
        resultsList.setItems(data);
    }


    private void atidaryti() {
        AtaskaitaListDto item = resultsList.getSelectionModel().getSelectedItem();
        if (item == null) {
            this.setText("Parinkite įrašą");
            return;
        }
        try {
            getService().atidaryti(item.getFailas());

        } catch (Exception e) {
            this.setError("Klaida atidarant: ", e);
        }

    }


    public String getTitle() {
        return "Ataskaitos";
    }

    @Override
    public Node getButtonView() {
        return bPane;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void search(ActionEvent event) throws GuiException {
        try {
            List<AtaskaitaListDto> items = getService().sarasas(null);
            data.clear();
            for (AtaskaitaListDto v : items) {
                data.add(v);
            }
        } catch (Exception e) {
            throw new GuiException("Klaida gaunant ataskaitas: ", e);
        }
    }

    public void naujas(ActionEvent event) {
        show(Constants.ATASKAITA_PRESENTER, 0);
    }

    class MListDefinition extends ListDefinition<AtaskaitaListDto> {
        MListDefinition(EventHandler onClick) {
            fields.add(new FieldDefinition<AtaskaitaListDto, String>("Pavadinimas", 300,
                    new PropertyValueFactory<AtaskaitaListDto, String>("pavadinimas"))
                    .addCellFactory(new GenericCellFactory<AtaskaitaListDto, String>(onClick)));
            fields.add(new FieldDefinition<AtaskaitaListDto, Date>("Generavimo data", 200,
                    new PropertyValueFactory<AtaskaitaListDto, Date>("data"))
                    .addCellFactory(new DateTimeCellFactory<AtaskaitaListDto>(onClick)));
        }
    }
}

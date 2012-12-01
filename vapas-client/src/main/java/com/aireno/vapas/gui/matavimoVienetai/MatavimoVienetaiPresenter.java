package com.aireno.vapas.gui.matavimoVienetai;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.Processor;
import com.aireno.dto.*;
import com.aireno.vapas.gui.base.GuiPresenter;
import com.aireno.vapas.gui.base.PresenterBase;
import com.aireno.vapas.service.MatavimoVienetaiService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class MatavimoVienetaiPresenter extends PresenterBase implements Initializable, GuiPresenter
{
    @FXML private Node root;
    @FXML private Node bPane;
    @FXML private TextField kodas;
    @FXML private TextField pavadinimas;

    public MatavimoVienetaiService getMatavimoVienetaiService() {
        return ApplicationContextProvider.getProvider().getBean(MatavimoVienetaiService.class);
    }

    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        /*kodas.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MatavimoVienetas, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<MatavimoVienetas, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return new ObservableValue<String>() {
                    @Override
                    public void addListener(ChangeListener<? super String> changeListener) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void removeListener(ChangeListener<? super String> changeListener) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public String getValue() {
                        return "olia";  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void addListener(InvalidationListener invalidationListener) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void removeListener(InvalidationListener invalidationListener) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                };
            }
        });   */
        // unfortunately FXML does not currently have a clean way to set a custom CellFactory
        // like we need so we have to manually add this here in code
        /*resultsList.setCellFactory(new Callback<ListView<Contact>, ListCell<Contact>>()
        {
            public ListCell<Contact> call(ListView<Contact> contactListView)
            {
                final ListCell<Contact> cell = new ListCell<Contact>()
                {
                    protected void updateItem(Contact contact, boolean empty)
                    {
                        super.updateItem(contact, empty);
                        if (!empty)
                        {
                            setText(String.format("%s %s", contact.getFirstName(), contact.getLastName()));
                        }
                    }
                };
                cell.setOnMouseClicked(new EventHandler<Event>()
                {
                    public void handle(Event event)
                    {
                        Contact contact = cell.getItem();
                        if (contact != null)
                        {
                            contactSelected(contact.getId());
                        }
                    }
                });
                return cell;
            }
        });  */
    }

    public Node getView()
    {
        return root;
    }

    @Override
    public boolean init() {
        if (id > 0){
            Processor<MatavimoVienetasGautiReq, MatavimoVienetasGautiResp> p;
            p = getProcessor(MatavimoVienetasGautiReq.class);
            try {
                MatavimoVienetasGautiResp resp = p.process(new MatavimoVienetasGautiReq(id));
                this.setText("Gauta");
                kodas.setText(resp.result.getKodas());
                pavadinimas.setText(resp.result.getPavadinimas());
            } catch (Exception e) {
                this.setText(e.getLocalizedMessage());
            }
        }
        else
        {
            kodas.setText("");
            pavadinimas.setText("");
        }
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Node getButtonView() {
        return bPane;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void search(ActionEvent event)
    {

    }

    public void saugoti(ActionEvent event)
    {
        MatavimoVienetasDto dto = new MatavimoVienetasDto();
        dto.setPavadinimas(pavadinimas.getText());
        dto.setKodas(kodas.getText());
        dto.setId(id);
        Processor<MatavimoVienetasSaveReq, MatavimoVienetasSaveResp> p;
        p = getProcessor(MatavimoVienetasSaveReq.class);
        try {
            MatavimoVienetasSaveResp resp = p.process(new MatavimoVienetasSaveReq(dto));
            this.setText("Išsaugota");
            id = resp.result.getId();
        } catch (Exception e) {
           this.setText(e.getLocalizedMessage());
        }

    }

    public void iseiti(ActionEvent event)
    {
        this.goBack();
    }
}

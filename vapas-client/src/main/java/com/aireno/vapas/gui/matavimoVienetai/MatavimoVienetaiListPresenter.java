package com.aireno.vapas.gui.matavimoVienetai;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.Processor;
import com.aireno.dto.MatavimoVienetaiListReq;
import com.aireno.dto.MatavimoVienetaiListResp;
import com.aireno.dto.MatavimoVienetasDto;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.*;
import com.aireno.vapas.service.persistance.MatavimoVienetas;
import com.aireno.vapas.service.MatavimoVienetaiService;
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

public class MatavimoVienetaiListPresenter extends PresenterBase implements Initializable, GuiPresenter
{
    @FXML private Node root;
    @FXML private Node bPane;
    @FXML private TextField searchField;
    @FXML private TableView<MatavimoVienetasDto> resultsList;
    //@FXML private TableColumn<MatavimoVienetas, String> kodas;
    //@FXML private TableColumn<MatavimoVienetas, String> pavadinimas;


    ObservableList<MatavimoVienetasDto> data;

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
        final Task<List<MatavimoVienetasDto>> searchTask = new Task<List<MatavimoVienetasDto>>()
        {
            protected List<MatavimoVienetasDto> call() throws Exception
            {
                Processor<MatavimoVienetaiListReq, MatavimoVienetaiListResp> p;
                p = getProcessor(MatavimoVienetaiListReq.class);
                MatavimoVienetaiListResp resp = p.process(new MatavimoVienetaiListReq());
                return resp.result;
            }
        };

        searchTask.stateProperty().addListener(new ChangeListener<Worker.State>()
        {
            public void changed(ObservableValue<? extends Worker.State> source, Worker.State oldState, Worker.State newState)
            {
                if (newState.equals(Worker.State.SUCCEEDED))
                {
                    data.clear();
                    for (MatavimoVienetasDto v: searchTask.getValue())
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
        show(Constants.MATAVIMOVIENETAI_PRESENTER, 0);
    }

    public void redaguoti(ActionEvent event)
    {
        MatavimoVienetasDto item = resultsList.getSelectionModel().getSelectedItem();
        if (item == null){
            this.setText("Parinkite įrašą");
            return;
        }
        show(Constants.MATAVIMOVIENETAI_PRESENTER, item.getId());
    }

    public void trinti(ActionEvent event)
    {

    }

   class MListDefinition extends ListDefinition<MatavimoVienetasDto>
   {
       MListDefinition() {
           fields.add(new FieldDefinition<MatavimoVienetasDto, String>("Kodas", 200, new PropertyValueFactory<MatavimoVienetasDto,String>("kodas")));
           fields.add(new FieldDefinition<MatavimoVienetasDto, String>("Pavadinimas", 200, new PropertyValueFactory<MatavimoVienetasDto,String>("pavadinimas")));
           fields.add(new FieldDefinition<MatavimoVienetasDto, Long>("Id", 50, new PropertyValueFactory<MatavimoVienetasDto,Long>("id")));
       }
   }
}

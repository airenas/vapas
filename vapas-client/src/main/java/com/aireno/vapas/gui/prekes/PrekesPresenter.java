package com.aireno.vapas.gui.prekes;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.Processor;
import com.aireno.dto.*;
import com.aireno.vapas.gui.base.GuiPresenter;
import com.aireno.vapas.gui.base.PresenterBase;
import com.aireno.vapas.gui.controls.ComboBoxWithId;
import com.aireno.vapas.service.MatavimoVienetaiService;
import com.aireno.vapas.service.PrekeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class PrekesPresenter extends PresenterBase implements Initializable, GuiPresenter
{
    @FXML private Node root;
    @FXML private Node bPane;
    @FXML private TextField pavadinimas;
    @FXML private TextArea pastaba;
    @FXML private ComboBoxWithId<MatavimoVienetasDto> matVienetas;

    public MatavimoVienetaiService getMatavimoVienetaiService() {
        return ApplicationContextProvider.getProvider().getBean(MatavimoVienetaiService.class);
    }

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
        setMatVienetai();
        if (id > 0){
            try {
                PrekeDto dto = this.getPrekeService().gauti(id);
                this.setText("Gauta");
                matVienetas.setValue(dto.getMatVienetasId());
                pavadinimas.setText(dto.getPavadinimas());
                pastaba.setText(dto.getPastaba());
            } catch (Exception e) {
                this.setText(e.getLocalizedMessage());
            }
        }
        else
        {
            matVienetas.setValue(null);
            pavadinimas.setText("");
            pastaba.setText("");
        }

        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void setMatVienetai() {
        matVienetas.getItems().clear();
        Processor<MatavimoVienetaiListReq, MatavimoVienetaiListResp> p;
        p = getProcessor(MatavimoVienetaiListReq.class);
        try {
            MatavimoVienetaiListResp resp = p.process(new MatavimoVienetaiListReq());
            this.setText("Gauta");
            ObservableList<MatavimoVienetasDto> data =
                    FXCollections.observableArrayList();
            data.clear();
            for (MatavimoVienetasDto v: resp.result)
            {
                data.add(v);
            }
            matVienetas.setItems(data);

            final StringConverter<MatavimoVienetasDto> converter = new StringConverter<MatavimoVienetasDto>() {

                @Override
                public String toString(MatavimoVienetasDto p) {
                    return p.getPavadinimas();
                }

                @Override
                public MatavimoVienetasDto fromString(String s) {
                    // TODO Auto-generated method stub
                    return null;
                }
            };

            matVienetas.setCellFactory(new Callback<ListView<MatavimoVienetasDto>, ListCell<MatavimoVienetasDto>>() {
                @Override
                public ListCell<MatavimoVienetasDto> call(ListView<MatavimoVienetasDto> matavimoVienetasDtoListView) {
                    final ListCell<MatavimoVienetasDto> cell = new ListCell<MatavimoVienetasDto>() {
                        {
                            super.setPrefWidth(100);
                        }
                        @Override public void updateItem(MatavimoVienetasDto item,
                                                         boolean empty) {
                            super.updateItem(item, empty);
                            if (item != null) {
                                setText(item.getPavadinimas());
                            }
                            else {
                                setText(null);
                            }
                        }
                    };
                    return cell;
                }
            });
            matVienetas.setConverter(new StringConverter<MatavimoVienetasDto>(){
                @Override public String toString(MatavimoVienetasDto object) {

                    System.out.print("converting object: ");
                    if (object==null) {
                        System.out.println("null");
                        return "[none]";
                    }
                    System.out.println(object.toString());
                    return object.getPavadinimas();
                }

                @Override public MatavimoVienetasDto fromString(String string) {
                    throw new RuntimeException("not required for non editable ComboBox");
                }

            });

        } catch (Exception e) {
            this.setText(e.getLocalizedMessage());
        }
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
        PrekeDto dto = new PrekeDto();
        dto.setPavadinimas(pavadinimas.getText());
        dto.setMatVienetasId(matVienetas.getValue().getId());
        dto.setPastaba(pastaba.getText());
        dto.setId(id);
        try {
            PrekeDto dtor = getPrekeService().saugoti(dto);
            this.setText("Išsaugota");
            id = dtor.getId();
        } catch (Exception e) {
           this.setText(e.getLocalizedMessage());
        }

    }

    public void iseiti(ActionEvent event)
    {
        this.goBack();
    }
}

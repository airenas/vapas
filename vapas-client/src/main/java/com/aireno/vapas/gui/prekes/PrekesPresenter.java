package com.aireno.vapas.gui.prekes;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.Processor;
import com.aireno.dto.MatavimoVienetaiListReq;
import com.aireno.dto.MatavimoVienetaiListResp;
import com.aireno.dto.MatavimoVienetasDto;
import com.aireno.dto.PrekeDto;
import com.aireno.vapas.gui.base.EntityPresenterBase;
import com.aireno.vapas.gui.controls.FilterLookup;
import com.aireno.vapas.service.MatavimoVienetaiService;
import com.aireno.vapas.service.PrekeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.apache.commons.lang.StringUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class PrekesPresenter extends EntityPresenterBase<PrekeDto> {
    @FXML
    private Node root;
    @FXML
    private Node bPane;
    @FXML
    private TextField pavadinimas;
    @FXML
    private TextArea pastaba;
    @FXML
    private FilterLookup<MatavimoVienetasDto> matVienetas;
    @FXML
    private Button bSaugoti;

    public MatavimoVienetaiService getMatavimoVienetaiService() {
        return ApplicationContextProvider.getProvider().getBean(MatavimoVienetaiService.class);
    }

    public PrekeService getPrekeService() {
        return ApplicationContextProvider.getProvider().getBean(PrekeService.class);
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public Node getView() {
        return root;
    }

    @Override
    public boolean init() {
        setMatVienetai();
        if (id > 0) {
            try {
                PrekeDto dto = this.getPrekeService().gauti(id);
                this.setText("Gauta");
                matVienetas.setValueId(dto.getMatVienetasId());
                pavadinimas.setText(dto.getPavadinimas());
                pastaba.setText(dto.getPastaba());
                item = dto;
            } catch (Exception e) {
                this.setText(e.getLocalizedMessage());
            }
        } else {
            matVienetas.setValueId(0);
            pavadinimas.setText("");
            pastaba.setText("");
            item = new PrekeDto();
        }

        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void setMatVienetai() {
        matVienetas.getItems().clear();
        Processor<MatavimoVienetaiListReq, MatavimoVienetaiListResp> p;
        p = getProcessor(MatavimoVienetaiListReq.class);
        try {
            MatavimoVienetaiListResp resp = p.process(new MatavimoVienetaiListReq());
            //this.setText("Gauta");
            ObservableList<MatavimoVienetasDto> data =
                    FXCollections.observableArrayList();
            data.clear();
            for (MatavimoVienetasDto v : resp.result) {
                data.add(v);
            }
            matVienetas.setData(data);

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

                        @Override
                        public void updateItem(MatavimoVienetasDto item,
                                               boolean empty) {
                            super.updateItem(item, empty);
                            if (item != null) {
                                setText(item.getPavadinimas());
                            } else {
                                setText(null);
                            }
                        }
                    };
                    return cell;
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

    public void saugotiInt() throws Exception {
        PrekeDto dto = new PrekeDto();
        dto.setPavadinimas(pavadinimas.getText());
        dto.setMatVienetasId(matVienetas.getValueId());
        dto.setPastaba(pastaba.getText());
        dto.setId(id);

        PrekeDto dtor = getPrekeService().saugoti(dto);
        this.setText("Išsaugota");
        id = dtor.getId();
        item = dtor;
    }

    @Override
    public void updateControls() {
        boolean pakeista = pakeista();
        bSaugoti.setDisable(!pakeista);
    }

    public String getTitle() {
        return "Prekė " + (getId() == 0 ? "nauja" : pavadinimas.getText());
    }

    @Override
    protected boolean pakeista() {
        if (!StringUtils.equals(StringUtils.defaultString(item.getPavadinimas()), pavadinimas.getText()))
            return true;
        if (!StringUtils.equals(StringUtils.defaultString(item.getPastaba()), pastaba.getText()))
            return true;
        if (item.getMatVienetasId() != matVienetas.getValueId())
            return true;
        return false;
    }
}

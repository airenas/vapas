package com.aireno.vapas.gui.prekes;

import com.aireno.Constants;
import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.LookupDto;
import com.aireno.base.Processor;
import com.aireno.dto.MatavimoVienetaiListReq;
import com.aireno.dto.MatavimoVienetaiListResp;
import com.aireno.dto.MatavimoVienetasDto;
import com.aireno.dto.PrekeDto;
import com.aireno.vapas.gui.base.EntityPresenterBase;
import com.aireno.vapas.gui.controls.FilterLookup;
import com.aireno.vapas.gui.controls.LongTextField;
import com.aireno.vapas.service.LookupService;
import com.aireno.vapas.service.MatavimoVienetaiService;
import com.aireno.vapas.service.PrekeService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    private LongTextField islaukaMesai;
    @FXML
    private LongTextField islaukaPienui;
    @FXML
    private FilterLookup<LookupDto> matVienetas;
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
    public boolean init() throws Exception {
        initializing = true;
        try {
            setMatVienetai();
            if (id > 0) {
                try {
                    item = this.getPrekeService().gauti(id);
                    this.setText("Gauta");

                } catch (Exception e) {
                    this.setText(e.getLocalizedMessage());
                    return false;
                }
            } else {
                item = new PrekeDto();
            }
            matVienetas.setValueId(item.getMatVienetasId());
            pavadinimas.setText(item.getPavadinimas());
            islaukaMesai.setValue(item.getIslaukaMesai());
            islaukaPienui.setValue(item.getIslaukaPienui());
            pastaba.setText(item.getPastaba());
        } finally {
            initializing = false;
        }
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void setMatVienetai() throws Exception {
        matVienetas.setEditable(true);
        matVienetas.setData(getLookupService().
                sarasas(new LookupService.LookupRequest(Constants.LOOKUP_MATAVIMO_VIENETAI)));
        matVienetas.valueProperty().addListener(new ChangeListener<LookupDto>() {
            @Override
            public void changed(ObservableValue<? extends LookupDto> observableValue, LookupDto lookupDto, LookupDto lookupDto2) {
                if (initializing) {
                    return;
                }
                update();

            }
        });
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
        dto.setIslaukaMesai(islaukaMesai.getValue());
        dto.setIslaukaPienui(islaukaPienui.getValue());
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
        if (!StringUtils.equals(StringUtils.defaultString(item.getPastaba()),
                StringUtils.defaultString(pastaba.getText())))
            return true;
        if (item.getMatVienetasId() != matVienetas.getValueId())
            return true;
        if (item.getIslaukaPienui() != islaukaPienui.getValue())
            return true;
        if (item.getIslaukaMesai() != islaukaMesai.getValue())
            return true;
        return false;
    }
}

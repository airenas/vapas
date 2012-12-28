package com.aireno.vapas.gui.matavimoVienetai;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.Processor;
import com.aireno.dto.*;
import com.aireno.vapas.gui.base.EntityPresenterBase;
import com.aireno.vapas.service.MatavimoVienetaiService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.commons.lang.StringUtils;

public class MatavimoVienetaiPresenter extends EntityPresenterBase<MatavimoVienetasDto> {
    @FXML
    private Node root;
    @FXML
    private Node bPane;
    @FXML
    private TextField kodas;
    @FXML
    private TextField pavadinimas;
    @FXML
    private Button bSaugoti;

    public MatavimoVienetaiService getMatavimoVienetaiService() {
        return ApplicationContextProvider.getProvider().getBean(MatavimoVienetaiService.class);
    }

    public Node getView() {
        return root;
    }

    @Override
    public boolean init() {
        if (id > 0) {
            Processor<MatavimoVienetasGautiReq, MatavimoVienetasGautiResp> p;
            p = getProcessor(MatavimoVienetasGautiReq.class);
            try {
                MatavimoVienetasGautiResp resp = p.process(new MatavimoVienetasGautiReq(id));
                this.setText("Gauta");
                kodas.setText(resp.result.getKodas());
                pavadinimas.setText(resp.result.getPavadinimas());
                item = resp.result;
            } catch (Exception e) {
                this.setText(e.getLocalizedMessage());
            }
        } else {
            kodas.setText("");
            pavadinimas.setText("");
            item = new MatavimoVienetasDto();
        }
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Node getButtonView() {
        return bPane;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void saugotiInt() throws Exception {
        MatavimoVienetasDto dto = new MatavimoVienetasDto();
        dto.setPavadinimas(pavadinimas.getText());
        dto.setKodas(kodas.getText());
        dto.setId(id);
        Processor<MatavimoVienetasSaveReq, MatavimoVienetasSaveResp> p;
        p = getProcessor(MatavimoVienetasSaveReq.class);
        MatavimoVienetasSaveResp resp = p.process(new MatavimoVienetasSaveReq(dto));
        this.setText("Išsaugota");
        id = resp.result.getId();
        this.item = resp.result;
    }

    @Override
    public void updateControls() {
        boolean pakeista = pakeista();
        bSaugoti.setDisable(!pakeista);
    }

    public String getTitle() {
        return "Matavimo vienetas " + (getId() == 0 ? "naujas" : pavadinimas.getText());
    }

    @Override
    protected boolean pakeista() {
        if (!StringUtils.equals(StringUtils.defaultString(item.getKodas()), kodas.getText()))
            return true;
        if (!StringUtils.equals(StringUtils.defaultString(item.getPavadinimas()), pavadinimas.getText()))
            return true;
        return false;
    }
}

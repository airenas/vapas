package com.aireno.vapas.gui.tiekejai;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.Processor;
import com.aireno.dto.*;
import com.aireno.vapas.gui.base.GuiPresenter;
import com.aireno.vapas.gui.base.PresenterBase;
import com.aireno.vapas.service.MatavimoVienetaiService;
import com.aireno.vapas.service.TiekejasService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class TiekejaiPresenter extends PresenterBase implements Initializable, GuiPresenter
{
    @FXML private Node root;
    @FXML private Node bPane;

    @FXML private TextField pavadinimas;
    @FXML private TextField adresas;
    @FXML private TextField telefonas;
    @FXML private TextArea pastaba;

    public void initialize(URL url, ResourceBundle resourceBundle)
    {
    }

    public Node getView()
    {
        return root;
    }

    public TiekejasService getTiekejasService() {
        return ApplicationContextProvider.getProvider().getBean(TiekejasService.class);
    }

    @Override
    public boolean init() {
        if (id > 0){
            try {
                TiekejasDto dto = getTiekejasService().gauti(id);
                this.setText("Gauta");
                adresas.setText(dto.getAdresas());
                telefonas.setText(dto.getTelefonas());
                pastaba.setText(dto.getPastaba());
                pavadinimas.setText(dto.getPavadinimas());
            } catch (Exception e) {
                this.setText(e.getLocalizedMessage());
            }
        }
        else
        {
            adresas.setText("");
            pavadinimas.setText("");
            telefonas.setText("");
            pastaba.setText("");
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
        TiekejasDto dto = new TiekejasDto();
        dto.setPavadinimas(pavadinimas.getText());
        dto.setTelefonas(telefonas.getText());
        dto.setAdresas(adresas.getText());
        dto.setPastaba(pastaba.getText());
        dto.setId(id);
        try {
            getTiekejasService().saugoti(dto);
            this.setText("Išsaugota");
        } catch (Exception e) {
           this.setError("Klaida saugant: ", e);
        }

    }

    public void iseiti(ActionEvent event)
    {
        this.goBack();
    }
}

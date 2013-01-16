package com.aireno.vapas.gui.ataskaitos;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.LookupDto;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.PresenterBase;
import com.aireno.vapas.gui.controls.FilterLookup;
import com.aireno.vapas.service.AtaskaitaService;
import com.aireno.vapas.service.LookupService;
import com.panemu.tiwulfx.form.DateControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AtaskaitaPresenter extends PresenterBase {
    @FXML
    private Node root;
    @FXML
    private Node bPane;

    @FXML
    private DateControl data;
    @FXML
    private TextField numeris;
    @FXML
    private FilterLookup<LookupDto> imone;

    public Node getView() {
        return root;
    }

    public AtaskaitaService getService() {
        return ApplicationContextProvider.getProvider().getBean(AtaskaitaService.class);
    }

    @Override
    public boolean init() throws Exception {
        data.setDateFormat(new SimpleDateFormat(Constants.DATE_FORMAT));
        //data.setValue(Calendar.getInstance().getTime());
        imone.setData(getLookupService().
                sarasas(new LookupService.LookupRequest(com.aireno.Constants.LOOKUP_IMONE)));

        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Node getButtonView() {
        return bPane;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void nurasymai(ActionEvent event) {
        try {
            this.setText("GENERUOJAMA ....");
            getService().generuotiNurasymus(new AtaskaitaService.GeneruotiRequest(
                    data.getValue(), imone.getValueId(), numeris.getText()));
            this.setText("Sugeneruota");
        } catch (Exception e) {
            this.setText("");
            this.setError("Klaida generuojant: ", e);
        }
    }

    public void likuciai(ActionEvent event) {
        try {
            this.setText("GENERUOJAMA ....");
            getService().generuotiLikucius(new AtaskaitaService.GeneruotiRequest(
                    data.getValue(), imone.getValueId(), numeris.getText()));
            this.setText("Sugeneruota");
        } catch (Exception e) {
            this.setText("");
            this.setError("Klaida generuojant: ", e);
        }
    }

    public void iseiti(ActionEvent event) {
        this.goBack();
    }

    public void zurnalas(ActionEvent event) {
        try {
            this.setText("GENERUOJAMA ....");
            getService().generuotiZurnala(new AtaskaitaService.GeneruotiRequest(
                    data.getValue(), imone.getValueId(), numeris.getText()));
            this.setText("Sugeneruota");
        } catch (Exception e) {
            this.setText("");
            this.setError("Klaida generuojant: ", e);
        }
    }

    public String getTitle() {
        return "Ataskaitos generavimas";
    }
}

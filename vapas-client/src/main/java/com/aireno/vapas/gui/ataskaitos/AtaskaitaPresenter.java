package com.aireno.vapas.gui.ataskaitos;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.LookupDto;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.PresenterBase;
import com.aireno.vapas.gui.controls.FilterLookup;
import com.aireno.vapas.service.AtaskaitaService;
import com.aireno.vapas.service.LookupService;
import com.panemu.tiwulfx.form.DateControl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.text.SimpleDateFormat;
import java.util.concurrent.Callable;

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

    @FXML
    private Button bZurnalas;

    @FXML
    private Button bNurasymai;

    @FXML
    private Button bLikuciai;

    @FXML
    private Button bLikuciaiLent;

    boolean working;

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
        RunAssync(new Callable() {
            @Override
            public Object call() throws Exception {
                getService().generuotiNurasymus(new AtaskaitaService.GeneruotiRequest(
                        data.getValue(), imone.getValueId(), numeris.getText()));
                return true;
            }
        });
    }

    private class RTask extends Task<Boolean> {
        Callable func;

        @Override
        protected Boolean call() throws Exception {
            working = true;
            try {
                func.call();
            } finally {
                working = false;
            }
            return true;
        }

        private RTask(Callable func) {
            this.func = func;
            stateProperty().addListener(new ChangeListener<Worker.State>() {
                public void changed(ObservableValue<? extends Worker.State> source, Worker.State oldState, Worker.State newState) {
                    if (newState.equals(Worker.State.SUCCEEDED)) {
                        setText("Paruošta");
                    } else if (newState.equals(Worker.State.FAILED)) {
                        setText("");
                        setError("Klaida ruošiant: ", getException());
                    }
                    update();
                }
            });
        }


    }

    public void likuciai(ActionEvent event) {
        RunAssync(new Callable() {
            @Override
            public Object call() throws Exception {
                getService().generuotiLikucius(new AtaskaitaService.GeneruotiRequest(
                        data.getValue(), imone.getValueId(), numeris.getText()));
                return true;
            }
        });
    }

    public void likuciaiLent(ActionEvent event) {
        RunAssync(new Callable() {
            @Override
            public Object call() throws Exception {
                getService().generuotiDabartiniusLikucius(new AtaskaitaService.GeneruotiRequest(
                        data.getValue(), imone.getValueId(), numeris.getText()));
                return true;
            }
        });
    }

    private void RunAssync(Callable func)
    {
        RTask task = new RTask(func);
        working = true;
        update();
        setText("RUOŠIAMA ....");
        new Thread(task).start();

    }

    public void iseiti(ActionEvent event) {
        this.goBack();
    }

    public void zurnalas(ActionEvent event) {
        RunAssync(new Callable() {
            @Override
            public Object call() throws Exception {
                getService().generuotiZurnala(new AtaskaitaService.GeneruotiRequest(
                        data.getValue(), imone.getValueId(), numeris.getText()));
                return true;
            }
        });
    }

    public String getTitle() {
        return "Ataskaitos ruošimas";
    }

    @Override
    public void updateControls() {

        bNurasymai.setDisable(working);
        bLikuciai.setDisable(working);
        bZurnalas.setDisable(working);
        bLikuciaiLent.setDisable(working);
    }
}

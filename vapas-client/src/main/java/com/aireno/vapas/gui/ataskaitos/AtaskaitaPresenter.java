package com.aireno.vapas.gui.ataskaitos;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.LookupDto;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.PresenterBase;
import com.aireno.vapas.gui.controls.FilterLookup;
import com.aireno.vapas.service.AtaskaitaService;
import com.aireno.vapas.service.LookupService;
import com.aireno.vapas.service.NustatymasService;
import com.panemu.tiwulfx.form.DateControl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.text.SimpleDateFormat;
import java.util.concurrent.Callable;

public class AtaskaitaPresenter extends PresenterBase {
    public CheckBox cbRodytiTikTeigiamus;
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
    protected void initInternal() throws Exception {
    }

    @Override
    protected void initOnce() throws Exception {
        cbRodytiTikTeigiamus.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean lookupDto,
                                Boolean lookupDto2) {
                if (!initializing) {
                    runAndExpectError(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getNustatymasService()
                                        .saugotiBoolean(new NustatymasService.NustatymasSaugotiRequest<Boolean>("ATASKAITA_TIK_TEIGIAMI",
                                                cbRodytiTikTeigiamus.isSelected()));

                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, "Saugoma nustatymas");
                }
            }
        });
        data.dateFormatProperty().setValue(new SimpleDateFormat(com.aireno.Constants.DATE_FORMAT));
        //data.setValue(Calendar.getInstance().getTime());
        imone.setData(getLookupService().
                sarasas(new LookupService.LookupRequest(com.aireno.Constants.LOOKUP_IMONE)));
        cbRodytiTikTeigiamus.setSelected(getNustatymasService()
                .gautiBoolean(new NustatymasService.NustatymasRequest<Boolean>("ATASKAITA_TIK_TEIGIAMI", false))
                .result);
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
                getService().generuotiDabartiniusLikucius(new AtaskaitaService.GeneruotiDabLikuciaiRequest(
                        data.getValue(), imone.getValueId(), numeris.getText(),
                        cbRodytiTikTeigiamus.isSelected()));
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

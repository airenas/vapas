package com.aireno.vapas.gui.main;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.GuiException;
import com.aireno.vapas.gui.base.GuiPresenter;
import com.aireno.vapas.gui.base.PresenterBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jfxtras.labs.dialogs.DialogFX;
import org.springframework.beans.BeansException;

public class MainPresenter extends PresenterBase {
    @FXML
    private Parent root;
    @FXML
    private AnchorPane dataArea;
    @FXML
    private AnchorPane buttonArea;
    @FXML
    private Label topLabel;

    GuiPresenter currentPresenter;
    private Stage stage;

    public Parent getView() {
        return root;
    }

    @Override
    public boolean init() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Node getButtonView() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean needSave() {
        return false;
    }

    @Override
    public void save() {

    }

    public void show(String viewName) {
        GuiPresenter presenter = null;
        try {
            presenter = ApplicationContextProvider.getProvider().getBean(viewName, GuiPresenter.class);
        } catch (BeansException e) {
            this.setError("Klaida", e);
            return;
        }
        presenter.from(currentPresenter);
        show(presenter);
    }

    public void show(String viewName, long id) {
        GuiPresenter presenter = null;
        try {
            presenter = ApplicationContextProvider.getProvider().getBean(viewName, GuiPresenter.class);
        } catch (BeansException e) {
            this.setError("Klaida", e);
            return;
        }
        presenter.setId(id);
        presenter.from(currentPresenter);
        show(presenter);
    }

    public void show(GuiPresenter presenter) {
        try {
            if (currentPresenter != presenter && currentPresenter != null) {
                if (currentPresenter.needSave()) {
                    if (saveYes(currentPresenter.getTitle())) {
                        currentPresenter.save();
                    }
                }
            }
            if (!presenter.init())
                return;
        } catch (GuiException e) {
            this.setError(e.getErrorOn(), e);
            return;
        } catch (Exception e) {
            this.setError("Klaida:", e);
            return;
        }
        //presenter.from(currentPresenter);
        Node node = presenter.getView();
        dataArea.getChildren().clear();
        dataArea.getChildren().add(node);
        AnchorPane.setTopAnchor(node, 10.0);
        AnchorPane.setLeftAnchor(node, 10.0);
        AnchorPane.setRightAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);

        buttonArea.getChildren().clear();
        if (presenter.getButtonView() != null)
            buttonArea.getChildren().add(presenter.getButtonView());
        //buttonArea.setCenter(presenter.getButtonView());
        currentPresenter = presenter;
        update();
    }

    private boolean saveYes(String title) {
        DialogFX dialog = new DialogFX(DialogFX.Type.QUESTION);
        dialog.setTitleText(title);
        dialog.setMessage("Buvo pakeitimų. Išsaugoti?");
        int result = dialog.showDialog();
        return result == 0;
    }

    public void showMain(ActionEvent event) {
        show(Constants.MAIN_PRESENTER);
    }

    public void setText(String info) {
        topLabel.setText(info);
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setTitle(String info) {
        if (stage != null)
            stage.setTitle(info);
    }

    public void update() {
        if (currentPresenter != null) {
            currentPresenter.updateControls();
            setTitle(currentPresenter.getTitle());
        }
    }
}

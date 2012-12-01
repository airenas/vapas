package com.aireno.vapas.gui.main;

import com.aireno.vapas.gui.Constants;
import com.aireno.base.ApplicationContextProvider;
import com.aireno.vapas.gui.base.GuiException;
import com.aireno.vapas.gui.base.GuiPresenter;
import com.aireno.vapas.gui.base.PresenterBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.BeansException;

public class MainPresenter extends PresenterBase
{
    @FXML private Parent root;
    @FXML private BorderPane dataArea;
    @FXML private BorderPane buttonArea;
    @FXML private Label topLabel;

    GuiPresenter currentPresenter;

    public Parent getView()
    {
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

    public void show(GuiPresenter presenter) {
        try {
           if (!presenter.init())
               return;
        } catch (GuiException e) {
            this.setError(e.getErrorOn(), e);
            return;
        }
        catch (Exception e) {
            this.setError("Klaida:", e);
            return;
        }
        //presenter.from(currentPresenter);
        dataArea.setCenter(presenter.getView());
        buttonArea.setCenter(presenter.getButtonView());
        currentPresenter = presenter;
    }

    public void showMain(ActionEvent event) {
        show(Constants.MAIN_PRESENTER);
    }

    public void setText(String info) {
        topLabel.setText(info);
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
}

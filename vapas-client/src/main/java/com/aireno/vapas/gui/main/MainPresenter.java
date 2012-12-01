package com.aireno.vapas.gui.main;

import com.aireno.vapas.gui.Constants;
import com.aireno.base.ApplicationContextProvider;
import com.aireno.vapas.gui.base.GuiPresenter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class MainPresenter
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

    public void show(String viewName) {
        GuiPresenter presenter = ApplicationContextProvider.getProvider().getBean(viewName, GuiPresenter.class);
        presenter.from(currentPresenter);
        show(presenter);
    }

    public void show(GuiPresenter presenter) {
        presenter.init();
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
        GuiPresenter presenter = ApplicationContextProvider.getProvider().getBean(viewName, GuiPresenter.class);
        presenter.setId(id);
        presenter.from(currentPresenter);
        show(presenter);
    }
}

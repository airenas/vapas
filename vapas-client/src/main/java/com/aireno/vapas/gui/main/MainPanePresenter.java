
package com.aireno.vapas.gui.main;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.GuiPresenter;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;

public class MainPanePresenter implements GuiPresenter
{
    @FXML private Parent root;

    public Parent getView()
    {
        return root;
    }

    @Override
    public boolean init() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void showMatavimoVienetai() {
        MainPresenter mainPresenter = ApplicationContextProvider.getProvider().getBean("MainPresenter", MainPresenter.class);
        mainPresenter.show(Constants.MATAVIMOVIENETAILIST_PRESENTER);
    }

    public void showTiekejai() {
        MainPresenter mainPresenter = ApplicationContextProvider.getProvider().getBean("MainPresenter", MainPresenter.class);
        mainPresenter.show(Constants.TIEKEJAILIST_PRESENTER);
    }

    public void showPrekes() {
        MainPresenter mainPresenter = ApplicationContextProvider.getProvider().getBean("MainPresenter", MainPresenter.class);
        mainPresenter.show(Constants.PREKESLIST_PRESENTER);
    }

    public Node getButtonView() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void from(GuiPresenter currentPresenter) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setId(long id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}

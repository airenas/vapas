package com.aireno.vapas.gui.main;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.base.GuiPresenter;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;

public class MainPanePresenter implements GuiPresenter {
    @FXML
    private Parent root;

    public Parent getView() {
        return root;
    }

    @Override
    public boolean init() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void showMatavimoVienetai() {
        MainPresenter mainPresenter = ApplicationContextProvider.getProvider().getBean("MainPresenter", MainPresenter.class);
        mainPresenter.show(Constants.MATAVIMOVIENETAILIST_PRESENTER);
    }

    public void showAtaskaitos() {
        MainPresenter mainPresenter = ApplicationContextProvider.getProvider().getBean("MainPresenter", MainPresenter.class);
        mainPresenter.show(Constants.ATASKAITALIST_PRESENTER);
    }

    public void showTiekejai() {
        MainPresenter mainPresenter = ApplicationContextProvider.getProvider().getBean("MainPresenter", MainPresenter.class);
        mainPresenter.show(Constants.TIEKEJAILIST_PRESENTER);
    }

    public void showPrekes() {
        MainPresenter mainPresenter = ApplicationContextProvider.getProvider().getBean("MainPresenter", MainPresenter.class);
        mainPresenter.show(Constants.PREKESLIST_PRESENTER);
    }

    public void showImones() {
        MainPresenter mainPresenter = getMainPresenter();
        mainPresenter.show(Constants.IMONESLIST_PRESENTER);
    }

    public void showNurasymai() {
        MainPresenter mainPresenter = getMainPresenter();
        mainPresenter.show(Constants.NURASYMASLIST_PRESENTER);
    }

    public void showLikuciai() {
        MainPresenter mainPresenter = getMainPresenter();
        mainPresenter.show(Constants.LIKUTISLIST_PRESENTER);
    }

    public void showSaskaitos() {
        MainPresenter mainPresenter = getMainPresenter();
        mainPresenter.show(Constants.SASKAITALIST_PRESENTER);
    }

    public void showGyvunuRusys() {
        MainPresenter mainPresenter = getMainPresenter();
        mainPresenter.show(Constants.GYVUNORUSISLIST_PRESENTER);
    }

    public void showGydymoZurnalas() {
        MainPresenter mainPresenter = getMainPresenter();
        mainPresenter.show(Constants.GYDYMOZURNALASLIST_PRESENTER);
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

    @Override
    public String getTitle() {
        return "Vaistų apskaita";
    }

    @Override
    public void updateControls() {
        MainPresenter mainPresenter = getMainPresenter();
        mainPresenter.updateControls();
    }

    @Override
    public boolean needSave() {
        return false;
    }

    @Override
    public void save() {

    }

    public MainPresenter getMainPresenter() {
        return ApplicationContextProvider.getProvider().getBean("MainPresenter", MainPresenter.class);
    }
}

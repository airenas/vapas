package com.aireno.vapas.gui.base;

import javafx.scene.Node;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.11
 * Time: 09.36
 * To change this template use File | Settings | File Templates.
 */
public interface GuiPresenter {
    Node getView();
    boolean init() throws GuiException;
    Node getButtonView();
    void from(GuiPresenter currentPresenter);

    void setId(long id);

    String getTitle();
}

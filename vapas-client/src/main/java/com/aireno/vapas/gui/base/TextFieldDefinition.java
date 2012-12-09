package com.aireno.vapas.gui.base;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;


/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.12
 * Time: 23.34
 * To change this template use File | Settings | File Templates.
 */
public class TextFieldDefinition<T> extends EditFieldDefinition<T, String> {
       public TextFieldDefinition(String name, int size, Callback<TableColumn.CellDataFeatures<T, String>, ObservableValue<String>> valueFactory, EventHandler<TableColumn.CellEditEvent<T, String>> cellEditEventEventHandler) {
        super(name, size, valueFactory, cellEditEventEventHandler);
    }

    @Override
    protected TableCell createCell() {
        return new TextCell();
    }
}

class TextCell<T> extends com.aireno.vapas.gui.controls.EditingCell<T, String> {

    @Override
    protected String getValue(String s) {
        return s;
    }

    @Override
    protected String getShowValue(String s) {
        return s;
    }
}

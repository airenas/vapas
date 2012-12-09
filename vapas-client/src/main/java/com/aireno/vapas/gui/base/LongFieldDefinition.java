package com.aireno.vapas.gui.base;

import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;


/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.12
 * Time: 23.34
 * To change this template use File | Settings | File Templates.
 */
public class LongFieldDefinition<T> extends EditFieldDefinition<T, Long> {
    public LongFieldDefinition(String name, int size, Callback<TableColumn.CellDataFeatures<T, Long>,
            ObservableValue<Long>> valueFactory, EventHandler<TableColumn.CellEditEvent<T, Long>> cellEditEventEventHandler) {
        super(name, size, valueFactory, cellEditEventEventHandler);
    }

    @Override
    protected TableCell createCell() {
        return new LongCell();
    }
}

class LongCell<T> extends com.aireno.vapas.gui.controls.EditingCell<T, Long> {

    @Override
    protected Long getValue(String s) {
        return Long.parseLong(s);
    }

    @Override
    protected String getShowValue(Long aLong) {
        return aLong.toString();
    }
}

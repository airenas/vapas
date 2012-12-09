package com.aireno.vapas.gui.base;

import com.aireno.vapas.gui.base.EditFieldDefinition;
import com.aireno.vapas.gui.base.ListDefinition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;


/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.12
 * Time: 23.34
 * To change this template use File | Settings | File Templates.
 */
public class DecimalFieldDefinition<T> extends EditFieldDefinition<T, BigDecimal> {
    public DecimalFieldDefinition(String name, int size, Callback<TableColumn.CellDataFeatures<T, BigDecimal>, ObservableValue<BigDecimal>> valueFactory, EventHandler<TableColumn.CellEditEvent<T, BigDecimal>> cellEditEventEventHandler) {
        super(name, size, valueFactory, cellEditEventEventHandler);
    }

    @Override
    protected TableCell createCell() {
        return new DecimalCell();
    }
}

class DecimalCell<T> extends com.aireno.vapas.gui.controls.EditingCell<T, BigDecimal> {

    @Override
    protected BigDecimal getValue(String s) {
        if (StringUtils.isEmpty(s))
            return null;
        return new BigDecimal(s);
    }

    @Override
    protected String getShowValue(BigDecimal aLong) {
        if (aLong == null)
            return "";
        return aLong.toString();
    }
}


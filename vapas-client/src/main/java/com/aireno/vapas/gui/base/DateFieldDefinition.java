package com.aireno.vapas.gui.base;

import com.aireno.vapas.gui.Constants;
import com.aireno.vapas.gui.controls.EditingCell;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.12
 * Time: 23.34
 * To change this template use File | Settings | File Templates.
 */
public class DateFieldDefinition<T> extends EditFieldDefinition<T, Date> {
    public DateFieldDefinition(String name, int size, Callback<TableColumn.CellDataFeatures<T, Date>, ObservableValue<Date>> valueFactory, EventHandler<TableColumn.CellEditEvent<T, Date>> cellEditEventEventHandler) {
        super(name, size, valueFactory, cellEditEventEventHandler);
    }

    @Override
    protected TableCell createCell() {
        return new DateCell();
    }
}



class DateCell<T> extends EditingCell<T, Date> {

    @Override
    protected Date getValue(String s) {
        if (StringUtils.isEmpty(s))
            return null;

        DateFormat formatter ;
        Date date ;
        formatter = new SimpleDateFormat(Constants.DATE_FORMAT);
        try {
            date = (Date)formatter.parse(s);
        } catch (ParseException e) {
            throw  new RuntimeException(e);
        }
        return date;
    }

    @Override
    protected String getShowValue(Date item) {
        if (item == null)
            return "";
        return new SimpleDateFormat(Constants.DATE_FORMAT).format(item);
    }
}


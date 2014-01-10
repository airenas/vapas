package com.aireno.vapas.gui.tablefields;

import com.aireno.vapas.gui.base.EditFieldDefinition;
import com.aireno.vapas.gui.controls.EditingCell;
import com.panemu.tiwulfx.form.DateControl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

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
    public DateFieldDefinition(String name, int size,
                               Callback<TableColumn.CellDataFeatures<T, Date>, ObservableValue<Date>> valueFactory,
                               EditFieldDefinition.ChangeEvent<T, Date> editEventHandler) {
        super(name, size, valueFactory, editEventHandler);
    }

    @Override
    protected TableCell createCell() {
        DateEditingCell result = new DateEditingCell(editEventHandler);
        return result;
    }
}

class DateEditingCell<T> extends EditingCell<T, Date, DateControl> {

    protected DateEditingCell(EditFieldDefinition.ChangeEvent<T, Date> editEventHandler) {
        super(editEventHandler);
    }

    @Override
    protected void setFieldValue(Date item) {
        field.setValue(item);
    }

    @Override
    protected DateControl createFieldInternal() {
        DateControl field = new DateControl();
        field.setDateFormat(new SimpleDateFormat(com.aireno.Constants.DATE_FORMAT));
        field.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

        field.getInputComponent().selectedDateProperty().addListener(new ChangeListener<Date>() {
            @Override
            public void changed(ObservableValue<? extends Date> observableValue, Date date, Date date2) {
                onChangedTo(date2);
            }
        });
        return field;
    }
}


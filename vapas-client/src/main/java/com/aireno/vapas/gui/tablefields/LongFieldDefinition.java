package com.aireno.vapas.gui.tablefields;

import com.aireno.vapas.gui.base.EditFieldDefinition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
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
            ObservableValue<Long>> valueFactory, ChangeEvent<T, Long> editEventHandler) {
        super(name, size, valueFactory, editEventHandler);
    }

    @Override
    protected TableCell createCell() {
        return new LongCell(editEventHandler);
    }
}

class LongCell<T> extends com.aireno.vapas.gui.controls.EditingCell<T, Long, TextField> {

    protected LongCell(EditFieldDefinition.ChangeEvent<T, Long> editEventHandler) {
        super(editEventHandler);
    }

    protected Long getValue(String s) {
        return Long.parseLong(s);
    }

    @Override
    protected void setFieldValue(Long item) {
        if (item == null) {
            field.setText(null);
        } else {
            field.setText(item.toString());
        }
    }

    @Override
    protected TextField createFieldInternal() {
        TextField textField = new TextField();
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0,
                                Boolean arg1, Boolean arg2) {
                if (!arg2 && field != null) {
                    onChangedTo(getValue(field.getText()));
                }
            }
        });
        return textField;
    }
}

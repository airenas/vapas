package com.aireno.vapas.gui.base;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
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
    public DecimalFieldDefinition(String name, int size, Callback<TableColumn.CellDataFeatures<T, BigDecimal>, ObservableValue<BigDecimal>> valueFactory,
                                  ChangeEvent<T, BigDecimal> editEventHandler) {
        super(name, size, valueFactory, editEventHandler);
    }

    @Override
    protected TableCell createCell() {
        return new DecimalCell(editEventHandler);
    }
}

class DecimalCell<T> extends com.aireno.vapas.gui.controls.EditingCell<T, BigDecimal, TextField> {
    protected DecimalCell(EditFieldDefinition.ChangeEvent<T, BigDecimal> editEventHandler) {
        super(editEventHandler);
    }

    protected BigDecimal getValue(String s) {
        if (StringUtils.isEmpty(s))
            return null;
        return new BigDecimal(s);
    }

    @Override
    protected void setFieldValue(BigDecimal item) {
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
                    editEventHandler.handle(getRecord(), getValue(field.getText()));
                }
            }
        });
        return textField;
    }
}


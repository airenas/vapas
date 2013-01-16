package com.aireno.vapas.gui.tablefields;

import com.aireno.utils.ANumberUtils;
import com.aireno.vapas.gui.base.EditFieldDefinition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;


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

    protected BigDecimal getValue(String s) throws ParseException {
        if (StringUtils.isEmpty(s))
            return null;
        return ANumberUtils.stringToDecimal(s);
    }

    @Override
    protected void setFieldValue(BigDecimal item) {
        field.setText(ANumberUtils.decimalToString(item));
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
                    try {
                        onChangedTo(getValue(field.getText()));
                    } catch (ParseException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        });
        return textField;
    }
}


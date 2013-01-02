package com.aireno.vapas.gui.base;

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
public class TextFieldDefinition<T> extends EditFieldDefinition<T, String> {
    public TextFieldDefinition(String name, int size, Callback<TableColumn.CellDataFeatures<T, String>, ObservableValue<String>> valueFactory,
                                    ChangeEvent<T, String> editEventHandler) {
        super(name, size, valueFactory, editEventHandler);
    }

    @Override
    protected TableCell createCell() {
        return new TextCell(editEventHandler);
    }
}

class TextCell<T> extends com.aireno.vapas.gui.controls.EditingCell<T, String, TextField> {
    protected TextCell(EditFieldDefinition.ChangeEvent<T, String> editEventHandler) {
        super(editEventHandler);
    }

    @Override
    protected void setFieldValue(String item) {
        field.setText(item);
    }

    @Override
    protected TextField createFieldInternal() {
        TextField textField = new TextField();
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0,
                                Boolean arg1, Boolean arg2) {
                if (!arg2) {
                    if (field != null) {
                        onChangedTo(field.getText());
                    }
                }
            }
        });
        return textField;
    }
}

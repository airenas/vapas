package com.aireno.vapas.gui.controls;

import com.aireno.vapas.gui.base.EditFieldDefinition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;

import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.12
 * Time: 23.34
 * To change this template use File | Settings | File Templates.
 */
public abstract class EditingCell<T, S, TField extends Node> extends TableCell<T, S> {

    public EditFieldDefinition.ChangeEvent<T, S> getEditEventHandler() {
        return editEventHandler;
    }

    public void setEditEventHandler(EditFieldDefinition.ChangeEvent<T, S> editEventHandler) {
        this.editEventHandler = editEventHandler;
    }

    protected EditFieldDefinition.ChangeEvent<T, S> editEventHandler;

    protected TField field;

    protected EditingCell(EditFieldDefinition.ChangeEvent<T, S> editEventHandler) {
        this.editEventHandler = editEventHandler;
    }

    @Override
    public void updateItem(S item, boolean empty) {
        super.updateItem(item, false);
        ObservableValue<S> d = getTableColumn().getCellObservableValue(getIndex());
        if (d == null)
        {
            return;
        }
        createField();
        setGraphic(field);
        setFieldValue(item);
    }

    protected abstract void setFieldValue(S item);

    private void createField() {
        if (field != null) {
            return;
        }
        field = createFieldInternal();
    }

    protected abstract TField createFieldInternal();

    protected T getRecord() {
        return (T)getTableView().getItems().get(getIndex());
    }
}

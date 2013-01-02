package com.aireno.vapas.gui.base;

import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;


/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.12
 * Time: 23.34
 * To change this template use File | Settings | File Templates.
 */
public abstract class EditFieldDefinition<T, S> extends FieldDefinition<T, S> {
    protected final ChangeEvent<T, S> editEventHandler;

    public EditFieldDefinition(String name, int size, Callback<TableColumn.CellDataFeatures<T, S>,
            ObservableValue<S>> valueFactory, ChangeEvent<T, S> editEventHandler) {
        super(name, size, valueFactory);
        this.editEventHandler = editEventHandler;
    }

    public TableColumn<T, S> addToTable(ListDefinition<T> tListDefinition, TableView<T> tableView) {
        TableColumn<T, S> col = new TableColumn<T, S>(getName());
        col.prefWidthProperty().setValue(getSize());
        col.setCellValueFactory(getValueFactory());

        Callback<TableColumn<T, S>, TableCell<T, S>> cellFactory =
                new Callback<TableColumn<T, S>, TableCell<T, S>>() {
                    public TableCell call(TableColumn p) {
                        return createCell();
                    }
                };
        col.setEditable(true);
        col.setCellFactory(cellFactory);
        tableView.getColumns().add(col);
        return col;
    }

    protected abstract TableCell createCell();

    public interface ChangeEvent<T, S> {
        void handle(ChangeEventParam<T, S> param);

        public class ChangeEventParam<T, S>
        {
            public T item;
            public S value;
            public TableView<T> tableView;
            public int index;
        }
    }
}



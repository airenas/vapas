package com.aireno.vapas.gui.base;

import com.aireno.dto.SaskaitosPrekeDto;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;


/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.12
 * Time: 23.34
 * To change this template use File | Settings | File Templates.
 */
public abstract class EditFieldDefinition<T, S> extends FieldDefinition<T, S> {


    private final EventHandler<TableColumn.CellEditEvent<T,S>> cellEditEventEventHandler;

    public EditFieldDefinition(String name, int size, Callback<TableColumn.CellDataFeatures<T, S>,
            ObservableValue<S>> valueFactory, EventHandler<TableColumn.CellEditEvent<T,S>> cellEditEventEventHandler) {
        super(name, size, valueFactory);
        this.cellEditEventEventHandler = cellEditEventEventHandler;
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

        col.setOnEditCommit(cellEditEventEventHandler);

        col.setEditable(true);
        col.setCellFactory(cellFactory);
        tableView.getColumns().add(col);
        return col;
    }

    protected abstract TableCell createCell();
}

package com.aireno.vapas.gui.base;

import javafx.beans.value.ObservableValue;
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
public class FieldDefinition<T, S> {
    public FieldDefinition(String name, int size, Callback<TableColumn.CellDataFeatures<T, S>, ObservableValue<S>> valueFactory) {
        this.name = name;
        this.size = size;
        this.valueFactory = valueFactory;
    }

    public TableColumn<T, S> addToTable(ListDefinition<T> tListDefinition, TableView<T> tableView) {
        TableColumn<T, S> col = new TableColumn<T, S>(getName());
        col.prefWidthProperty().setValue(getSize());
        col.setCellValueFactory(getValueFactory());
        tableView.getColumns().add(col);
        return col;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    String name;
    String type;
    int size;
    boolean visible;
    int pos;
    String property;
    Callback<TableColumn.CellDataFeatures<T, S>, ObservableValue<S>> valueFactory;

    public Callback<TableColumn.CellDataFeatures<T, S>, ObservableValue<S>> getValueFactory() {
        return valueFactory;  //To change body of created methods use File | Settings | File Templates.
    }
}

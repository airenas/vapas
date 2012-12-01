package com.aireno.vapas.gui.base;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.12
 * Time: 23.39
 * To change this template use File | Settings | File Templates.
 */
public abstract class ListDefinition<T> {
    protected List<FieldDefinition> fields = new ArrayList<FieldDefinition>();

    public void InitTable(TableView<T> tableView)
    {
        tableView.getColumns().clear();
        for (FieldDefinition f: fields)
        {
            f.addToTable(this, tableView);
        }
    }





}

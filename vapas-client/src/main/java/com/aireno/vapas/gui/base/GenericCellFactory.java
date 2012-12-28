package com.aireno.vapas.gui.base;

import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.12.27
 * Time: 23.15
 * To change this template use File | Settings | File Templates.
 */
public class GenericCellFactory<T, S> implements Callback<TableColumn<T, S>, TableCell<T, S>> {
    EventHandler click;

    public GenericCellFactory(EventHandler click) {
        this.click = click;
    }

    public TableCell<T, S> call(TableColumn<T, S> p) {
        TableCell<T, S> cell = new TableCell<T, S>() {
            @Override
            protected void updateItem(S item, boolean empty) {
                // calling super here is very important - don't skip this!
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.toString());
                }
            }
        };
        // Double click
        if (click != null) {
            cell.setOnMouseClicked(click);
        }
        return cell;
    }
}

package com.aireno.vapas.gui.base;

import com.aireno.utils.ADateUtils;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

import java.util.Date;

/**
 * @author Airenas Vaičiūnas
 * @since 14.1.10 (11.57)
 */
public class DateCellFactory<T> extends GenericCellFactory<T, Date> {
    public DateCellFactory(EventHandler click) {
        super(click);
    }

    public TableCell<T, Date> call(TableColumn<T, Date> p) {
        TableCell<T, Date> cell = new TableCell<T, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                // calling super here is very important - don't skip this!
                super.updateItem(item, empty);
                if (item != null) {
                    setText(ADateUtils.dateToString(item));
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

package com.aireno.vapas.gui.base;

import com.aireno.utils.ANumberUtils;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.12.27
 * Time: 23.15
 * To change this template use File | Settings | File Templates.
 */
public class DecimalCellFactory<T> extends GenericCellFactory<T, BigDecimal> {
    public DecimalCellFactory(EventHandler click) {
        super(click);
    }

    public TableCell<T, BigDecimal> call(TableColumn<T, BigDecimal> p) {
        TableCell<T, BigDecimal> cell = new TableCell<T, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                // calling super here is very important - don't skip this!
                super.updateItem(item, empty);
                if (item != null) {
                    setText(ANumberUtils.decimalToString(item));
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

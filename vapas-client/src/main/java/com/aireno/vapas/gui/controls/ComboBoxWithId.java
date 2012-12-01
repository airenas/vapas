package com.aireno.vapas.gui.controls;

import com.aireno.base.LookupDto;
import javafx.scene.control.ComboBox;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.24
 * Time: 11.41
 * To change this template use File | Settings | File Templates.
 */

public class ComboBoxWithId<T extends LookupDto> extends ComboBox<T> {

    public void setValue(long id) {
        setValue(null);
        List<T> items = this.getItems();
        for (T item: items)
        {
            if (item.getId() == id){
                setValue(item);
                return;
            }
        }
    }
}


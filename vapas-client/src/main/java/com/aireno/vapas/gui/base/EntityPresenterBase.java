
package com.aireno.vapas.gui.base;

import com.aireno.base.DtoBase;
import javafx.event.ActionEvent;

public abstract class EntityPresenterBase<T extends DtoBase> extends PresenterBase {
    protected T item;

    protected Long getId() {
        if (item != null) {
            return item.getId();
        }
        return id;
    }

    protected abstract boolean pakeista();

    @Override
    public boolean needSave() {
        return pakeista();
    }
    public void saugoti(ActionEvent event) {
        try {
            saugotiInt();
            update();
        } catch (Exception e) {
            this.setText(e.getLocalizedMessage());
        }

    }

    @Override
    public void save() throws Exception {
        saugotiInt();
    }

    protected abstract void saugotiInt() throws Exception;

    public void iseiti(ActionEvent event) {
        this.goBack();
    }
}

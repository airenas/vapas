
package com.aireno.vapas.gui.base;

import com.aireno.base.DtoBase;
import com.aireno.dto.ZurnaloVaistasDto;
import javafx.event.ActionEvent;

import java.util.List;

public abstract class EntityPresenterBase<T extends DtoBase> extends PresenterBase {
    protected T item;
    protected boolean initializing = false;

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
            this.setText("Klaida: " + e.getLocalizedMessage());
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

    protected <T extends DtoBase> T rask(List<T> prekes, long id) {
        if (prekes == null)
            return null;
        for (T dto : prekes) {
            if (dto.getId() == id) {
                return dto;
            }
        }
        return null;
    }
}

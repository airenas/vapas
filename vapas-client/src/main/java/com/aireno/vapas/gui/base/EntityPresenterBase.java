
package com.aireno.vapas.gui.base;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.DtoBase;
import com.aireno.base.Processor;
import com.aireno.vapas.gui.main.MainPresenter;
import org.thehecklers.dialogfx.DialogFX;

public abstract class EntityPresenterBase<T extends DtoBase> extends PresenterBase
{
    protected T item;

    protected Long getId() {
        if (item != null){
            return item.getId();
        }
        return id;
    }
}

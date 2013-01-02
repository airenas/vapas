package com.aireno.vapas.gui.base;

import com.aireno.base.LookupDto;
import com.aireno.vapas.gui.controls.FilterLookup;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.12
 * Time: 23.34
 * To change this template use File | Settings | File Templates.
 */
public class LookupFieldDefinitionCB<T, TLookup> extends EditFieldDefinition<T, Long> {
    private final DataProvider<TLookup> provider;

    public LookupFieldDefinitionCB(String name, int size, Callback<TableColumn.CellDataFeatures<T, Long>,
            ObservableValue<Long>> valueFactory, ChangeEvent<T, Long> editEventHandler, DataProvider<TLookup> provider) {
        super(name, size, valueFactory, editEventHandler);
        this.provider = provider;
    }

    @Override
    protected TableCell createCell() {
        return new LookupCellCB(editEventHandler, provider, getSize());
    }

    public interface DataProvider<T> {
        List<T> getItems() throws Exception;
    }
}

class LookupCellCB<T extends LookupDto> extends com.aireno.vapas.gui.controls.EditingCell<T, Long, FilterLookup<T>> {
    private final LookupFieldDefinitionCB.DataProvider<T> provider;
    private final int size;


    public LookupCellCB(EditFieldDefinition.ChangeEvent<T, Long> editEventHandler, LookupFieldDefinitionCB.DataProvider<T> provider
    ,int size) {
        super(editEventHandler);
        this.provider = provider;
        this.size = size;
    }

    @Override
    protected void setFieldValue(Long item) {
        if (item == null) {
            field.setValueId(0);
        } else {
            field.setValueId(item);
        }
    }

    @Override
    protected FilterLookup<T> createFieldInternal() throws Exception {
        FilterLookup<T> result = new FilterLookup<T>();
        result.setEditable(true);
        result.setPrefWidth(size);
        result.setMinWidth(size);// - this.getGraphicTextGap() * 2);

        result.valueProperty().addListener(new ChangeListener<T>(){

            @Override
            public void changed(ObservableValue<? extends T> observableValue, T t, T t2) {
                onChangedTo(field.getValueId());

            }
        });
        result.setData(provider.getItems());
        return result;
    }
}


package com.aireno.vapas.gui.base;

import com.aireno.base.LookupDto;
import com.aireno.dto.StringLookupItemDto;
import com.aireno.vapas.gui.controls.EditingCell;
import com.aireno.vapas.gui.controls.FilterLookup;
import com.aireno.vapas.gui.controls.StringFilterLookup;
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
public class StringLookupFieldDefinitionCB<T> extends EditFieldDefinition<T, String> {
    private final DataProvider provider;

    public StringLookupFieldDefinitionCB(String name, int size, Callback<TableColumn.CellDataFeatures<T, String>,
            ObservableValue<String>> valueFactory, ChangeEvent<T, String> editEventHandler, DataProvider provider) {
        super(name, size, valueFactory, editEventHandler);
        this.provider = provider;
    }

    @Override
    protected TableCell createCell() {
        return new StringLookupCellCB(editEventHandler, provider, getSize());
    }

    public interface DataProvider<T>{
        public abstract List<StringLookupItemDto> getDataList(T item, String sId) throws Exception;
    }
}

class StringLookupCellCB<T extends LookupDto> extends EditingCell<T, String, StringFilterLookup> {
    private final StringLookupFieldDefinitionCB.DataProvider<T> provider;
    private final int size;


    public StringLookupCellCB(EditFieldDefinition.ChangeEvent<T, String> editEventHandler, StringLookupFieldDefinitionCB.DataProvider<T> provider
    ,int size) {
        super(editEventHandler);
        this.provider = provider;
        this.size = size;
    }

    @Override
    protected void setFieldValue(String item) {
        if (item == null) {
            field.setStringValue(null, true);
        } else {
            field.setStringValue(item);
        }
    }

    @Override
    protected StringFilterLookup createFieldInternal() throws Exception {
        StringFilterLookup result = new StringFilterLookup();
        result.setEditable(true);
        result.setPrefWidth(size);
        result.setProvider(new CellDataProvider(this, provider));
        result.setMinWidth(size);// - this.getGraphicTextGap() * 2);

        result.valueProperty().addListener(new ChangeListener<StringLookupItemDto>() {
            @Override
            public void changed(ObservableValue<? extends StringLookupItemDto> observableValue, StringLookupItemDto stringLookupItemDto, StringLookupItemDto stringLookupItemDto2) {
                onChangedTo(field.getStringValue());
            }
        });
        return result;
    }

    class CellDataProvider implements StringFilterLookup.DataProvider {
        final StringLookupCellCB<T> cell;
        final StringLookupFieldDefinitionCB.DataProvider provider;

        CellDataProvider(StringLookupCellCB<T> cell, StringLookupFieldDefinitionCB.DataProvider provider) {
            this.cell = cell;
            this.provider = provider;
        }

        @Override
        public List<StringLookupItemDto> getDataList(String sId) throws Exception {
            return provider.getDataList(cell.getRecord(), sId);
        }
    }

}


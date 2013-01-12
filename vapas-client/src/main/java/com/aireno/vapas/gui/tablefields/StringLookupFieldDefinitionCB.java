package com.aireno.vapas.gui.tablefields;

import com.aireno.base.LookupDto;
import com.aireno.dto.StringLookupItemDto;
import com.aireno.vapas.gui.base.EditFieldDefinition;
import com.aireno.vapas.gui.controls.EditingCell;
import com.aireno.vapas.gui.controls.StringFilterLookup;
import com.aireno.vapas.gui.controls.StringNewLookup;
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
        public abstract List<String> getDataList(T item, String sId) throws Exception;
    }
}

class StringLookupCellCB<T extends LookupDto> extends EditingCell<T, String, StringNewLookup> {
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
    protected StringNewLookup createFieldInternal() throws Exception {
        StringNewLookup result = new StringNewLookup();
        result.setEditable(true);
        result.setPrefWidth(size);
        result.setProvider(new CellDataProvider(this, provider));
        result.setMinWidth(size);// - this.getGraphicTextGap() * 2);

        result.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String stringLookupItemDto, String stringLookupItemDto2) {
                onChangedTo(field.getStringValue());
            }
        });
        return result;
    }

    class CellDataProvider implements StringNewLookup.DataProvider {
        final StringLookupCellCB<T> cell;
        final StringLookupFieldDefinitionCB.DataProvider provider;

        CellDataProvider(StringLookupCellCB<T> cell, StringLookupFieldDefinitionCB.DataProvider provider) {
            this.cell = cell;
            this.provider = provider;
        }

        @Override
        public List<String> getDataList(String sId) throws Exception {
            return provider.getDataList(cell.getRecord(), sId);
        }
    }

}


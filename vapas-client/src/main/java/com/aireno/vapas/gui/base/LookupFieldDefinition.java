package com.aireno.vapas.gui.base;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.base.LookupDto;
import com.aireno.vapas.service.LookupService;
import com.panemu.tiwulfx.common.TableData;
import com.panemu.tiwulfx.control.LookupFieldController;
import com.panemu.tiwulfx.form.LookupControl;
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
public class LookupFieldDefinition<T> extends EditFieldDefinition<T, Long> {
    public LookupFieldDefinition(String name, int size, Callback<TableColumn.CellDataFeatures<T, Long>,
            ObservableValue<Long>> valueFactory, ChangeEvent<T, Long> editEventHandler) {
        super(name, size, valueFactory, editEventHandler);
    }

    @Override
    protected TableCell createCell() {
        return new LookupCell(editEventHandler);
    }
}

class LookupCell<T> extends com.aireno.vapas.gui.controls.EditingCell<T, Long, LookupControl<T>> {

    protected LookupCell(EditFieldDefinition.ChangeEvent<T, Long> editEventHandler) {
        super(editEventHandler);
    }

    protected Long getValue(String s) {
        return Long.parseLong(s);
    }

    @Override
    protected void setFieldValue(Long item) {
        if (item == null) {
            field.setId(null);
        } else {
            field.setId(item.toString());
        }
    }

    @Override
    protected LookupControl<T> createFieldInternal() {
        LookupControl<T> result = new LookupControl<T>();
        result.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        result.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0,
                                Boolean arg1, Boolean arg2) {
                if (!arg2 && field != null) {
                    onChangedTo(getValue(field.getId()));
                }
            }
        });
        result.setController(insuranceLookupController);
        return result;
    }

    private LookupFieldController insuranceLookupController = new LookupFieldController(LookupDto.class) {
        @Override
        public String[] getColumns() {
            return new String[]{
                    "id",
                    "pavadinimas"
            };
        }

        @Override
        protected String getWindowTitle() {
            return "Prekės";
        }

        public LookupService getLookupService() {
            return ApplicationContextProvider.getProvider().getBean(LookupService.class);
        }

        @Override
        protected TableData loadData(int startIndex, List filteredColumns, List sortedColumns, List sortingTypes, int maxResult) {
            List<LookupDto> list = null;

            try {
                list = getLookupService().sarasas(
                        new LookupService.LookupRequest(com.aireno.Constants.LOOKUP_TIEKEJAS));
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            TableData data = new TableData(list, false, list.size());
            return data;
        }
    };
}


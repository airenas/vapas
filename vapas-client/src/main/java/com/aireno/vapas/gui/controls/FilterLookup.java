package com.aireno.vapas.gui.controls;

import com.aireno.base.LookupDto;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.12.23
 * Time: 20.39
 * To change this template use File | Settings | File Templates.
 */
public class FilterLookup<T extends LookupDto> extends ComboBox<T> {
    private ObservableList<T> items;
    private String typed;
    private boolean changing = false;

    private class KeyHandler implements EventHandler<KeyEvent> {

        private SingleSelectionModel<T> sm;

        public KeyHandler() {
            sm = getSelectionModel();
        }

        @Override
        public void handle(KeyEvent event) {
            // handle non alphanumeric keys like backspace, delete etc
            if (event.getCode() == KeyCode.BACK_SPACE && typed.length() > 0)
                typed = typed.substring(0, typed.length() - 1);
            if (event.getCode() == KeyCode.TAB) {
                System.out.println("tab");
                hide();
            }
            System.out.println(typed);
        }

    }

    public FilterLookup() {
        super();
        //setOnKeyReleased(new KeyHandler());
        getEditor().textProperty()
                .addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable,
                                        String oldValue, String newValue) {
                        if (changing) {
                            return;
                        }
                        System.out.println("old " + oldValue);
                        System.out.println("new " + newValue);
                        final TextField editor = getEditor();
                        T selected = null;
                        Object selectedO = getSelectionModel().getSelectedItem();
                        //if (selectedO  T)
                        {
                            selected = (T)selectedO;
                        }
                        if (selected == null || !selected.getPavadinimas().equals(editor.getText())) {
                            show();
                            filterItems(newValue);
                            if (getItems().size() == 0) {
                                editor.setText(newValue);
                            }
                        }
                    }
                });
    }

    public void setData(List<T> items) {
        changing = true;

        try {
            ObservableList<T> data =
                    FXCollections.observableArrayList();
            data.clear();
            for (T v : items) {
                data.add(v);
            }
            setItems(data);
            this.items = data;
            setOnKeyReleased(new KeyHandler());
            setConverter(new StringConverter<T>() {
                @Override
                public String toString(T t) {
                    if (t == null)
                        return null;
                    return t.getPavadinimas();
                }

                @Override
                public T fromString(String s) {
                    return getItem(s);
                }
            });
        } finally {
            changing = false;
        }
    }


    private T getItem(String s) {
        if (s == null)
            return null;
        for (T item : items) {
            if (item.getPavadinimas().toLowerCase().equals(s.toLowerCase())) {
                return item;
            }
        }
        return null;
    }

    public void setValueId(long id) {
        changing = true;
        try {
            setValue(null);
            typed = "";
            java.util.List<T> items = this.getItems();
            for (T item : items) {
                if (item.getId() == id) {
                    typed = item.getPavadinimas();
                    setValue(item);
                    return;
                }
            }
        } finally {
            changing = false;
        }
    }

    private void filterItems(String filter) {
        List<T> filteredItems = new ArrayList<T>();
        for (T item : items) {
            if (item.getPavadinimas().toLowerCase().startsWith(filter.toLowerCase())) {
                filteredItems.add(item);
            }
        }
        setItems(FXCollections.observableArrayList(filteredItems));
    }

    public long getValueId() {
        T result = getValue();
        if (result == null) {
            return 0;
        }
        return result.getId();
    }
}

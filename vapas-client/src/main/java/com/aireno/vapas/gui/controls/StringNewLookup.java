package com.aireno.vapas.gui.controls;

import com.aireno.dto.StringLookupItemDto;
import com.mytdev.javafx.scene.control.AutoCompleteTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.12.23
 * Time: 20.39
 * To change this template use File | Settings | File Templates.
 */
public class StringNewLookup extends ComboBox<String> {
    private boolean changing = false;
    private boolean needReloadList = true;
    private boolean allowNewItem = false;
    private DataProvider provider;
    private List<String> copyitems = new ArrayList<>();

    private class KeyHandler implements EventHandler<KeyEvent> {

        private SingleSelectionModel<String> sm;

        public KeyHandler() {
            sm = getSelectionModel();
        }

        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.TAB) {
                //System.out.println("tab");
                hide();
            }
        }
    }

    public StringNewLookup() {
        super();
        setOnShowing(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (needReloadList) {
                    try {
                        copyitems = provider.getDataList(getStringValue());
                        setItems(FXCollections.observableArrayList(copyitems));
                    } catch (Exception e) {
                        throw new RuntimeException(e);  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
                needReloadList = false;
            }
        });
        setOnHiding(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (allowNewItem && getValue() != null && !getItems().contains(getValue())) {
                    try {
                        getItems().add(getValue());
                    } catch (Exception e) {
                        throw new RuntimeException(e);  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
                needReloadList = false;
            }
        });
        getEditor().textProperty()
                .addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable,
                                        String oldValue, String newValue) {
                        if (changing) {
                            return;
                        }
                        final TextField editor = getEditor();
                        System.out.println(editor.getText());

                        String selected = getSelectionModel().getSelectedItem();
                        if (selected == null || !selected.equals(editor.getText())) {
                            String s = editor.getText();
                            show();
                            filterItems(s);

                            if (getItems().size() == 0)
                            {
                                setValue(s);
                            }
                            //String option = trySelectItem(editor.getText());
                            //getSelectionModel().selectFirst();
                        }
                    }
                });
    }

    private void filterItems(String text) {
        changing = true;
        try {
            List<String> filteredItems = new ArrayList<>();
            for (String item : copyitems) {
                if (StringUtils.startsWithIgnoreCase(item, text)) {
                    filteredItems.add(item);
                }
            }
            setItems(FXCollections.observableArrayList(filteredItems));
        } finally {
            changing = false;
        }
    }

    private String trySelectItem(String s) {
        for (String item : getItems()) {
            if (item.toLowerCase().startsWith(s.toLowerCase())) {
                return item;
            }
        }
        return s;
    }

    public void setProvider(DataProvider provider) {
        changing = true;
        this.provider = provider;
        needReloadList = true;
        try {
            setOnKeyReleased(new KeyHandler());
            setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                @Override
                public ListCell<String> call(ListView<String> p) {
                    return new ListCell<String>() {
                        {
                            setContentDisplay(ContentDisplay.TEXT_ONLY);
                        }

                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);

                                setText(item);

                        }
                    };
                }
            });
           /* setConverter(new StringConverter<StringLookupItemDto>() {
                @Override
                public String toString(StringLookupItemDto t) {
                    if (t == null)
                        return null;
                    return t.getId();
                }

                @Override
                public StringLookupItemDto fromString(String s) {
                    return getItem(s);
                }
            });*/
        } finally {
            changing = false;
        }
    }

    public interface DataProvider {
        List<String> getDataList(String sId) throws Exception;
    }

    public void setStringValue(String id, boolean needReloadList) {
        changing = true;
        this.needReloadList = needReloadList;
        try {
             setValue(id);
             return;
        } finally {
            changing = false;
        }
    }

    public void setStringValue(String id) {
        changing = true;
        try {

            setValue(id);

        } finally {
            changing = false;
        }
    }

    public String getStringValue() {
        return getValue();
    }
}

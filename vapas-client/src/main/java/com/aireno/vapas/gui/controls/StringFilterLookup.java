package com.aireno.vapas.gui.controls;

import com.aireno.dto.StringLookupItemDto;
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

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.12.23
 * Time: 20.39
 * To change this template use File | Settings | File Templates.
 */
public class StringFilterLookup extends ComboBox<StringLookupItemDto> {
    private boolean changing = false;
    private boolean needReloadList = true;
    private boolean allowNewItem = false;
    private boolean addedFake = false;
    private DataProvider provider;

    public boolean isAllowNewItem() {
        return allowNewItem;
    }

    public void setAllowNewItem(boolean allowNewItem) {
        this.allowNewItem = allowNewItem;
    }

    private class KeyHandler implements EventHandler<KeyEvent> {

        private SingleSelectionModel<StringLookupItemDto> sm;

        public KeyHandler() {
            sm = getSelectionModel();
        }

        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.TAB) {
                System.out.println("tab");
                hide();
            }
        }
    }

    public StringFilterLookup() {
        super();
        setOnShowing(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (needReloadList) {
                    try {
                        setItems(FXCollections.observableArrayList(provider.getDataList(getStringValue())));
                        addedFake = false;
                    } catch (Exception e) {
                        throw new RuntimeException(e);  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
                addedFake = false;
                needReloadList = false;
            }
        });
        setOnHiding(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (allowNewItem && getValue() != null && !getItems().contains(getValue())) {
                    try {
                        getItems().add(getValue());
                        addedFake = true;
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
                        System.out.println("old " + oldValue);
                        System.out.println("new " + newValue);
                        final TextField editor = getEditor();
                        StringLookupItemDto selected = null;
                        Object selectedO = getSelectionModel().getSelectedItem();
                        if (selectedO instanceof StringLookupItemDto)
                        {
                            selected = (StringLookupItemDto)selectedO;
                        }
                        if (selected == null || !selected.getId().equals(editor.getText())) {
                            //filterItems(newValue);
                            show();
                            if (getItems().size() == 1) {
                                final StringLookupItemDto onlyOption = getItems().get(0);
                                final String current = editor.getText();
                            }
                            StringLookupItemDto option = trySelectItem(editor.getText());
                            setValue(option);
                        }
                    }
                });
    }

    private StringLookupItemDto trySelectItem(String s) {
        if (StringUtils.isEmpty(s))
            return null;
        for (StringLookupItemDto item : getItems()) {
            if (StringUtils.startsWith(item.getId().toLowerCase(), s.toLowerCase())) {
                return item;
            }
        }

        if (allowNewItem)
        {
            StringLookupItemDto item = new StringLookupItemDto(s);
            return item;
        }

        return null;
    }

    public void setProvider(DataProvider provider) {
        changing = true;
        this.provider = provider;
        try {
            setOnKeyReleased(new KeyHandler());
            setCellFactory(new Callback<ListView<StringLookupItemDto>, ListCell<StringLookupItemDto>>() {
                @Override
                public ListCell<StringLookupItemDto> call(ListView<StringLookupItemDto> p) {
                    return new ListCell<StringLookupItemDto>() {
                        {
                            setContentDisplay(ContentDisplay.TEXT_ONLY);
                        }

                        @Override
                        protected void updateItem(StringLookupItemDto item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item == null || empty) {
                                setText(null);
                            } else {
                                setText(item.getPavadinimas());
                            }
                        }
                    };
                }
            });
            setConverter(new StringConverter<StringLookupItemDto>() {
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
            });
        } finally {
            changing = false;
        }
    }

    public interface DataProvider {
        List<StringLookupItemDto> getDataList(String sId) throws Exception;
    }


    private StringLookupItemDto getItem(String s) {
        if (s == null)
            return null;
        for (StringLookupItemDto item : getItems()) {
            if (StringUtils.equalsIgnoreCase(item.getId(), s) ||StringUtils.equalsIgnoreCase(item.getPavadinimas(), s)) {
                return item;
            }
        }
        return null;
    }

    public void setStringValue(String id, boolean needReloadList) {
        changing = true;
        this.needReloadList = needReloadList;
        try {
            List<StringLookupItemDto> items = this.getItems();
            for (StringLookupItemDto item : items) {
                if (item.getId() == id) {
                    setValue(item);
                    return;
                }
            }
            StringLookupItemDto item = new StringLookupItemDto(id);
            setValue(item);

        } finally {
            changing = false;
        }
    }

    public void setStringValue(String id) {
        changing = true;
        try {
            if (StringUtils.isEmpty(id)) {
                setValue(null);
                return;
            }

            List<StringLookupItemDto> items = this.getItems();
            for (StringLookupItemDto item : items) {
                if (StringUtils.equals(item.getId(), id)) {
                    if (getValue() != item) {
                        setValue(item);
                    }
                    return;
                }
            }

            StringLookupItemDto item = new StringLookupItemDto(id);
            setValue(item);

        } finally {
            changing = false;
        }
    }

    public String getStringValue() {
        StringLookupItemDto result = getValue();
        if (result == null) {
            return null;
        }
        return result.getId();
    }
}

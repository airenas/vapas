package com.aireno.vapas.gui.base;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;


/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.12
 * Time: 23.34
 * To change this template use File | Settings | File Templates.
 */
public class EditFieldDefinition<T, S> extends FieldDefinition<T, S> {
    public EditFieldDefinition(String name, int size, Callback<TableColumn.CellDataFeatures<T, S>, ObservableValue<S>> valueFactory) {
        super(name, size, valueFactory);
    }

    public TableColumn<T, S> addToTable(ListDefinition<T> tListDefinition, TableView<T> tableView) {
        TableColumn<T, S> col = new TableColumn<T, S>(getName());
        col.prefWidthProperty().setValue(getSize());
        col.setCellValueFactory(getValueFactory());

        Callback<TableColumn, TableCell> cellFactory =
                new Callback<TableColumn, TableCell>() {
                    public TableCell call(TableColumn p) {
                        return new EditingCell();
                    }
                };

        Callback<TableColumn<T, S>, TableCell<T, S>> editableFactory = new Callback<TableColumn<T, S>, TableCell<T, S>>() {
            @Override
            public TableCell call(TableColumn p) {
                return new EditingCell<T,S>();
            }
        };
        col.setEditable(true);
        col.setCellFactory(editableFactory);
        tableView.getColumns().add(col);
        return col;
    }


    class EditingCell<T, S> extends TableCell<T, S> {

        private TextField textField;

        public EditingCell() {}

        @Override
        public void startEdit() {
            super.startEdit();

            if (textField == null) {
                createTextField();
            }

            setGraphic(textField);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(String.valueOf(getItem()));
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }

        @Override
        public void updateItem(S item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setGraphic(textField);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                } else {
                    setText(getString());
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()*2);
            textField.setOnKeyPressed(new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent t) {
                    if (t.getCode() == KeyCode.ENTER) {
                        commitEdit((S)textField.getText());
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
}

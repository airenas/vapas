package com.aireno.vapas.gui.base;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.13
 * Time: 00.25
 * To change this template use File | Settings | File Templates.
 */
public abstract class FPropertyValueFactory<S, T>   implements
        javafx.util.Callback<javafx.scene.control.TableColumn.CellDataFeatures<S,T>,
                javafx.beans.value.ObservableValue<T>>{
    private java.lang.Class columnClass;
    private java.lang.String previousProperty;
    private com.sun.javafx.property.PropertyReference<T> propertyRef;

    public javafx.beans.value.ObservableValue<T> call(javafx.scene.control.TableColumn.CellDataFeatures<S,T> stCellDataFeatures) {
        T value = getValueInt(stCellDataFeatures.getValue());
        return new  FObservableValue(value);
    }

    public final java.lang.String getProperty() {
        return "oha";
    }

    protected abstract T getValueInt(S value);
}

class FObservableValue<T>   implements
        javafx.beans.value.ObservableValue<T>{
    private java.lang.Class columnClass;

    T value;

    public FObservableValue(T value) {
        this.value = value;
    }

    @Override
    public void addListener(ChangeListener<? super T> changeListener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeListener(ChangeListener<? super T> changeListener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public T getValue() {
        return value;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

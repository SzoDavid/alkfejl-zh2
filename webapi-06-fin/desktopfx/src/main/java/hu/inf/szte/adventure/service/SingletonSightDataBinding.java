package hu.inf.szte.adventure.service;

import hu.inf.szte.adventure.model.Sight;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;

public class SingletonSightDataBinding {

    private final ListProperty<Sight> bindingList;

    private SingletonSightDataBinding() {
        bindingList = new SimpleListProperty<>();
    }

    public static SingletonSightDataBinding getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public ListProperty<Sight> bindingListProperty() {
        return bindingList;
    }

    private static final class SingletonInstance {

        private static final SingletonSightDataBinding INSTANCE = new SingletonSightDataBinding();
    }
}

package hu.inf.szte.adventure.service;

import hu.inf.szte.adventure.model.Trip;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;

public class SingletonTripDataBinding {

    private final ListProperty<Trip> bindingList;

    private SingletonTripDataBinding() {
        bindingList = new SimpleListProperty<>();
    }

    public static SingletonTripDataBinding getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public ListProperty<Trip> bindingListProperty() {
        return bindingList;
    }

    private static final class SingletonInstance {

        private static final SingletonTripDataBinding INSTANCE = new SingletonTripDataBinding();
    }
}

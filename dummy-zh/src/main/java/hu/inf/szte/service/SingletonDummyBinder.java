package hu.inf.szte.service;

import hu.inf.szte.model.Dummy;
import javafx.beans.property.SimpleListProperty;

public class SingletonDummyBinder {

    private final SimpleListProperty<Dummy> dataBinding;

    private SingletonDummyBinder() {
        dataBinding = new SimpleListProperty<>();
    }

    public static SingletonDummyBinder getInstance() {
        return BinderInstance.INSTANCE;
    }

    public SimpleListProperty<Dummy> dataProperty() {
        return dataBinding;
    }

    private static class BinderInstance {
        private static final SingletonDummyBinder INSTANCE = new SingletonDummyBinder();
    }
}

package hu.inf.szte.data;

import lombok.NonNull;

import java.util.List;

public interface Dao<T> {

    // Saves model to a persistent underlying storage mechanism.
    void save(@NonNull T model);
    // Fetch all record from persistent storage.
    List<T> findAll();
    // Fetch all record by a given criterium.
    // The criterium is given by a model object,
    // where the fields are concatenated by AND operator.
    List<T> findAllByCrit(@NonNull T model);
}

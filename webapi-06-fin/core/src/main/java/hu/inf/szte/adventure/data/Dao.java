package hu.inf.szte.adventure.data;

import lombok.NonNull;

import java.util.Optional;

/**
 * Data Access Objects implementing this interface should
 * implement all the methods listed here. This DAO interface
 * could be used to communicate with, and manage the underlying
 * persistent storage.
 *
 * @param <ID> Type parameter of the primary key of stored models
 * @param <T>  Type of persisted models
 */
public interface Dao<ID, T> {

    /**
     * Persists a model based on the underlying implementation choice.
     *
     * @param model The model to be persisted
     */
    void save(@NonNull T model);

    /**
     * Fetches a model from the database by its primary key.
     *
     * @param id The primary key of the persisted model
     * @return {@link Optional#of(T)} the persisted model if found,
     * else {@link Optional#empty()}
     */
    Optional<T> findById(@NonNull ID id);

    /**
     * Fetches all records from the underlying storage.
     *
     * @return An {@link Iterable<T>} of stored models, e.g. {@link java.util.ArrayList<T>}
     */
    Iterable<T> findAll();

    /**
     * Fetches all records that match at least one of the ids provided.
     * Condition on ids are concatenated by OR operation.
     *
     * @return An {@link Iterable<T>} of stored models, e.g. {@link java.util.ArrayList<T>}
     */
    Iterable<T> findAllByIds(@NonNull Iterable<ID> ids);

    /**
     * Fetches all records that match the criteria provided by the model.
     * Null fields are ignored, and conditions are concatenated by AND operation.
     *
     * @param model Filtering criteria given by the field values
     * @return An {@link Iterable<T>} of stored models, e.g. {@link java.util.ArrayList<T>}
     */
    Iterable<T> findAllByCrit(@NonNull T model);

    /**
     * Delete a record by the id specified.
     *
     * @param id The primary key of model to delete
     * @return Number of affected rows, 0 if no rows deleted,
     * should be 1 otherwise
     */
    int deleteById(@NonNull ID id);

    /**
     * Delete all records by the specified primary keys (id).
     * If no ids are specified this operation should STOP,
     * and not prune the entire table. At least one id is required.
     * The condition of deletion will be made by concatenated OR operations.
     *
     * @param ids List of ids to delete
     * @return Number of affected rows, 0 if no rows deleted,
     * >0 otherwise
     */
    int deleteAllByIds(@NonNull Iterable<ID> ids);

    /**
     * Updates a record met by the id criteria.
     * Only non-null fields will be considered for a field
     * update. If all fields in the model are null,
     * then no update will be performed, even if a valid
     * id is given.
     *
     * @param id    The primary key of model to update
     * @param model The model to update by
     * @return Modified rows count, 0 if no rows were affected,
     * should be 1 otherwise
     */
    int updateById(@NonNull ID id, T model);

    /**
     * Updates all records met by the id criteria.
     * The condition for update is expressed by an OR
     * operation, so that any matching key should be updated.
     * In case an empty list of ids is passed, all records
     * should be updated.
     * Only non-null fields will be considered for a field
     * update. If all fields in the model are null,
     * then no update will be performed.
     *
     * @param ids   The collection of primary keys to update
     * @param model The model to update by
     * @return Modified rows count, 0 if no rows were affected,
     * >0 otherwise
     */
    int updateAllByIds(@NonNull Iterable<ID> ids, @NonNull T model);

    /**
     * Deletes all rows from the underlying database.
     *
     * @return Number of deleted rows
     */
    int prune();

    /**
     * Counts records in the persistent storage.
     *
     * @return Row count
     */
    int count();
}

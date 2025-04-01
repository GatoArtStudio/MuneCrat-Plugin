package com.github.gatoartstudios.munecraft.core.interfaces;

import java.util.List;

/**
 * This interface provides the basic CRUD operations (Create, Read, Update,
 * Delete) to interact with a database.
 *
 * @param <T> The type of object to be stored in the database.
 */
public interface ICrud<T> {
    /**
     * Creates a new record in the database.
     *
     * @param entity The object to be stored in the database.
     */
    void create(T entity);

    /**
     * Retrieves a record from the database by its ID.
     *
     * @param id The ID of the record to be retrieved.
     * @return The object retrieved from the database.
     */
    T read(long id);

    /**
     * Updates an existing record in the database.
     *
     * @param entity The object with the updated information.
     */
    void update(T entity);

    /**
     * Deletes a record from the database by its ID.
     *
     * @param id The ID of the record to be deleted.
     */
    void delete(long id);

    /**
     * Retrieves all records from the database.
     *
     * @return A list of objects retrieved from the database.
     */
    List<T> getAll();
}
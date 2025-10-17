package com.library.domain.repository;

import com.library.domain.model.BaseEntity;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Generic repository interface for data access operations.
 * Pure OOP implementation without any framework dependencies.
 *
 * @param <T> The entity type
 * @param <ID> The ID type
 */
public interface Repository<T extends BaseEntity, ID> {
    /**
     * Saves an entity.
     * @param entity The entity to save
     * @return The saved entity
     */
    T save(T entity);

    /**
     * Saves multiple entities.
     * @param entities The entities to save
     * @return The saved entities
     */
    List<T> saveAll(Iterable<T> entities);

    /**
     * Finds an entity by ID.
     * @param id The ID to search for
     * @return The entity, or empty if not found
     */
    Optional<T> findById(ID id);

    /**
     * Checks if an entity exists by ID.
     * @param id The ID to check
     * @return true if the entity exists, false otherwise
     */
    boolean existsById(ID id);

    /**
     * Finds all entities.
     * @return A list of all entities
     */
    List<T> findAll();

    /**
     * Finds all entities by IDs.
     * @param ids The IDs to search for
     * @return A list of entities
     */
    List<T> findAllById(Iterable<ID> ids);

    /**
     * Counts all entities.
     * @return The number of entities
     */
    long count();

    /**
     * Deletes an entity by ID.
     * @param id The ID of the entity to delete
     */
    void deleteById(ID id);

    /**
     * Deletes an entity.
     * @param entity The entity to delete
     */
    void delete(T entity);

    /**
     * Deletes multiple entities.
     * @param entities The entities to delete
     */
    void deleteAll(Iterable<T> entities);

    /**
     * Deletes all entities.
     */
    void deleteAll();

    /**
     * Soft deletes an entity.
     * @param entity The entity to soft delete
     */
    void softDelete(T entity);

    /**
     * Soft deletes an entity by ID.
     * @param id The ID of the entity to soft delete
     */
    void softDeleteById(ID id);

    /**
     * Finds all active (not deleted) entities.
     * @return A list of active entities
     */
    List<T> findAllActive();

    /**
     * Finds all deleted entities.
     * @return A list of deleted entities
     */
    List<T> findAllDeleted();

    /**
     * Finds entities using a predicate.
     * @param predicate The predicate to filter entities
     * @return A list of matching entities
     */
    List<T> findAll(java.util.function.Predicate<T> predicate);
}

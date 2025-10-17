package com.library.infrastructure.repository;

import com.library.domain.model.BaseEntity;
import com.library.domain.repository.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of the Repository interface.
 * Uses ConcurrentHashMap for thread-safe operations.
 * Pure OOP implementation without any framework dependencies.
 *
 * @param <T> The entity type
 * @param <ID> The ID type
 */
public abstract class InMemoryRepository<T extends BaseEntity, ID> implements Repository<T, ID> {
    protected final Map<ID, T> store;

    public InMemoryRepository() {
        this.store = new ConcurrentHashMap<>();
    }

    @Override
    public T save(T entity) {
        Objects.requireNonNull(entity, "Entity cannot be null");
        ID id = extractId(entity);
        store.put(id, entity);
        return entity;
    }

    @Override
    public List<T> saveAll(Iterable<T> entities) {
        Objects.requireNonNull(entities, "Entities cannot be null");
        List<T> saved = new ArrayList<>();
        for (T entity : entities) {
            saved.add(save(entity));
        }
        return saved;
    }

    @Override
    public Optional<T> findById(ID id) {
        Objects.requireNonNull(id, "ID cannot be null");
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public boolean existsById(ID id) {
        Objects.requireNonNull(id, "ID cannot be null");
        return store.containsKey(id);
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        Objects.requireNonNull(ids, "IDs cannot be null");
        List<T> result = new ArrayList<>();
        for (ID id : ids) {
            Optional<T> entity = findById(id);
            entity.ifPresent(result::add);
        }
        return result;
    }

    @Override
    public long count() {
        return store.size();
    }

    @Override
    public void deleteById(ID id) {
        Objects.requireNonNull(id, "ID cannot be null");
        store.remove(id);
    }

    @Override
    public void delete(T entity) {
        Objects.requireNonNull(entity, "Entity cannot be null");
        ID id = extractId(entity);
        store.remove(id);
    }

    @Override
    public void deleteAll(Iterable<T> entities) {
        Objects.requireNonNull(entities, "Entities cannot be null");
        for (T entity : entities) {
            delete(entity);
        }
    }

    @Override
    public void deleteAll() {
        store.clear();
    }

    @Override
    public void softDelete(T entity) {
        Objects.requireNonNull(entity, "Entity cannot be null");
        entity.markAsDeleted();
        save(entity);
    }

    @Override
    public void softDeleteById(ID id) {
        Objects.requireNonNull(id, "ID cannot be null");
        Optional<T> entity = findById(id);
        entity.ifPresent(this::softDelete);
    }

    @Override
    public List<T> findAllActive() {
        return store.values().stream()
            .filter(entity -> !entity.isDeleted())
            .collect(Collectors.toList());
    }

    @Override
    public List<T> findAllDeleted() {
        return store.values().stream()
            .filter(BaseEntity::isDeleted)
            .collect(Collectors.toList());
    }

    @Override
    public List<T> findAll(java.util.function.Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "Predicate cannot be null");
        return store.values().stream()
            .filter(predicate)
            .collect(Collectors.toList());
    }

    /**
     * Extracts the ID from an entity.
     * Subclasses should override this method to provide the appropriate ID extraction logic.
     *
     * @param entity The entity to extract the ID from
     * @return The ID of the entity
     */
    protected abstract ID extractId(T entity);
}

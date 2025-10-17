package com.library.infrastructure.persistence;

import com.library.domain.model.BaseEntity;
import com.library.domain.repository.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of the Repository interface.
 * Stores entities in a concurrent map for thread-safe access.
 * Pure OOP implementation without any framework dependencies.
 *
 * @param <T> The entity type
 * @param <ID> The ID type
 */
public class InMemoryRepository<T extends BaseEntity, ID> implements Repository<T, ID> {
    private final Map<ID, T> store;
    private final List<RepositoryListener<T>> listeners;

    public InMemoryRepository() {
        this.store = new ConcurrentHashMap<>();
        this.listeners = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public T save(T entity) {
        Objects.requireNonNull(entity, "Entity cannot be null");
        
        @SuppressWarnings("unchecked")
        ID id = (ID) entity.getId();
        
        T saved = store.put(id, entity);
        notifyEntitySaved(entity);
        
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
        
        T removed = store.remove(id);
        if (removed != null) {
            notifyEntityDeleted(removed);
        }
    }

    @Override
    public void delete(T entity) {
        Objects.requireNonNull(entity, "Entity cannot be null");
        
        @SuppressWarnings("unchecked")
        ID id = (ID) entity.getId();
        deleteById(id);
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
        notifyRepositoryCleared();
    }

    @Override
    public void softDelete(T entity) {
        Objects.requireNonNull(entity, "Entity cannot be null");
        
        entity.markAsDeleted();
        save(entity);
        notifyEntitySoftDeleted(entity);
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
     * Adds a listener for repository events.
     * @param listener The listener to add
     */
    public void addListener(RepositoryListener<T> listener) {
        listeners.add(Objects.requireNonNull(listener, "Listener cannot be null"));
    }

    /**
     * Removes a listener for repository events.
     * @param listener The listener to remove
     */
    public void removeListener(RepositoryListener<T> listener) {
        listeners.remove(listener);
    }

    /**
     * Gets the number of entities in the repository.
     * @return The number of entities
     */
    public int size() {
        return store.size();
    }

    /**
     * Clears the repository.
     */
    public void clear() {
        store.clear();
    }

    private void notifyEntitySaved(T entity) {
        for (RepositoryListener<T> listener : listeners) {
            try {
                listener.onEntitySaved(entity);
            } catch (Exception e) {
                // Ignore listener errors
            }
        }
    }

    private void notifyEntityDeleted(T entity) {
        for (RepositoryListener<T> listener : listeners) {
            try {
                listener.onEntityDeleted(entity);
            } catch (Exception e) {
                // Ignore listener errors
            }
        }
    }

    private void notifyEntitySoftDeleted(T entity) {
        for (RepositoryListener<T> listener : listeners) {
            try {
                listener.onEntitySoftDeleted(entity);
            } catch (Exception e) {
                // Ignore listener errors
            }
        }
    }

    private void notifyRepositoryCleared() {
        for (RepositoryListener<T> listener : listeners) {
            try {
                listener.onRepositoryCleared();
            } catch (Exception e) {
                // Ignore listener errors
            }
        }
    }

    /**
     * Interface for listening to repository events.
     */
    public interface RepositoryListener<T> {
        void onEntitySaved(T entity);
        void onEntityDeleted(T entity);
        void onEntitySoftDeleted(T entity);
        void onRepositoryCleared();
    }
}

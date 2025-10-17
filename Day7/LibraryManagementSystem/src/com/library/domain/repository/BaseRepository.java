package com.library.domain.repository;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T, ID> {
    void save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void deleteById(ID id);
    void update(T entity);
    long count();
}

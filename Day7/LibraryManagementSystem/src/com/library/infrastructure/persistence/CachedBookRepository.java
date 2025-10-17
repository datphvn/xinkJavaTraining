package com.library.infrastructure.persistence;

import com.library.domain.model.Book;
import com.library.domain.repository.BookRepository;
import com.library.domain.valueobject.ISBN;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cached implementation of BookRepository.
 * Provides caching layer on top of the underlying repository.
 * Pure OOP implementation without any framework dependencies.
 */
public class CachedBookRepository implements BookRepository {
    private final BookRepository inner;
    private final java.util.Map<UUID, Book> cache;

    public CachedBookRepository(BookRepository inner) {
        this.inner = inner;
        this.cache = new ConcurrentHashMap<>();
    }

    @Override
    public Book save(Book entity) {
        Book saved = inner.save(entity);
        cache.put(saved.getId(), saved);
        return saved;
    }

    @Override
    public List<Book> saveAll(Iterable<Book> entities) {
        List<Book> saved = inner.saveAll(entities);
        for (Book book : saved) {
            cache.put(book.getId(), book);
        }
        return saved;
    }

    @Override
    public Optional<Book> findById(UUID id) {
        Book cached = cache.get(id);
        if (cached != null) {
            return Optional.of(cached);
        }
        Optional<Book> book = inner.findById(id);
        book.ifPresent(b -> cache.put(id, b));
        return book;
    }

    @Override
    public boolean existsById(UUID id) {
        if (cache.containsKey(id)) {
            return true;
        }
        return inner.existsById(id);
    }

    @Override
    public List<Book> findAll() {
        return inner.findAll();
    }

    @Override
    public List<Book> findAllById(Iterable<UUID> ids) {
        return inner.findAllById(ids);
    }

    @Override
    public long count() {
        return inner.count();
    }

    @Override
    public void deleteById(UUID id) {
        inner.deleteById(id);
        cache.remove(id);
    }

    @Override
    public void delete(Book entity) {
        inner.delete(entity);
        cache.remove(entity.getId());
    }

    @Override
    public void deleteAll(Iterable<Book> entities) {
        inner.deleteAll(entities);
        for (Book entity : entities) {
            cache.remove(entity.getId());
        }
    }

    @Override
    public void deleteAll() {
        inner.deleteAll();
        cache.clear();
    }

    @Override
    public void softDelete(Book entity) {
        inner.softDelete(entity);
        cache.put(entity.getId(), entity);
    }

    @Override
    public void softDeleteById(UUID id) {
        inner.softDeleteById(id);
        Optional<Book> book = inner.findById(id);
        book.ifPresent(b -> cache.put(id, b));
    }

    @Override
    public List<Book> findAllActive() {
        return inner.findAllActive();
    }

    @Override
    public List<Book> findAllDeleted() {
        return inner.findAllDeleted();
    }

    @Override
    public List<Book> findAll(java.util.function.Predicate<Book> predicate) {
        return inner.findAll(predicate);
    }

    @Override
    public Optional<Book> findByIsbn(ISBN isbn) {
        // Search cache first
        Optional<Book> cached = cache.values().stream()
            .filter(book -> book.getIsbn().equals(isbn))
            .findFirst();
        
        if (cached.isPresent()) {
            return cached;
        }
        
        Optional<Book> book = inner.findByIsbn(isbn);
        book.ifPresent(b -> cache.put(b.getId(), b));
        return book;
    }

    @Override
    public List<Book> findByTitle(String title) {
        return inner.findByTitle(title);
    }

    @Override
    public List<Book> findByAuthor(String authorName) {
        return inner.findByAuthor(authorName);
    }

    @Override
    public List<Book> findByCategory(String categoryName) {
        return inner.findByCategory(categoryName);
    }

    @Override
    public List<Book> findAllAvailable() {
        return inner.findAllAvailable();
    }

    @Override
    public long countAvailableCopies() {
        return inner.countAvailableCopies();
    }

    /**
     * Clears the cache.
     */
    public void clearCache() {
        cache.clear();
    }

    /**
     * Gets the cache size.
     * @return The number of items in the cache
     */
    public int getCacheSize() {
        return cache.size();
    }
}

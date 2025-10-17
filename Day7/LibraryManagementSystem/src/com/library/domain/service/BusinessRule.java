package com.library.domain.service;

import com.library.domain.exception.LibraryException;

/**
 * Interface for implementing business rules.
 * Business rules encapsulate domain logic that can be applied to entities.
 * Pure OOP implementation without any framework dependencies.
 *
 * @param <T> The type of entity this rule applies to
 */
public interface BusinessRule<T> {
    /**
     * Checks if this rule applies to the given entity.
     * @param entity The entity to check
     * @return true if this rule should be executed, false otherwise
     */
    boolean applies(T entity);

    /**
     * Executes the business rule.
     * @param entity The entity to apply the rule to
     * @throws LibraryException if the rule is violated
     */
    void execute(T entity) throws LibraryException;

    /**
     * Gets a description of this rule.
     * @return A human-readable description
     */
    String getDescription();

    /**
     * Gets the priority of this rule.
     * Higher priority rules are executed first.
     * @return The priority value
     */
    int getPriority();

    /**
     * Gets the name of this rule.
     * @return The rule name
     */
    String getName();
}

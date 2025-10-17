package com.library.domain.service;

import java.util.Map;

/**
 * Context for workflow execution.
 * Holds data that flows through the workflow steps.
 * Pure OOP implementation without any framework dependencies.
 */
public class WorkflowContext {
    private final Map<String, Object> data;

    public WorkflowContext() {
        this.data = new java.util.HashMap<>();
    }

    /**
     * Puts a value into the context.
     * @param key The key to store the value under
     * @param value The value to store
     */
    public <T> void put(String key, T value) {
        data.put(key, value);
    }

    /**
     * Gets a value from the context with type checking.
     * @param key The key to retrieve
     * @param type The expected type
     * @return The value, or null if not found
     * @throws ClassCastException if the value is not of the expected type
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        Object value = data.get(key);
        if (value == null) {
            return null;
        }
        if (!type.isInstance(value)) {
            throw new ClassCastException("Expected " + type.getName() + " but got " + value.getClass().getName());
        }
        return (T) value;
    }

    /**
     * Gets a value from the context as an Optional.
     * @param key The key to retrieve
     * @param type The expected type
     * @return An Optional containing the value, or empty if not found or type mismatch
     */
    public <T> java.util.Optional<T> getOptional(String key, Class<T> type) {
        try {
            return java.util.Optional.ofNullable(get(key, type));
        } catch (ClassCastException e) {
            return java.util.Optional.empty();
        }
    }

    /**
     * Checks if a key exists in the context.
     * @param key The key to check
     * @return true if the key exists, false otherwise
     */
    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    /**
     * Clears all data from the context.
     */
    public void clear() {
        data.clear();
    }

    /**
     * Gets a copy of all data in the context.
     * @return A copy of the data map
     */
    public Map<String, Object> getData() {
        return new java.util.HashMap<>(data);
    }
}

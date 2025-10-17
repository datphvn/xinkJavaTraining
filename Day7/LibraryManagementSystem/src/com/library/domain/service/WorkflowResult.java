package com.library.domain.service;

import java.util.Map;

/**
 * Result of a workflow execution.
 * Encapsulates the outcome of a workflow including success status, message, and result data.
 * Pure OOP implementation without any framework dependencies.
 */
public class WorkflowResult {
    private final boolean success;
    private final String message;
    private final Map<String, Object> resultData;
    private final Exception exception;

    /**
     * Private constructor to enforce use of factory methods.
     */
    private WorkflowResult(boolean success, String message, Map<String, Object> resultData, Exception exception) {
        this.success = success;
        this.message = message;
        this.resultData = new java.util.HashMap<>(resultData);
        this.exception = exception;
    }

    /**
     * Creates a successful workflow result with a message.
     * @param message The success message
     * @return A successful WorkflowResult
     */
    public static WorkflowResult success(String message) {
        return new WorkflowResult(true, message, new java.util.HashMap<>(), null);
    }

    /**
     * Creates a successful workflow result with a message and result data.
     * @param message The success message
     * @param resultData The result data from the workflow
     * @return A successful WorkflowResult
     */
    public static WorkflowResult success(String message, Map<String, Object> resultData) {
        return new WorkflowResult(true, message, resultData, null);
    }

    /**
     * Creates a failed workflow result with a message.
     * @param message The failure message
     * @return A failed WorkflowResult
     */
    public static WorkflowResult failure(String message) {
        return new WorkflowResult(false, message, new java.util.HashMap<>(), null);
    }

    /**
     * Creates a failed workflow result with a message and exception.
     * @param message The failure message
     * @param exception The exception that caused the failure
     * @return A failed WorkflowResult
     */
    public static WorkflowResult failure(String message, Exception exception) {
        return new WorkflowResult(false, message, new java.util.HashMap<>(), exception);
    }

    /**
     * Checks if the workflow execution was successful.
     * @return true if successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Gets the message from the workflow result.
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets a copy of the result data.
     * @return A copy of the result data map
     */
    public Map<String, Object> getResultData() {
        return new java.util.HashMap<>(resultData);
    }

    /**
     * Gets the exception that caused the failure, if any.
     * @return The exception, or null if no exception occurred
     */
    public Exception getException() {
        return exception;
    }

    /**
     * Gets a specific result value with type casting.
     * @param key The key of the result value
     * @param type The expected type
     * @return The result value, or null if not found
     */
    public <T> T getResult(String key, Class<T> type) {
        Object value = resultData.get(key);
        if (value == null) {
            return null;
        }
        return type.cast(value);
    }

    @Override
    public String toString() {
        return "WorkflowResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", resultData=" + resultData +
                ", exception=" + exception +
                '}';
    }
}

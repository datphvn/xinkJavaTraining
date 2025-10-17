package com.library.domain.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Exception thrown when entity validation fails.
 * Collects multiple field errors.
 */
public class ValidationException extends LibraryException {
    private final Map<String, String> fieldErrors;

    /**
     * Constructor with a single error message.
     * @param message The error message
     */
    public ValidationException(String message) {
        super("VALIDATION_ERROR", message);
        this.fieldErrors = new HashMap<>();
    }

    /**
     * Constructor with field errors.
     * @param fieldErrors Map of field names to error messages
     */
    public ValidationException(Map<String, String> fieldErrors) {
        super("VALIDATION_ERROR", "Validation failed with " + fieldErrors.size() + " error(s)");
        this.fieldErrors = new HashMap<>(Objects.requireNonNull(fieldErrors, "Field errors cannot be null"));
    }

    /**
     * Constructor with message and field errors.
     * @param message The error message
     * @param fieldErrors Map of field names to error messages
     */
    public ValidationException(String message, Map<String, String> fieldErrors) {
        super("VALIDATION_ERROR", message);
        this.fieldErrors = new HashMap<>(Objects.requireNonNull(fieldErrors, "Field errors cannot be null"));
    }

    /**
     * Constructor with message and cause.
     * @param message The error message
     * @param cause The cause of the exception
     */
    public ValidationException(String message, Throwable cause) {
        super("VALIDATION_ERROR", message, cause);
        this.fieldErrors = new HashMap<>();
    }

    public Map<String, String> getFieldErrors() {
        return new HashMap<>(fieldErrors);
    }

    public void addFieldError(String fieldName, String errorMessage) {
        fieldErrors.put(fieldName, errorMessage);
    }

    public boolean hasFieldErrors() {
        return !fieldErrors.isEmpty();
    }

    public int getErrorCount() {
        return fieldErrors.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        if (!fieldErrors.isEmpty()) {
            sb.append("\nField Errors:\n");
            fieldErrors.forEach((field, error) -> 
                sb.append("  - ").append(field).append(": ").append(error).append("\n")
            );
        }
        return sb.toString();
    }
}

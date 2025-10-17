package com.library.domain.valueobject;

import java.io.Serializable;
import java.util.Objects;

/**
 * Value object representing an International Standard Book Number (ISBN).
 * Supports both ISBN-10 and ISBN-13 formats.
 * Pure OOP implementation without any framework dependencies.
 */
public class ISBN implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String value;

    protected ISBN() {
        // For deserialization
        this.value = null;
    }

    public ISBN(String isbn) {
        Objects.requireNonNull(isbn, "ISBN cannot be null");
        
        // Normalize the input (remove hyphens and spaces, convert to uppercase)
        String normalized = isbn.replaceAll("[\\s-]", "").toUpperCase();
        
        if (!isValidISBN(normalized)) {
            throw new IllegalArgumentException("Invalid ISBN: " + isbn);
        }
        
        this.value = normalized;
    }

    /**
     * Validates the format and check digit of an ISBN.
     * @param isbn The ISBN to validate
     * @return true if the ISBN is valid, false otherwise
     */
    public static boolean isValidISBN(String isbn) {
        if (isbn == null) {
            return false;
        }
        
        // Remove hyphens and spaces
        String normalized = isbn.replaceAll("[\\s-]", "").toUpperCase();
        
        // Check if it's ISBN-10 or ISBN-13
        if (normalized.length() == 10) {
            return isValidISBN10(normalized);
        } else if (normalized.length() == 13) {
            return isValidISBN13(normalized);
        }
        
        return false;
    }
    
    private static boolean isValidISBN10(String isbn) {
        if (isbn == null || isbn.length() != 10) {
            return false;
        }
        
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            char c = isbn.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
            sum += (c - '0') * (10 - i);
        }
        
        // Check the check digit
        char lastChar = isbn.charAt(9);
        if (lastChar != 'X' && !Character.isDigit(lastChar)) {
            return false;
        }
        
        int checkDigit = (lastChar == 'X') ? 10 : (lastChar - '0');
        sum += checkDigit;
        
        return sum % 11 == 0;
    }
    
    private static boolean isValidISBN13(String isbn) {
        if (isbn == null || isbn.length() != 13 || !isbn.matches("^[0-9]+")) {
            return false;
        }
        
        // Check prefix (must be 978 or 979)
        if (!isbn.startsWith("978") && !isbn.startsWith("979")) {
            return false;
        }
        
        // Calculate check digit
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = isbn.charAt(i) - '0';
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        
        int checkDigit = (10 - (sum % 10)) % 10;
        return checkDigit == (isbn.charAt(12) - '0');
    }
    
    public String getValue() {
        return value;
    }
    
    public String toFormattedString() {
        if (value == null) {
            return "";
        }
        
        if (value.length() == 13) {
            // Format as ISBN-13: 978-3-16-148410-0
            return String.format("%s-%s-%s-%s-%s",
                value.substring(0, 3),
                value.substring(3, 4),
                value.substring(4, 6),
                value.substring(6, 12),
                value.substring(12));
        } else {
            // Format as ISBN-10: 0-306-40615-2
            return String.format("%s-%s-%s-%s",
                value.substring(0, 1),
                value.substring(1, 4),
                value.substring(4, 9),
                value.substring(9));
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ISBN isbn1 = (ISBN) o;
        return Objects.equals(value, isbn1.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return toFormattedString();
    }
}

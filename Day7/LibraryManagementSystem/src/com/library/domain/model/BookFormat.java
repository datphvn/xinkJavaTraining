package com.library.domain.model;

/**
 * Represents the format of a book in the library.
 */
public enum BookFormat {
    /** Physical book */
    PHYSICAL("Physical book"),
    
    /** Electronic book */
    EBOOK("Electronic book"),
    
    /** Audio book */
    AUDIOBOOK("Audio book"),
    
    /** Reference only - cannot borrow */
    REFERENCE_ONLY("Reference only - cannot borrow"),
    
    /** Digital access required */
    DIGITAL_ACCESS("Digital access required");

    private final String description;

    BookFormat(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Checks if this format is digital.
     * @return true if this is a digital format, false otherwise
     */
    public boolean isDigital() {
        return this == EBOOK || this == AUDIOBOOK || this == DIGITAL_ACCESS;
    }

    /**
     * Checks if this format can be borrowed.
     * @return true if this format can be borrowed, false otherwise
     */
    public boolean canBorrow() {
        return this != REFERENCE_ONLY;
    }
}

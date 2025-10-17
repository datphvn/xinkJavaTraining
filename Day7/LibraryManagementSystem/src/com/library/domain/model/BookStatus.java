package com.library.domain.model;

/**
 * Represents the status of a book in the library.
 */
public enum BookStatus {
    /** Book is available for borrowing */
    AVAILABLE,
    
    /** Book is currently borrowed */
    BORROWED,
    
    /** Book is lost and cannot be borrowed */
    LOST,
    
    /** Book is damaged and cannot be borrowed */
    DAMAGED,
    
    /** Book is under maintenance/repair */
    UNDER_MAINTENANCE,
    
    /** Book is reserved by a member */
    RESERVED
}

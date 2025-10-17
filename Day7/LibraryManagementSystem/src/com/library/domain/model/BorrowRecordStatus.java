package com.library.domain.model;

/**
 * Represents the status of a borrow record.
 */
public enum BorrowRecordStatus {
    /** Book is currently borrowed */
    BORROWED,
    
    /** Book has been returned */
    RETURNED,
    
    /** Book is overdue and not yet returned */
    OVERDUE,
    
    /** Book is lost during borrowing */
    LOST
}

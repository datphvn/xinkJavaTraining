package com.library.domain.exception;

/**
 * Exception thrown when a member exceeds their borrow limit.
 */
public class BorrowLimitExceededException extends LibraryException {
    public BorrowLimitExceededException(String memberId, int borrowLimit) {
        super("BORROW_LIMIT_EXCEEDED", 
              "Member '" + memberId + "' has reached the borrow limit of " + borrowLimit + " books");
    }

    public BorrowLimitExceededException(String memberId, int borrowLimit, Throwable cause) {
        super("BORROW_LIMIT_EXCEEDED", 
              "Member '" + memberId + "' has reached the borrow limit of " + borrowLimit + " books", 
              cause);
    }
}

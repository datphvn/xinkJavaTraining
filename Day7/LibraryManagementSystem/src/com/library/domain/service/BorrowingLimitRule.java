package com.library.domain.service;

import com.library.domain.exception.LibraryException;
import com.library.domain.model.Member;
import com.library.domain.model.MemberStatus;

/**
 * Business rule that checks if a member can borrow more books.
 * Validates that the member has not exceeded their borrowing limit.
 */
public class BorrowingLimitRule implements BusinessRule<Member> {
    private static final int DEFAULT_BORROW_LIMIT = 5;

    @Override
    public boolean applies(Member member) {
        return member != null && member.getStatus() == MemberStatus.ACTIVE;
    }

    @Override
    public void execute(Member member) throws LibraryException {
        int currentBorrowings = member.getBorrowedBookCount();
        int maxAllowed = member.getBorrowLimit();

        if (currentBorrowings >= maxAllowed) {
            throw new LibraryException("BORROW_LIMIT_EXCEEDED",
                String.format("Member %s has reached maximum borrowing limit: %d",
                    member.getMemberId(), maxAllowed));
        }
    }

    @Override
    public String getDescription() {
        return "Check if member has exceeded borrowing limit";
    }

    @Override
    public int getPriority() {
        return 100; // High priority
    }

    @Override
    public String getName() {
        return "BorrowingLimitRule";
    }
}

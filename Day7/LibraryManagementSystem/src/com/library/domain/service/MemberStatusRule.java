package com.library.domain.service;

import com.library.domain.exception.LibraryException;
import com.library.domain.model.Member;
import com.library.domain.model.MemberStatus;

/**
 * Business rule that checks if a member is active and can perform borrowing operations.
 */
public class MemberStatusRule implements BusinessRule<Member> {

    @Override
    public boolean applies(Member member) {
        return member != null;
    }

    @Override
    public void execute(Member member) throws LibraryException {
        if (member.getStatus() != MemberStatus.ACTIVE) {
            throw new LibraryException("MEMBER_NOT_ACTIVE",
                String.format("Member %s is not active. Current status: %s",
                    member.getMemberId(), member.getStatus()));
        }
    }

    @Override
    public String getDescription() {
        return "Check if member is active";
    }

    @Override
    public int getPriority() {
        return 200; // Higher priority than borrowing limit
    }

    @Override
    public String getName() {
        return "MemberStatusRule";
    }
}

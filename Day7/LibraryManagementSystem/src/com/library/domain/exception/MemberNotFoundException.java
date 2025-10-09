package com.library.domain.exception;

public class MemberNotFoundException extends LibraryException {
    public MemberNotFoundException(String id) {
        super("MEMBER_NOT_FOUND", "Member not found: " + id);
    }
}

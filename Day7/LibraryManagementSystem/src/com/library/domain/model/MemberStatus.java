package com.library.domain.model;

/**
 * Represents the status of a library member.
 */
public enum MemberStatus {
    /** Member is active and can borrow books */
    ACTIVE,
    
    /** Member is suspended and cannot borrow books */
    SUSPENDED,
    
    /** Member is inactive (account closed) */
    INACTIVE
}

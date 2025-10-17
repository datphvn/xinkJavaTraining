package com.library.domain.repository;

import com.library.domain.model.Member;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Member entities.
 * Provides specialized query methods for members.
 */
public interface MemberRepository extends Repository<Member, UUID> {
    /**
     * Finds members by name (case-insensitive, partial match).
     * @param name The name to search for
     * @return A list of matching members
     */
    List<Member> findByName(String name);

    /**
     * Finds all active members.
     * @return A list of active members
     */
    List<Member> findAllActive();

    /**
     * Finds all suspended members.
     * @return A list of suspended members
     */
    List<Member> findBySuspended();

    /**
     * Counts the number of active members.
     * @return The number of active members
     */
    long countActiveMembers();
}

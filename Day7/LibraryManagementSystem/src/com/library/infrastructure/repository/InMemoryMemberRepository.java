package com.library.infrastructure.repository;

import com.library.domain.model.Member;
import com.library.domain.model.MemberStatus;
import com.library.domain.repository.MemberRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * In-memory implementation of MemberRepository.
 * Provides specialized query methods for members.
 * Pure OOP implementation without any framework dependencies.
 */
public class InMemoryMemberRepository extends InMemoryRepository<Member, UUID> implements MemberRepository {

    @Override
    public List<Member> findByName(String name) {
        if (name == null || name.isBlank()) {
            return List.of();
        }
        String searchTerm = name.toLowerCase();
        return store.values().stream()
            .filter(member -> member.getName().toLowerCase().contains(searchTerm) && !member.isDeleted())
            .collect(Collectors.toList());
    }

    @Override
    public List<Member> findAllActive() {
        return store.values().stream()
            .filter(member -> member.getStatus() == MemberStatus.ACTIVE && !member.isDeleted())
            .collect(Collectors.toList());
    }

    @Override
    public List<Member> findBySuspended() {
        return store.values().stream()
            .filter(member -> member.getStatus() == MemberStatus.SUSPENDED && !member.isDeleted())
            .collect(Collectors.toList());
    }

    @Override
    public long countActiveMembers() {
        return store.values().stream()
            .filter(member -> member.getStatus() == MemberStatus.ACTIVE && !member.isDeleted())
            .count();
    }

    @Override
    protected UUID extractId(Member entity) {
        return entity.getId();
    }
}

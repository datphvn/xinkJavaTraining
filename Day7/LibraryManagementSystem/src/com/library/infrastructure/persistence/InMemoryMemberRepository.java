package com.library.infrastructure.persistence;

import com.library.domain.model.Member;
import com.library.domain.model.MemberStatus;
import com.library.domain.repository.MemberRepository;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryMemberRepository extends InMemoryRepository<Member, java.util.UUID> implements MemberRepository {

    @Override
    public List<Member> findByName(String name) {
        return findAll().stream()
                .filter(m -> m.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Member> findAllActive() {
        return findAll().stream()
                .filter(m -> m.getStatus() == MemberStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    @Override
    public List<Member> findBySuspended() {
        return findAll().stream()
                .filter(m -> m.getStatus() == MemberStatus.SUSPENDED)
                .collect(Collectors.toList());
    }

    @Override
    public long countActiveMembers() {
        return findAllActive().size();
    }
}

package com.xink.training.day13.service;

import com.xink.training.day13.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByAgeRange(int minAge, int maxAge);
    List<User> findByNamePattern(String pattern);
    long count();
}


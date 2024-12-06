package com.permission_management.domain.models;

import java.util.Optional;
import java.util.UUID;

public interface Gateway<T> {
    T save(T entity);
    Optional<T> findById(UUID id);
    void deleteById(UUID id);
}


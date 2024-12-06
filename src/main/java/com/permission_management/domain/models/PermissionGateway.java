package com.permission_management.domain.models;

import com.permission_management.infrastructure.persistence.entity.Permission;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionGateway extends Gateway<Permission> {
    Permission save(Permission permission);
    List<Permission> findAll();
    Optional<Permission> findById(UUID id);
    void deleteById(UUID id);
}

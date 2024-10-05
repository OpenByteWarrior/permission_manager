package com.permissions.domain.models;

import com.permissions.infrastructure.driven_adapter.bd.entity.Permission;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionGateway {
    Permission save(Permission permission);
    List<Permission> findAll();
    Optional<Permission> findById(UUID id);
}

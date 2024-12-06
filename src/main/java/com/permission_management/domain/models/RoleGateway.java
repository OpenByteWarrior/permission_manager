package com.permission_management.domain.models;
import com.permission_management.infrastructure.persistence.entity.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleGateway extends Gateway<Role> {
    Role save(Role role);
    List<Role> findAll();
    Optional<Role> findById(UUID id);
    void deleteById(UUID id);
}

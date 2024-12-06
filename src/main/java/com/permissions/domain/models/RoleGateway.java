package com.permissions.domain.models;
import com.permissions.infrastructure.driven_adapter.bd.entity.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleGateway extends Gateway<Role> {
    Role save(Role role);
    List<Role> findAll();
    Optional<Role> findById(UUID id);
    void deleteById(UUID id);
}

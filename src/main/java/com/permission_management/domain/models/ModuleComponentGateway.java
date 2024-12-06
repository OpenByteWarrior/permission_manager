package com.permission_management.domain.models;

import com.permission_management.infrastructure.persistence.entity.ModuleComponent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleComponentGateway {
    ModuleComponent save(ModuleComponent moduleComponent);
    List<ModuleComponent> findAll();
    Optional<ModuleComponent> findById(UUID id);
    void deleteById(UUID id);
}

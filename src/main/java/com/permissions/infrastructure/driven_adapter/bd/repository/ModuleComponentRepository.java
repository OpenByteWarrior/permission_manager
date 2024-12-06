package com.permissions.infrastructure.driven_adapter.bd.repository;

import com.permissions.infrastructure.driven_adapter.bd.entity.ModuleComponent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ModuleComponentRepository extends JpaRepository<ModuleComponent, UUID> {
}

package com.permission_management.infrastructure.persistence.repository;

import com.permission_management.infrastructure.persistence.entity.ModuleComponent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ModuleComponentRepository extends JpaRepository<ModuleComponent, UUID> {
}

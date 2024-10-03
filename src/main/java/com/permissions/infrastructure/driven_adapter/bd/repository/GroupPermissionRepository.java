package com.permissions.infrastructure.driven_adapter.bd.repository;

import com.permissions.infrastructure.driven_adapter.bd.entity.GroupPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GroupPermissionRepository extends JpaRepository<GroupPermission, UUID> {
}

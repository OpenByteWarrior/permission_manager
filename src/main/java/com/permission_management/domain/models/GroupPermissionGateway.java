package com.permission_management.domain.models;

import com.permission_management.infrastructure.persistence.entity.GroupPermission;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupPermissionGateway extends Gateway<GroupPermission> {
  GroupPermission save(GroupPermission groupPermission);
  List<GroupPermission> findAll();
  Optional<GroupPermission> findById(UUID id);
  void deleteById(UUID id);
}

package com.permissions.domain.models;

import com.permissions.infrastructure.driven_adapter.bd.entity.GroupPermission;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupPermissionGateway {
  GroupPermission save(GroupPermission groupPermission);
  List<GroupPermission> findAll();
  Optional<GroupPermission> findById(UUID id);
}

package com.permissions.infrastructure.driven_adapter.bd.repository;

import com.permissions.infrastructure.driven_adapter.bd.entity.GroupPermission;
import com.permissions.domain.models.GroupPermissionGateway;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GroupAdapter implements GroupPermissionGateway {

  private final GroupPermissionRepository groupPermissionRepository;

  @Override
  public GroupPermission save(GroupPermission groupPermission) {
    return this.groupPermissionRepository.save(groupPermission);
  }

  @Override
  public List<GroupPermission> findAll() {
    return this.groupPermissionRepository.findAll();
  }

  @Override
  public Optional<GroupPermission> findById(UUID id) {
    return this.groupPermissionRepository.findById(id);
  }
}

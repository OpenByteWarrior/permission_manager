package com.permission_management.infrastructure.persistence.adapter;

import com.permission_management.infrastructure.persistence.entity.GroupPermission;
import com.permission_management.domain.models.GroupPermissionGateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.permission_management.infrastructure.persistence.repository.GroupPermissionRepository;
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

    @Override
    public void deleteById(UUID id) {
        this.groupPermissionRepository.deleteById(id);
    }
}

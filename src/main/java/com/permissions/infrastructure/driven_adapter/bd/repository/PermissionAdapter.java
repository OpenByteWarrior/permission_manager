package com.permissions.infrastructure.driven_adapter.bd.repository;

import com.permissions.infrastructure.driven_adapter.bd.entity.Permission;
import com.permissions.domain.models.PermissionGateway;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PermissionAdapter implements PermissionGateway {

    private final PermissionRepository permissionRepository;

    @Override
    public Permission save(Permission groupPermission) {
        return this.permissionRepository.save(groupPermission);
    }

    @Override
    public List<Permission> findAll() {
        return this.permissionRepository.findAll();
    }

    @Override
    public Optional<Permission> findById(UUID id) {
        return this.permissionRepository.findById(id);
    }
}

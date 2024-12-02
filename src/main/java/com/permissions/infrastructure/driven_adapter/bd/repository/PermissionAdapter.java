package com.permissions.infrastructure.driven_adapter.bd.repository;

import com.permissions.domain.models.dto.PermissionDTO;
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
    public Permission save(Permission permission) {
        return this.permissionRepository.save(permission);
    }

    @Override
    public List<Permission> findAll() {
        return this.permissionRepository.findAll();
    }

    @Override
    public Optional<Permission> findById(UUID id) {
        return this.permissionRepository.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
        this.permissionRepository.deleteById(id);
    }

}

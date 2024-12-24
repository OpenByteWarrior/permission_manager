package com.permission_management.infrastructure.persistence.adapter;

import com.permission_management.infrastructure.persistence.entity.Permission;
import com.permission_management.domain.models.PermissionGateway;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.permission_management.infrastructure.persistence.repository.PermissionRepository;
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

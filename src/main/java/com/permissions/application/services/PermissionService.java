package com.permissions.application.services;

import com.permissions.domain.models.dto.ResponseHttpDTO;
import com.permissions.application.ports.input.PermissionUseCase;
import com.permissions.infrastructure.driven_adapter.bd.entity.Permission;
import com.permissions.domain.usecases.PermissionDomainUseCase;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PermissionService implements PermissionUseCase {
    private final PermissionDomainUseCase domainService;

    public PermissionService(PermissionDomainUseCase domainService) {
        this.domainService = domainService;
    }

    @Override
    public ResponseEntity<ResponseHttpDTO<Permission>> createPermission(Permission permission) {
        return domainService.createPermission(permission);
    }

    @Override
    public ResponseEntity<ResponseHttpDTO<List<Permission>>> getAllPermissions() {
        return domainService.getAllPermissions();
    }

    @Override
    public  ResponseEntity<ResponseHttpDTO<Permission>> getPermissionById(UUID id){
        return domainService.getPermissionById(id);
    }
}

package com.permissions.application.services;

import com.permissions.application.dto.ResponseHttpDTO;
import com.permissions.application.ports.input.PermissionUseCase;
import com.permissions.domain.models.Permission;
import com.permissions.domain.services.PermissionDomainService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PermissionService implements PermissionUseCase {
    private final PermissionDomainService domainService;

    public PermissionService(PermissionDomainService domainService) {
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

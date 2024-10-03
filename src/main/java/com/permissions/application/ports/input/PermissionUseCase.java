package com.permissions.application.ports.input;

import com.permissions.application.dto.ResponseHttpDTO;
import com.permissions.domain.models.Permission;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

public interface PermissionUseCase {
    ResponseEntity<ResponseHttpDTO<Permission>> createPermission(Permission permission);
    ResponseEntity<ResponseHttpDTO<List<Permission>>> getAllPermissions();
    ResponseEntity<ResponseHttpDTO<Permission>> getPermissionById(UUID id);
}
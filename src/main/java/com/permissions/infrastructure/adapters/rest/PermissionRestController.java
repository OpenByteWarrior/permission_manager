package com.permissions.infrastructure.adapters.rest;

import com.permissions.domain.models.dto.ResponseHttpDTO;
import com.permissions.application.ports.input.PermissionUseCase;
import com.permissions.infrastructure.driven_adapter.bd.entity.Permission;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/permissions")
public class PermissionRestController {
    private final PermissionUseCase permissionService;

    public PermissionRestController(PermissionUseCase permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping
    public void createPermission(@RequestBody Permission permission) {
        permissionService.createPermission(permission);
    }

    @GetMapping
    public ResponseEntity<ResponseHttpDTO<List<Permission>>> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseHttpDTO<Permission>> getPermissionById(@PathVariable("id") UUID id){
        return permissionService.getPermissionById(id);
    }
}
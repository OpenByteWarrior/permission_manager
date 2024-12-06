package com.permission_management.infrastructure.rest;

import com.permission_management.domain.models.dto.GroupPermissionDTO;
import com.permission_management.domain.models.dto.PermissionAssignAndRemoveBodyGroupDTO;
import com.permission_management.domain.models.dto.PermissionDTO;
import com.permission_management.domain.models.dto.ResponseHttpDTO;
import com.permission_management.application.usecase.PermissionDomainUseCase;
import com.permission_management.infrastructure.persistence.entity.Permission;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionRestController {
    private final PermissionDomainUseCase permissionDomainUseCase;

    @PostMapping
    public ResponseEntity<ResponseHttpDTO<PermissionDTO>> createPermission(@RequestBody Permission permission) {
        return new ResponseEntity<>(
                permissionDomainUseCase.createPermission(permission), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponseHttpDTO<List<PermissionDTO>>> getAllPermissions() {
        return new ResponseEntity<>(permissionDomainUseCase.getAllPermissions(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseHttpDTO<PermissionDTO>> getPermissionById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(permissionDomainUseCase.getPermissionById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseHttpDTO<String>> deletePermissionById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(permissionDomainUseCase.deletePermissionById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseHttpDTO<PermissionDTO>> updatePermissionById(@PathVariable("id") UUID id, @RequestBody PermissionDTO permissionDTO) {
        return new ResponseEntity<>(permissionDomainUseCase.updatePermissionById(id, permissionDTO), HttpStatus.OK);
    }

    @PutMapping("/assign")
    public ResponseEntity<ResponseHttpDTO<GroupPermissionDTO>> assignPermissionGroup( @RequestBody PermissionAssignAndRemoveBodyGroupDTO body) {
        return new ResponseEntity<>(permissionDomainUseCase.assignPermissionGroup(body), HttpStatus.OK);
    }
    @DeleteMapping("/remove")
    public ResponseEntity<ResponseHttpDTO<GroupPermissionDTO>> removePermissionGroup( @RequestBody PermissionAssignAndRemoveBodyGroupDTO body) {
        return new ResponseEntity<>(permissionDomainUseCase.removePermissionGroup(body), HttpStatus.OK);
    }
}
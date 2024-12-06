package com.permission_management.infrastructure.rest;

import com.permission_management.domain.models.dto.ResponseHttpDTO;
import com.permission_management.domain.models.dto.RoleBodyDTO;
import com.permission_management.domain.models.dto.RoleDTO;
import com.permission_management.application.usecase.RoleDomainUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleRestController {
    private final RoleDomainUseCase roleDomainUseCase;

    @PostMapping
    public ResponseEntity<ResponseHttpDTO<RoleDTO>> createRole(@RequestBody RoleBodyDTO role) {
        return new ResponseEntity<>(
                roleDomainUseCase.createRole(role), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponseHttpDTO<List<RoleDTO>>> getAllRoles() {
        return new ResponseEntity<>(roleDomainUseCase.getAllRoles(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseHttpDTO<RoleDTO>> getRoleById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(roleDomainUseCase.getRoleById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseHttpDTO<String>> deleteRoleById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(roleDomainUseCase.deleteRoleById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseHttpDTO<RoleDTO>> updateRoleById(@PathVariable("id") UUID id, @RequestBody RoleDTO roleDTO) {
        return new ResponseEntity<>(roleDomainUseCase.updateRoleById(id, roleDTO), HttpStatus.OK);
    }
}

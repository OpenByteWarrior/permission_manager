package com.permissions.infrastructure.adapters.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.permissions.application.dto.GroupPermissionDTO;
import com.permissions.application.dto.ResponseHttpDTO;
import com.permissions.application.ports.input.GroupPermissionUseCase;
import com.permissions.domain.models.GroupPermission;

@RestController
@RequestMapping("/group_permissions")
public class GroupPermissionRestController {

    private final GroupPermissionUseCase groupPermissionService;

    public GroupPermissionRestController(GroupPermissionUseCase groupPermissionService) {
        this.groupPermissionService = groupPermissionService;
    }
    @PostMapping
    public ResponseEntity<ResponseHttpDTO<GroupPermission>> createGroupPermission(@Valid @RequestBody GroupPermissionDTO groupPermissionDTO) {
        return groupPermissionService.createGroupPermission(groupPermissionDTO);
    }
    @GetMapping
    public ResponseEntity<ResponseHttpDTO<List<GroupPermission>>> getAllGroupsPermissions() {
        return groupPermissionService.getAllGroupsPermissions();
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseHttpDTO<GroupPermission>> getGroupPermissionById(@PathVariable("id") UUID id) {
        return groupPermissionService.getGroupPermissionById(id);
    }
}

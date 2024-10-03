package com.permissions.application.ports.input;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.permissions.application.dto.GroupPermissionDTO;
import com.permissions.application.dto.ResponseHttpDTO;
import com.permissions.domain.models.GroupPermission;

public interface GroupPermissionUseCase {
    ResponseEntity<ResponseHttpDTO<GroupPermission>> createGroupPermission(GroupPermissionDTO groupPermissionDTO);
    ResponseEntity<ResponseHttpDTO<List<GroupPermission>>> getAllGroupsPermissions();
    ResponseEntity<ResponseHttpDTO<GroupPermission>> getGroupPermissionById(UUID id);
}

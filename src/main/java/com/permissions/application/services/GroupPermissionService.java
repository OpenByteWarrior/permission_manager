package com.permissions.application.services;

import java.util.List;
import java.util.UUID;

import com.permissions.application.dto.GroupPermissionDTO;
import com.permissions.application.dto.ResponseHttpDTO;
import com.permissions.application.ports.input.GroupPermissionUseCase;
import com.permissions.domain.models.GroupPermission;
import com.permissions.domain.services.GroupPermissionDomainService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GroupPermissionService implements GroupPermissionUseCase {

    private final GroupPermissionDomainService domainService;

    public GroupPermissionService(GroupPermissionDomainService domainService) {
        this.domainService = domainService;
    }

    @Override
    public ResponseEntity<ResponseHttpDTO<GroupPermission>> createGroupPermission(GroupPermissionDTO groupPermissionDTO) {
        return domainService.createGroupPermission(groupPermissionDTO);
    }

    @Override
    public ResponseEntity<ResponseHttpDTO<List<GroupPermission>>> getAllGroupsPermissions(){
        return domainService.getAllGroupsPermissions();
    }

    @Override
    public ResponseEntity<ResponseHttpDTO<GroupPermission>> getGroupPermissionById(UUID id) {
        return domainService.getGroupPermissionById(id);
    }
}

;
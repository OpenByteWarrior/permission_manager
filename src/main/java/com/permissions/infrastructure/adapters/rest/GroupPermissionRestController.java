package com.permissions.infrastructure.adapters.rest;

import com.permissions.domain.usecases.GroupPermissionDomainUseCase;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.permissions.domain.models.dto.GroupPermissionDTO;
import com.permissions.domain.models.dto.ResponseHttpDTO;
import com.permissions.infrastructure.driven_adapter.bd.entity.GroupPermission;

@RestController
@RequestMapping("/group_permissions")
@RequiredArgsConstructor
public class GroupPermissionRestController {

  private final GroupPermissionDomainUseCase groupPermissionDomainUseCase;

  @PostMapping
  public ResponseEntity<ResponseHttpDTO<GroupPermission>> createGroupPermission(
      @Valid @RequestBody GroupPermissionDTO groupPermissionDTO) {
    return new ResponseEntity<>(
        groupPermissionDomainUseCase.createGroupPermission(groupPermissionDTO), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<ResponseHttpDTO<List<GroupPermission>>> getAllGroupsPermissions() {
    return groupPermissionDomainUseCase.getAllGroupsPermissions();
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResponseHttpDTO<GroupPermission>> getGroupPermissionById(
      @PathVariable("id") UUID id) {
    return groupPermissionDomainUseCase.getGroupPermissionById(id);
  }
}

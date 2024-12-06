package com.permissions.infrastructure.rest;

import com.permissions.domain.usecases.GroupPermissionDomainUseCase;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.permissions.domain.models.dto.GroupPermissionBodyDTO;
import com.permissions.domain.models.dto.GroupPermissionDTO;
import com.permissions.domain.models.dto.ResponseHttpDTO;
import com.permissions.infrastructure.driven_adapter.bd.entity.GroupPermission;

@RestController
@RequestMapping("/group_permissions")
@RequiredArgsConstructor
public class GroupPermissionRestController {

  private final GroupPermissionDomainUseCase groupPermissionDomainUseCase;

  @PostMapping
  public ResponseEntity<ResponseHttpDTO<GroupPermissionDTO>> createGroupPermission(
      @RequestBody GroupPermissionBodyDTO groupPermissionBodyDTO) {
    return new ResponseEntity<>(
        groupPermissionDomainUseCase.createGroupPermission(groupPermissionBodyDTO), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<ResponseHttpDTO<List<GroupPermissionDTO>>> getAllGroupsPermissions() {
    return new ResponseEntity<>(groupPermissionDomainUseCase.getAllGroupsPermissions(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResponseHttpDTO<GroupPermissionDTO>> getGroupPermissionById(
      @PathVariable("id") UUID id) {
    return new ResponseEntity<>(groupPermissionDomainUseCase.getGroupPermissionById(id), HttpStatus.OK);
  }
  @DeleteMapping("/{id}")
  public ResponseEntity<ResponseHttpDTO<String>> deleteGroupPermissionById(
      @PathVariable("id") UUID id) {
    return new ResponseEntity<>(groupPermissionDomainUseCase.deleteGroupPermissionById(id), HttpStatus.OK);
  }
  @PutMapping("/{id}")
  public ResponseEntity<ResponseHttpDTO<GroupPermissionDTO>> updateGroupPermissionById(
      @PathVariable("id") UUID id, @RequestBody GroupPermissionDTO groupPermissionDTO) {
    return new ResponseEntity<>(
        groupPermissionDomainUseCase.updateGroupPermissionById(id, groupPermissionDTO), HttpStatus.OK);
  }
}

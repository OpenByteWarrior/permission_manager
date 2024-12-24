package com.permission_management.infrastructure.api;

import com.permission_management.application.dto.common.GroupPermissionDTO;
import com.permission_management.application.dto.common.RoleDTO;
import com.permission_management.application.dto.request.RequestAssignAndRemoveBodyDTO;
import com.permission_management.application.dto.request.RequestGroupPermissionBodyDTO;
import com.permission_management.application.dto.response.ResponseHttpDTO;
import com.permission_management.application.usecase.GroupPermissionUseCase;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group_permissions")
@RequiredArgsConstructor
public class GroupPermissionRestController {

  private final GroupPermissionUseCase groupPermissionDomainUseCase;

  @PostMapping
  public ResponseEntity<ResponseHttpDTO<GroupPermissionDTO>> createGroupPermission(
      @RequestBody RequestGroupPermissionBodyDTO groupPermissionBodyDTO) {
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
  @PutMapping("/assign")
  public ResponseEntity<ResponseHttpDTO<RoleDTO>> assignGroupPermissionToRole(@RequestBody RequestAssignAndRemoveBodyDTO body) {
    return new ResponseEntity<>(groupPermissionDomainUseCase.assignGroupPermissionToRole(body), HttpStatus.OK);
  }
  @DeleteMapping("/remove")
  public ResponseEntity<ResponseHttpDTO<RoleDTO>> removeGroupPermissionToRole( @RequestBody RequestAssignAndRemoveBodyDTO body) {
    return new ResponseEntity<>(groupPermissionDomainUseCase.removeGroupPermissionToRole(body), HttpStatus.OK);
  }
}

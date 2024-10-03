package com.permissions.domain.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.permissions.application.dto.ResponseHttpDTO;
import com.permissions.application.dto.GroupPermissionDTO;
import com.permissions.domain.models.GroupPermission;
import com.permissions.domain.models.Permission;
import com.permissions.domain.repository.GroupPermissionRepository;
import com.permissions.domain.repository.PermissionRepository;

@Service
public class GroupPermissionDomainService {
    private final GroupPermissionRepository groupPermissionRepository;
    private final PermissionRepository permissionRepository;

    public GroupPermissionDomainService(GroupPermissionRepository groupPermissionRepository,
            PermissionRepository permissionRepository) {
        this.groupPermissionRepository = groupPermissionRepository;
        this.permissionRepository = permissionRepository;
    }

    public ResponseEntity<ResponseHttpDTO<GroupPermission>> createGroupPermission(
            GroupPermissionDTO groupPermissionDTO) {
        try {
            Set<Permission> permissions = groupPermissionDTO.getPermissionIds().stream()
                    .map(permissionId -> permissionRepository.findById(permissionId))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());

            GroupPermission groupPermission = new GroupPermission();
            groupPermission.setName(groupPermissionDTO.getName());
            groupPermission.setDescription(groupPermissionDTO.getDescription());
            groupPermission.setPermissions(permissions);

            GroupPermission savedGroupPermission = groupPermissionRepository.save(groupPermission);

            ResponseHttpDTO<GroupPermission> response = new ResponseHttpDTO<>("200",
                    "Grupo de permisos creado correctamente", savedGroupPermission);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (DataAccessException e) {
            ResponseHttpDTO<GroupPermission> errorResponse = new ResponseHttpDTO<>("500",
                    "Error al guardar el grupo de permisos: " + e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            ResponseHttpDTO<GroupPermission> errorResponse = new ResponseHttpDTO<>("500",
                    "Ocurrió un error inesperado: " + e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseHttpDTO<List<GroupPermission>>> getAllGroupsPermissions() {
        try {
            List<GroupPermission> allGroupPermissions = groupPermissionRepository.findAll();

            if (allGroupPermissions.isEmpty()) {
                ResponseHttpDTO<List<GroupPermission>> emptyResponse = new ResponseHttpDTO<>("204",
                        "No hay grupos de permisos disponibles", null);
                return new ResponseEntity<>(emptyResponse, HttpStatus.NO_CONTENT);
            }
            ResponseHttpDTO<List<GroupPermission>> response = new ResponseHttpDTO<>("200",
                    "Grupos de permisos obtenidos correctamente", allGroupPermissions);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (DataAccessException e) {
            ResponseHttpDTO<List<GroupPermission>> errorResponse = new ResponseHttpDTO<>("500",
                    "Error al obtener los grupos de permisos: " + e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            ResponseHttpDTO<List<GroupPermission>> errorResponse = new ResponseHttpDTO<>("500",
                    "Ocurrió un error inesperado: " + e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseHttpDTO<GroupPermission>> getGroupPermissionById(UUID id) {

        try {
            Optional<GroupPermission> groupPermission = groupPermissionRepository.findById(id);
            if (groupPermission.isPresent()) {
                ResponseHttpDTO<GroupPermission> response = new ResponseHttpDTO<>("200",
                        "Grupo de permisos obtenido correctamente", groupPermission.get());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                ResponseHttpDTO<GroupPermission> response = new ResponseHttpDTO<>("404",
                        "El Grupo de permisos no existe", null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            ResponseHttpDTO<GroupPermission> errorResponse = new ResponseHttpDTO<>("500",
                    "Error al obtener la informacion del grupo de permisos: " + e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            ResponseHttpDTO<GroupPermission> errorResponse = new ResponseHttpDTO<>("500",
                    "Error al obtener la informacion del grupo de permisos: " + e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

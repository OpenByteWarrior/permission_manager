package com.permissions.domain.usecases;

import com.permissions.domain.models.GroupPermissionGateway;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.permissions.domain.models.dto.ResponseHttpDTO;
import com.permissions.domain.models.dto.GroupPermissionDTO;
import com.permissions.infrastructure.driven_adapter.bd.entity.GroupPermission;
import com.permissions.infrastructure.driven_adapter.bd.entity.Permission;
import com.permissions.infrastructure.driven_adapter.bd.repository.PermissionRepository;

@Service
@RequiredArgsConstructor
public class GroupPermissionDomainUseCase {
    private final GroupPermissionGateway groupPermissionGateway;
    private final PermissionRepository permissionRepository;

    public ResponseHttpDTO<GroupPermission> createGroupPermission(
            GroupPermissionDTO groupPermissionDTO) {
        try {
            Set<Permission> permissions = groupPermissionDTO.getPermissionIds().stream()
                    .map(permissionRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());

            GroupPermission groupPermission = new GroupPermission();
            groupPermission.setName(groupPermissionDTO.getName());
            groupPermission.setDescription(groupPermissionDTO.getDescription());
            groupPermission.setPermissions(permissions);

            GroupPermission savedGroupPermission = groupPermissionGateway.save(groupPermission);

            return new ResponseHttpDTO<>("200",
                    "Grupo de permisos creado correctamente", savedGroupPermission);

        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500",
                    "Error al guardar el grupo de permisos: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500",
                    "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseEntity<ResponseHttpDTO<List<GroupPermission>>> getAllGroupsPermissions() {
        try {
            List<GroupPermission> allGroupPermissions = groupPermissionGateway.findAll();

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
            Optional<GroupPermission> groupPermission = groupPermissionGateway.findById(id);
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

package com.permission_management.application.usecase;

import com.permission_management.application.dto.common.GroupPermissionDTO;
import com.permission_management.application.dto.common.PermissionDTO;
import com.permission_management.application.dto.request.RequestAssignAndRemoveBodyDTO;
import com.permission_management.application.dto.response.ResponseHttpDTO;
import com.permission_management.application.service.CrudService;
import com.permission_management.domain.models.*;
import com.permission_management.infrastructure.persistence.entity.GroupPermission;
import com.permission_management.infrastructure.persistence.entity.Permission;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionUseCase {
    private final PermissionGateway permissionGateway;
    private final GroupPermissionGateway groupPermissionGateway;
    private final CrudService crudService;
    private final ModelMapper modelMapper;

    public ResponseHttpDTO<PermissionDTO> createPermission(Permission permission) {
        return crudService.create(permission, permissionGateway,PermissionDTO.class, "permiso");
    }

    public ResponseHttpDTO<List<PermissionDTO>> getAllPermissions() {
        return crudService.getAllResource(permissionGateway, PermissionDTO.class, "permisos");
    }

    public ResponseHttpDTO<PermissionDTO> getPermissionById(UUID id) {
        return crudService.getResourceById(id, permissionGateway, PermissionDTO.class, "permiso");
    }

    public ResponseHttpDTO<String> deletePermissionById(UUID id) {
        return crudService.deleteResourceById(id, permissionGateway, "permiso");
    }

    public ResponseHttpDTO<PermissionDTO> updatePermissionById(UUID id, PermissionDTO permissionDTO) {
        return crudService.updateResourceById(id, permissionDTO, permissionGateway, PermissionDTO.class, "permiso");
    }

    public ResponseHttpDTO<GroupPermissionDTO> assignPermissionToGroup(RequestAssignAndRemoveBodyDTO body) {
        try {
            GroupPermission groupPermission = groupPermissionGateway.findById(body.getIdContainer())
                    .orElseThrow(() -> new IllegalArgumentException("El grupo de permisos no existe"));

            Set<Permission> resources = body.getResourcesIds().stream()
                    .map(id -> permissionGateway.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("El permiso con el ID " + id + " no existe")))
                    .collect(Collectors.toSet());

            Set<Permission> alreadyAssignedResources = resources.stream()
                    .filter(groupPermission.getPermissions()::contains)
                    .collect(Collectors.toSet());

            if (!alreadyAssignedResources.isEmpty()) {
                return new ResponseHttpDTO<>("400", "Algunos permisos ya están asignados al rol", null);
            }

            groupPermission.getPermissions().addAll(resources);
            groupPermissionGateway.save(groupPermission);

            GroupPermissionDTO groupPermissionDTO = modelMapper.map(groupPermission, GroupPermissionDTO.class);

            return new ResponseHttpDTO<>("200", "Permisos asignados correctamente al grupo de permisos", groupPermissionDTO);
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al acceder a la base de datos: " + e.getMessage(), null);
        } catch (IllegalArgumentException e) {
            return new ResponseHttpDTO<>("400", e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<GroupPermissionDTO> removePermissionToGroup(RequestAssignAndRemoveBodyDTO body) {
        try {
            GroupPermission groupPermission = groupPermissionGateway.findById(body.getIdContainer())
                    .orElseThrow(() -> new IllegalArgumentException("El grupo de permisos no existe"));

            Set<Permission> resourcesToRemove = body.getResourcesIds().stream()
                    .map(id -> permissionGateway.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("El permisos con el ID " + id + " no existe")))
                    .collect(Collectors.toSet());

            Set<Permission> assignedResources = resourcesToRemove.stream()
                    .filter(groupPermission.getPermissions()::contains)
                    .collect(Collectors.toSet());

            if (assignedResources.isEmpty()) {
                return new ResponseHttpDTO<>("400", "Ninguno de los permisos especificados está asignado al grupo de permisos", null);
            }

            groupPermission.getPermissions().removeAll(assignedResources);
            groupPermissionGateway.save(groupPermission);

            GroupPermissionDTO groupPermissionDTO = modelMapper.map(groupPermission, GroupPermissionDTO.class);

            return new ResponseHttpDTO<>("200", "Permisos eliminados correctamente del grupo de permisos", groupPermissionDTO);
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al acceder a la base de datos: " + e.getMessage(), null);
        } catch (IllegalArgumentException e) {
            return new ResponseHttpDTO<>("400", e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

}

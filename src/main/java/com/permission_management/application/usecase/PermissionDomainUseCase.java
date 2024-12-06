package com.permission_management.application.usecase;

import com.permission_management.application.service.ResourceAssignmentService;
import com.permission_management.domain.models.*;
import com.permission_management.application.dto.*;
import com.permission_management.infrastructure.persistence.entity.GroupPermission;
import com.permission_management.infrastructure.persistence.entity.Permission;

import com.permission_management.infrastructure.persistence.entity.Role;
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
public class PermissionDomainUseCase {
    private final PermissionGateway permissionGateway;
    private final GroupPermissionGateway groupPermissionGateway;
    private final ModelMapper modelMapper;

    public ResponseHttpDTO<PermissionDTO> createPermission(Permission permission) {
        try {
            Permission savedPermission = permissionGateway.save(permission);
            PermissionDTO permissionDTO = modelMapper.map(savedPermission, PermissionDTO.class);
            return new ResponseHttpDTO<>("200", "Permiso creado correctamente", permissionDTO);

        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al guardar el permiso: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<List<PermissionDTO>> getAllPermissions() {
        try {
            List<PermissionDTO> AllPermissions = permissionGateway.findAll().stream().map(permission -> modelMapper.map(permission, PermissionDTO.class)).toList();

            if (AllPermissions.isEmpty()) {
                return new ResponseHttpDTO<>("204", "No hay permisos disponibles", null);
            }
            return new ResponseHttpDTO<>("200", "Permisos obtenidos correctamente", AllPermissions);
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al obtener los permisos: " + e.getMessage(), null);

        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<PermissionDTO> getPermissionById(UUID id) {

        try {
            return permissionGateway.findById(id).map(permission -> {
                PermissionDTO permissionDTO = modelMapper.map(permission, PermissionDTO.class);
                return new ResponseHttpDTO<>("200", "Permiso obtenido correctamente", permissionDTO);
            }).orElseGet(() -> new ResponseHttpDTO<>("404", "El permiso no existe", null));
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al obtener la informacion del permiso: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }

    }

    public ResponseHttpDTO<String> deletePermissionById(UUID id) {
        try {
            return permissionGateway.findById(id).map(permission -> {
                permissionGateway.deleteById(id);
                return new ResponseHttpDTO<>("200", "Permiso eliminado correctamente", "OK");
            }).orElseGet(() -> new ResponseHttpDTO<>("404", "El permiso no existe", "NOT FOUND"));
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al eliminar el permiso: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<PermissionDTO> updatePermissionById(UUID id, PermissionDTO permissionDTO) {
        try {
            return permissionGateway.findById(id).map(existingPermission -> {
                modelMapper.getConfiguration().setSkipNullEnabled(true);
                modelMapper.map(permissionDTO, existingPermission);
                Permission updatedPermission = permissionGateway.save(existingPermission);
                PermissionDTO updatedPermissionDTO = modelMapper.map(updatedPermission, PermissionDTO.class);

                return new ResponseHttpDTO<>("200", "Permiso actualizado correctamente", updatedPermissionDTO);
            }).orElseGet(() -> new ResponseHttpDTO<>("404", "El permiso no existe", null));
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al actualizar la información del permiso: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<GroupPermissionDTO> assignPermissionToGroup(AssignAndRemoveBodyDTO body) {
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

    public ResponseHttpDTO<GroupPermissionDTO> removePermissionToGroup(AssignAndRemoveBodyDTO body) {
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

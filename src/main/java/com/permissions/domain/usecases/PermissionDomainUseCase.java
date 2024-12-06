package com.permissions.domain.usecases;

import com.permissions.domain.models.*;
import com.permissions.domain.models.dto.*;
import com.permissions.infrastructure.driven_adapter.bd.entity.Permission;

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
    private final RoleGateway roleGateway;
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

    public ResponseHttpDTO<GroupPermissionDTO> assignPermissionGroup(PermissionAssignAndRemoveBodyGroupDTO body) {
        return assignPermissions(body.getIdGroup(), body.getPermissionIds(), groupPermissionGateway, GroupPermissionDTO.class, "grupo de permisos");
    }

    public ResponseHttpDTO<GroupPermissionDTO> removePermissionGroup(PermissionAssignAndRemoveBodyGroupDTO body) {
        return removePermissions(body.getIdGroup(), body.getPermissionIds(), groupPermissionGateway, GroupPermissionDTO.class, "grupo de permisos");
    }
    public ResponseHttpDTO<RoleDTO> assignPermissionRole(PermissionAssignAndRemoveBodyRoleDTO body) {
        return assignPermissions(body.getIdRole(), body.getPermissionIds(), roleGateway, RoleDTO.class, "rol");
    }

    public ResponseHttpDTO<RoleDTO> removePermissionRole(PermissionAssignAndRemoveBodyRoleDTO body) {
        return removePermissions(body.getIdRole(), body.getPermissionIds(), roleGateway, RoleDTO.class, "rol");
    }

    public <T extends PermissionContainer,D> ResponseHttpDTO<D> assignPermissions(
            UUID containerId,
            Set<UUID> ids,
            Gateway<T> containerGateway,
            Class<D> dtoClass,
            String containerType
    ) {
        try {
            T container = containerGateway.findById(containerId)
                    .orElseThrow(() -> new IllegalArgumentException(containerType + " no existe"));

            Set<Permission> permissions = ids.stream()
                    .map(permissionId -> permissionGateway.findById(permissionId)
                            .orElseThrow(() -> new IllegalArgumentException("Permiso con ID " + permissionId + " no existe")))
                    .collect(Collectors.toSet());
            Set<Permission> alreadyAssignedPermissions = permissions.stream()
                    .filter(container.getPermissions()::contains)
                    .collect(Collectors.toSet());

            if (!alreadyAssignedPermissions.isEmpty()) {
                return new ResponseHttpDTO<>("400", "Algunos permisos ya están asignados al " + containerType, null);
            }
            container.getPermissions().addAll(permissions);
            containerGateway.save(container);
            D containerDTO = modelMapper.map(container, dtoClass);

            return new ResponseHttpDTO<D>("200", "Permisos asignados correctamente al " + containerType, containerDTO);
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al acceder a la base de datos: " + e.getMessage(), null);
        } catch (IllegalArgumentException e) {
            return new ResponseHttpDTO<>("400", e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public <T extends PermissionContainer,D> ResponseHttpDTO<D> removePermissions(
            UUID containerId,
            Set<UUID> ids,
            Gateway<T> containerGateway,
            Class<D> dtoClass,
            String containerType
    ) {
        try {
            T container = containerGateway.findById(containerId)
                    .orElseThrow(() -> new IllegalArgumentException(containerType + " no existe"));
            Set<Permission> permissionsToRemove = ids.stream()
                    .map(permissionId -> permissionGateway.findById(permissionId)
                            .orElseThrow(() -> new IllegalArgumentException("Permiso con ID " + permissionId + " no existe")))
                    .collect(Collectors.toSet());
            Set<Permission> assignedPermissions = permissionsToRemove.stream()
                    .filter(container.getPermissions()::contains)
                    .collect(Collectors.toSet());

            if (assignedPermissions.isEmpty()) {
                return new ResponseHttpDTO<>("400", "Ninguno de los permisos especificados está asignado al " + containerType, null);
            }
            container.getPermissions().removeAll(assignedPermissions);
            containerGateway.save(container);
            D containerDTO = modelMapper.map(container, dtoClass);

            return new ResponseHttpDTO<D>("200", "Permisos eliminados correctamente del " + containerType, containerDTO);
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al acceder a la base de datos: " + e.getMessage(), null);
        } catch (IllegalArgumentException e) {
            return new ResponseHttpDTO<>("400", e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }
}

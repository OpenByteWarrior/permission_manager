package com.permission_management.application.usecase;

import com.permission_management.application.service.ResourceAssignmentService;
import com.permission_management.domain.models.*;
import com.permission_management.domain.models.dto.*;
import com.permission_management.infrastructure.persistence.entity.Permission;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionDomainUseCase {
    private final PermissionGateway permissionGateway;
    private final GroupPermissionGateway groupPermissionGateway;
    private final RoleGateway roleGateway;
    private final ModelMapper modelMapper;
    private final ResourceAssignmentService resourceAssignmentService;

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
        return resourceAssignmentService.assignResource(body.getIdGroup(), body.getPermissionIds(), groupPermissionGateway, permissionGateway, GroupPermissionDTO.class, "grupo de permisos");
    }

    public ResponseHttpDTO<GroupPermissionDTO> removePermissionGroup(PermissionAssignAndRemoveBodyGroupDTO body) {
        return resourceAssignmentService.removeResource(body.getIdGroup(), body.getPermissionIds(), groupPermissionGateway, permissionGateway, GroupPermissionDTO.class, "grupo de permisos");
    }

    public ResponseHttpDTO<RoleDTO> assignPermissionRole(PermissionAssignAndRemoveBodyRoleDTO body) {
        return resourceAssignmentService.assignResource(body.getIdRole(), body.getPermissionIds(), roleGateway, permissionGateway, RoleDTO.class, "rol");
    }

    public ResponseHttpDTO<RoleDTO> removePermissionRole(PermissionAssignAndRemoveBodyRoleDTO body) {
        return resourceAssignmentService.removeResource(body.getIdRole(), body.getPermissionIds(), roleGateway, permissionGateway, RoleDTO.class, "rol");
    }

}

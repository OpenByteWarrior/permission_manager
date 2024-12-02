package com.permissions.domain.usecases;

import com.permissions.domain.models.PermissionGateway;
import com.permissions.domain.models.dto.ResponseHttpDTO;
import com.permissions.infrastructure.driven_adapter.bd.entity.Permission;
import com.permissions.domain.models.dto.PermissionDTO;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionDomainUseCase {
    private final PermissionGateway permissionGateway;
    private final ModelMapper modelMapper;

    public ResponseHttpDTO<PermissionDTO> createPermission(Permission permission) {
        try {
            Permission savedPermission = permissionGateway.save(permission);
            PermissionDTO permissionDTO = modelMapper.map(savedPermission, PermissionDTO.class);
            return new ResponseHttpDTO<>("200", "Permiso creado correctamente",
                    permissionDTO);

        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500",
                    "Error al guardar el permiso: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500",
                    "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<List<PermissionDTO>> getAllPermissions() {
        try {
            List<PermissionDTO> AllPermissions = permissionGateway.findAll().stream().map(permission -> modelMapper.map(permission, PermissionDTO.class)).toList();

            if (AllPermissions.isEmpty()) {
                return new ResponseHttpDTO<>("204",
                        "No hay permisos disponibles", null);
            }
            return new ResponseHttpDTO<>("200",
                    "Permisos obtenidos correctamente", AllPermissions);
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500",
                    "Error al obtener los permisos: " + e.getMessage(), null);

        } catch (Exception e) {
            return new ResponseHttpDTO<>("500",
                    "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<PermissionDTO> getPermissionById(UUID id) {

        try {
            return permissionGateway.findById(id)
                    .map(permission -> {
                        PermissionDTO permissionDTO = modelMapper.map(permission, PermissionDTO.class);
                        return new ResponseHttpDTO<>("200", "Permiso obtenido correctamente", permissionDTO);
                    })
                    .orElseGet(() -> new ResponseHttpDTO<>("404", "El permiso no existe", null));
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500",
                    "Error al obtener la informacion del permiso: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500",
                    "Ocurrió un error inesperado: " + e.getMessage(), null);
        }

    }

    public ResponseHttpDTO<Permission> deletePermissionById(UUID id) {
        try {
            return permissionGateway.findById(id)
                    .map(permission -> {
                        permissionGateway.deleteById(id);
                        return new ResponseHttpDTO<Permission>("200", "Permiso eliminado correctamente", null);
                    })
                    .orElseGet(() -> new ResponseHttpDTO<Permission>("404", "El permiso no existe", null));
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500",
                    "Error al eliminar el permiso: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500",
                    "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<PermissionDTO> updatePermission(UUID id, PermissionDTO permissionDTO) {
        try {
            return permissionGateway.findById(id)
                    .map(existingPermission -> {
                        modelMapper.getConfiguration().setSkipNullEnabled(true);
                        modelMapper.map(permissionDTO, existingPermission);
                        Permission updatedPermission = permissionGateway.save(existingPermission);
                        PermissionDTO updatedPermissionDTO = modelMapper.map(updatedPermission, PermissionDTO.class);

                        return new ResponseHttpDTO<>("200", "Permiso actualizado correctamente", updatedPermissionDTO);
                    })
                    .orElseGet(() -> new ResponseHttpDTO<>("404", "El permiso no existe", null));
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500",
                    "Error al actualizar la información del permiso: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500",
                    "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }
}

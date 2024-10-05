package com.permissions.domain.usecases;

import com.permissions.domain.models.PermissionGateway;
import com.permissions.domain.models.dto.ResponseHttpDTO;
import com.permissions.infrastructure.driven_adapter.bd.entity.Permission;
import com.permissions.infrastructure.driven_adapter.bd.repository.PermissionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionDomainUseCase{
    private final PermissionGateway permissionGateway;

    public ResponseHttpDTO<Permission> createPermission(Permission permission) {
        try {
            Permission savedPermission = permissionGateway.save(permission);

            return new ResponseHttpDTO<>("200", "Permiso creado correctamente",
                    savedPermission);

        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500",
                    "Error al guardar el permiso: " + e.getMessage(), null);
        } catch (Exception e) {

            return new ResponseHttpDTO<>("500",
                    "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<List<Permission>> getAllPermissions() {
        try {
            List<Permission> permissions = permissionGateway.findAll();

            if (permissions.isEmpty()) {
                return new ResponseHttpDTO<>("204",
                        "No hay permisos disponibles", null);
            }

            return new ResponseHttpDTO<>("200",
                    "Permisos obtenidos correctamente", permissions);
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500",
                    "Error al obtener los permisos: " + e.getMessage(), null);

        } catch (Exception e) {
            return new ResponseHttpDTO<>("500",
                    "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<Permission> getPermissionById(UUID id) {

        try {
            Optional<Permission> permission = permissionGateway.findById(id);
            if (permission.isPresent()) {
                return new ResponseHttpDTO<>("200",
                        "Permiso obtenido correctamente", permission.get());
            } else {
                return new ResponseHttpDTO<>("404",
                        "El permiso no existe", null);
            }
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500",
                    "Error al obtener la informacion del permiso: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500",
                    "Error al obtener la informacion del permiso: " + e.getMessage(), null);
        }

    }

}

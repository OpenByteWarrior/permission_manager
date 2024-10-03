package com.permissions.domain.usecases;

import com.permissions.domain.models.dto.ResponseHttpDTO;
import com.permissions.infrastructure.driven_adapter.bd.entity.Permission;
import com.permissions.infrastructure.driven_adapter.bd.repository.PermissionRepository;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PermissionDomainUseCase {
    private final PermissionRepository permissionRepository;

    public PermissionDomainUseCase(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public ResponseEntity<ResponseHttpDTO<Permission>> createPermission(Permission permission) {
        try {
            Permission savedPermission = permissionRepository.save(permission);

            ResponseHttpDTO<Permission> response = new ResponseHttpDTO<>("200", "Permiso creado correctamente",
                    savedPermission);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (DataAccessException e) {
            ResponseHttpDTO<Permission> errorResponse = new ResponseHttpDTO<>("500",
                    "Error al guardar el permiso: " + e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {

            ResponseHttpDTO<Permission> errorResponse = new ResponseHttpDTO<>("500",
                    "Ocurrió un error inesperado: " + e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseHttpDTO<List<Permission>>> getAllPermissions() {
        try {
            List<Permission> permissions = permissionRepository.findAll();

            if (permissions.isEmpty()) {
                ResponseHttpDTO<List<Permission>> emptyResponse = new ResponseHttpDTO<>("204",
                        "No hay permisos disponibles", null);
                return new ResponseEntity<>(emptyResponse, HttpStatus.NO_CONTENT);
            }

            ResponseHttpDTO<List<Permission>> response = new ResponseHttpDTO<>("200",
                    "Permisos obtenidos correctamente", permissions);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (DataAccessException e) {
            ResponseHttpDTO<List<Permission>> errorResponse = new ResponseHttpDTO<>("500",
                    "Error al obtener los permisos: " + e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            ResponseHttpDTO<List<Permission>> errorResponse = new ResponseHttpDTO<>("500",
                    "Ocurrió un error inesperado: " + e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseHttpDTO<Permission>> getPermissionById(UUID id) {

        try {
            Optional<Permission> permission = permissionRepository.findById(id);
            if (permission.isPresent()) {
                ResponseHttpDTO<Permission> response = new ResponseHttpDTO<>("200",
                        "Permiso obtenido correctamente", permission.get());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                ResponseHttpDTO<Permission> response = new ResponseHttpDTO<>("404",
                        "El permiso no existe", null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            ResponseHttpDTO<Permission> errorResponse = new ResponseHttpDTO<>("500",
                    "Error al obtener la informacion del permiso: " + e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            ResponseHttpDTO<Permission> errorResponse = new ResponseHttpDTO<>("500",
                    "Error al obtener la informacion del permiso: " + e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

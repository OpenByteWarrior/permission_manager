package com.permission_management.application.usecase;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.permission_management.application.dto.common.PermissionDTO;
import com.permission_management.application.dto.response.ResponseHttpDTO;
import com.permission_management.domain.models.GroupPermissionGateway;
import com.permission_management.domain.models.PermissionGateway;
import com.permission_management.infrastructure.persistence.entity.Permission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.UUID;

class PermissionUseCaseTest {

    @Mock
    private PermissionGateway permissionGateway;

    @Mock
    private GroupPermissionGateway groupPermissionGateway;

    @Mock
    private ModelMapper modelMapper;

    private PermissionUseCase permissionUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        permissionUseCase = new PermissionUseCase(permissionGateway, groupPermissionGateway, modelMapper);
    }

    @Test
    void testCreatePermission_Success() {
        UUID id = UUID.randomUUID();
        Permission permission = new Permission(id, "Permission Name", "Permission Description", null);
        PermissionDTO permissionDTO = new PermissionDTO(id, "Permission Name", "Permission Description");

        when(permissionGateway.save(any(Permission.class))).thenReturn(permission);
        when(modelMapper.map(any(), eq(PermissionDTO.class))).thenReturn(permissionDTO);

        ResponseHttpDTO<PermissionDTO> response = permissionUseCase.createPermission(permission);

        assertEquals("200", response.getStatus());
        assertEquals("Permiso creado correctamente", response.getMessage());
        assertNotNull(response.getResponse());
        assertEquals(permissionDTO.getId(), response.getResponse().getId());
    }

    @Test
    void testCreatePermission_Error() {
        Permission permission = new Permission(UUID.randomUUID(), "Permission Name", "Permission Description", null);

        when(permissionGateway.save(any(Permission.class))).thenThrow(new RuntimeException("Database error"));

        ResponseHttpDTO<PermissionDTO> response = permissionUseCase.createPermission(permission);

        assertEquals("500", response.getStatus());
        assertEquals("Ocurrió un error inesperado: Database error", response.getMessage());
        assertNull(response.getResponse());
    }
    @Test
    void testGetAllPermissions_Success() {
        List<Permission> permissions = List.of(
                new Permission(UUID.randomUUID(), "Permission 1", "Description 1", null),
                new Permission(UUID.randomUUID(), "Permission 2", "Description 2", null)
        );

        List<PermissionDTO> permissionDTOs = permissions.stream()
                .map(permission -> new PermissionDTO(permission.getId(), permission.getName(), permission.getDescription()))
                .toList();

        when(permissionGateway.findAll()).thenReturn(permissions);
        when(modelMapper.map(any(), eq(PermissionDTO.class))).thenReturn(permissionDTOs.get(0), permissionDTOs.get(1));

        ResponseHttpDTO<List<PermissionDTO>> response = permissionUseCase.getAllPermissions();

        assertEquals("200", response.getStatus());
        assertEquals("Permisos obtenidos correctamente", response.getMessage());
        assertNotNull(response.getResponse());
        assertEquals(2, response.getResponse().size());
    }

    @Test
    void testGetAllPermissions_NoPermissions() {
        when(permissionGateway.findAll()).thenReturn(List.of());

        ResponseHttpDTO<List<PermissionDTO>> response = permissionUseCase.getAllPermissions();

        assertEquals("204", response.getStatus());
        assertEquals("No hay permisos disponibles", response.getMessage());
        assertNull(response.getResponse());
    }

    @Test
    void testGetAllPermissions_Error() {
        when(permissionGateway.findAll()).thenThrow(new RuntimeException("Database error"));

        ResponseHttpDTO<List<PermissionDTO>> response = permissionUseCase.getAllPermissions();

        assertEquals("500", response.getStatus());
        assertEquals("Ocurrió un error inesperado: Database error", response.getMessage());
        assertNull(response.getResponse());
    }
    @Test
    void testGetPermissionById_Success() {
        UUID id = UUID.randomUUID();
        Permission permission = new Permission(id, "Permission Name", "Permission Description", null);
        PermissionDTO permissionDTO = new PermissionDTO(id, "Permission Name", "Permission Description");

        when(permissionGateway.findById(id)).thenReturn(java.util.Optional.of(permission));
        when(modelMapper.map(permission, PermissionDTO.class)).thenReturn(permissionDTO);

        ResponseHttpDTO<PermissionDTO> response = permissionUseCase.getPermissionById(id);

        assertEquals("200", response.getStatus());
        assertEquals("Permiso obtenido correctamente", response.getMessage());
        assertNotNull(response.getResponse());
    }

    @Test
    void testGetPermissionById_NotFound() {
        UUID id = UUID.randomUUID();

        when(permissionGateway.findById(id)).thenReturn(java.util.Optional.empty());

        ResponseHttpDTO<PermissionDTO> response = permissionUseCase.getPermissionById(id);

        assertEquals("404", response.getStatus());
        assertEquals("El permiso no existe", response.getMessage());
        assertNull(response.getResponse());
    }

    @Test
    void testGetPermissionById_Error() {
        UUID id = UUID.randomUUID();

        when(permissionGateway.findById(id)).thenThrow(new RuntimeException("Database error"));

        ResponseHttpDTO<PermissionDTO> response = permissionUseCase.getPermissionById(id);

        assertEquals("500", response.getStatus());
        assertEquals("Ocurrió un error inesperado: Database error", response.getMessage());
        assertNull(response.getResponse());
    }
    @Test
    void testDeletePermissionById_Success() {
        UUID id = UUID.randomUUID();
        Permission permission = new Permission(id, "Permission Name", "Permission Description", null);

        when(permissionGateway.findById(id)).thenReturn(java.util.Optional.of(permission));

        ResponseHttpDTO<String> response = permissionUseCase.deletePermissionById(id);

        assertEquals("200", response.getStatus());
        assertEquals("Permiso eliminado correctamente", response.getMessage());
        assertEquals("OK", response.getResponse());
    }

    @Test
    void testDeletePermissionById_NotFound() {
        UUID id = UUID.randomUUID();

        when(permissionGateway.findById(id)).thenReturn(java.util.Optional.empty());

        ResponseHttpDTO<String> response = permissionUseCase.deletePermissionById(id);

        assertEquals("404", response.getStatus());
        assertEquals("El permiso no existe", response.getMessage());
        assertEquals("NOT FOUND", response.getResponse());
    }

    @Test
    void testDeletePermissionById_Error() {
        UUID id = UUID.randomUUID();

        when(permissionGateway.findById(id)).thenThrow(new RuntimeException("Database error"));

        ResponseHttpDTO<String> response = permissionUseCase.deletePermissionById(id);

        assertEquals("500", response.getStatus());
        assertEquals("Ocurrió un error inesperado: Database error", response.getMessage());
        assertNull(response.getResponse());
    }
}

package com.permission_management.application.usecase;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.permission_management.application.dto.common.PermissionDTO;
import com.permission_management.application.dto.response.ResponseHttpDTO;
import com.permission_management.application.service.CrudService;
import com.permission_management.domain.models.PermissionGateway;
import com.permission_management.infrastructure.persistence.entity.Permission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class PermissionUseCaseTest {

    @Mock
    private PermissionGateway permissionGateway;

    @Mock
    private CrudService crudService;

    @InjectMocks
    private PermissionUseCase permissionUseCase;

    private Permission permission;
    private PermissionDTO permissionDTO;

    @BeforeEach
    void setUp() {
        permission = new Permission(UUID.randomUUID(), "Permission Name", "Permission Description", null);
        permissionDTO = new PermissionDTO(permission.getId(), permission.getName(), permission.getDescription());
    }

    @Test
    void testCreatePermission_Success() {
        when(crudService.create(permission, permissionGateway, PermissionDTO.class, "permiso"))
                .thenReturn(new ResponseHttpDTO<>("200", "permiso creado correctamente", permissionDTO));

        ResponseHttpDTO<PermissionDTO> response = permissionUseCase.createPermission(permission);

        assertEquals("200", response.getStatus());
        assertEquals("permiso creado correctamente", response.getMessage());
        assertNotNull(response.getResponse());
    }

    @Test
    void testGetAllPermissions_Success() {
        when(crudService.getAllResource(any(PermissionGateway.class), eq(PermissionDTO.class), eq("permisos")))
                .thenReturn(new ResponseHttpDTO<>("200", "permisos obtenidos correctamente", new ArrayList<>()))
        ;

        ResponseHttpDTO<List<PermissionDTO>> response = permissionUseCase.getAllPermissions();

        assertEquals("200", response.getStatus());
        assertEquals("permisos obtenidos correctamente", response.getMessage());
        assertNotNull(response.getResponse());
    }

    @Test
    void testGetPermissionById_Success() {
        UUID id = UUID.randomUUID();
        Permission permission = new Permission();
        permission.setId(id);

        when(crudService.getResourceById(eq(id), any(PermissionGateway.class), eq(PermissionDTO.class), eq("permiso")))
                .thenReturn(new ResponseHttpDTO<>("200", "permiso obtenido correctamente",permissionDTO));

        ResponseHttpDTO<PermissionDTO> response = permissionUseCase.getPermissionById(id);

        assertEquals("200", response.getStatus());
        assertEquals("permiso obtenido correctamente", response.getMessage());
        assertNotNull(response.getResponse());
    }

    @Test
    void testDeletePermissionById_Success() {
        UUID id = UUID.randomUUID();

        when(crudService.deleteResourceById(eq(id), any(PermissionGateway.class), eq("permiso")))
                .thenReturn(new ResponseHttpDTO<>("200", "permiso eliminado correctamente", "OK"));

        ResponseHttpDTO<String> response = permissionUseCase.deletePermissionById(id);

        assertEquals("200", response.getStatus());
        assertEquals("permiso eliminado correctamente", response.getMessage());
        assertEquals("OK", response.getResponse());
    }
}

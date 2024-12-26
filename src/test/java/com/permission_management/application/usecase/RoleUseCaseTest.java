package com.permission_management.application.usecase;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.permission_management.application.dto.common.RoleDTO;
import com.permission_management.application.dto.request.RequestRoleBodyDTO;
import com.permission_management.application.dto.response.ResponseHttpDTO;
import com.permission_management.application.service.CrudService;
import com.permission_management.domain.models.GroupPermissionGateway;
import com.permission_management.domain.models.RoleGateway;
import com.permission_management.infrastructure.persistence.entity.GroupPermission;
import com.permission_management.infrastructure.persistence.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class RoleUseCaseTest {

    @Mock
    private RoleGateway roleGateway;

    @Mock
    private GroupPermissionGateway groupPermissionGateway;

    @Mock
    private CrudService crudService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RoleUseCase roleUseCase;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateRole_Success() {
        RequestRoleBodyDTO request = new RequestRoleBodyDTO();

        request.setName("Admin");
        request.setGroupPermissionIDs(Collections.singleton(UUID.randomUUID()));

        GroupPermission groupPermission = new GroupPermission();
        groupPermission.setId(UUID.randomUUID());
        Role role = new Role();
        role.setId(UUID.randomUUID());

        when(groupPermissionGateway.findById(any(UUID.class))).thenReturn(Optional.of(groupPermission));
        when(modelMapper.map(request, Role.class)).thenReturn(role);
        when(crudService.create(any(Role.class), eq(roleGateway), eq(RoleDTO.class), eq("rol")))
                .thenReturn(new ResponseHttpDTO<>("200", "rol creado correctamente", new RoleDTO()));

        ResponseHttpDTO<RoleDTO> response = roleUseCase.createRole(request);

        assertEquals("200", response.getStatus());
        assertEquals("rol creado correctamente", response.getMessage());
        assertNotNull(response.getResponse());
    }


    @Test
    void testGetAllRoles_Success() {

        when(crudService.getAllResource(any(RoleGateway.class), eq(RoleDTO.class), eq("roles")))
                .thenReturn(new ResponseHttpDTO<>("200", "roles obtenidos correctamente", new ArrayList<>()))
        ;

        ResponseHttpDTO<List<RoleDTO>> response = roleUseCase.getAllRoles();

        assertEquals("200", response.getStatus());
        assertEquals("roles obtenidos correctamente", response.getMessage());
        assertNotNull(response.getResponse());
    }

    @Test
    void testGetRoleById_Success() {

        UUID id = UUID.randomUUID();
        Role role = new Role();
        role.setId(id);

        when(crudService.getResourceById(eq(id), any(RoleGateway.class), eq(RoleDTO.class), eq("rol")))
                .thenReturn(new ResponseHttpDTO<>("200", "rol obtenido correctamente", new RoleDTO()));

        ResponseHttpDTO<RoleDTO> response = roleUseCase.getRoleById(id);

        assertEquals("200", response.getStatus());
        assertEquals("rol obtenido correctamente", response.getMessage());
        assertNotNull(response.getResponse());
    }

    @Test
    void testDeleteRoleById_Success() {

        UUID id = UUID.randomUUID();

        when(crudService.deleteResourceById(eq(id), any(RoleGateway.class), eq("rol")))
                .thenReturn(new ResponseHttpDTO<>("200", "rol eliminado correctamente", "OK"));

        ResponseHttpDTO<String> response = roleUseCase.deleteRoleById(id);

        assertEquals("200", response.getStatus());
        assertEquals("rol eliminado correctamente", response.getMessage());
        assertEquals("OK", response.getResponse());
    }

}


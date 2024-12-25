package com.permission_management.application.usecase;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.permission_management.application.dto.common.RoleDTO;
import com.permission_management.application.dto.request.RequestRoleBodyDTO;
import com.permission_management.application.dto.response.ResponseHttpDTO;
import com.permission_management.domain.models.GroupPermissionGateway;
import com.permission_management.domain.models.RoleGateway;
import com.permission_management.infrastructure.persistence.entity.GroupPermission;
import com.permission_management.infrastructure.persistence.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;

import java.util.*;

class RoleUseCaseTest {

    @Mock
    private RoleGateway roleGateway;

    @Mock
    private GroupPermissionGateway groupPermissionGateway;

    @Mock
    private ModelMapper modelMapper;

    private RoleUseCase roleUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roleUseCase = new RoleUseCase(roleGateway, groupPermissionGateway, modelMapper);
    }

    @Test
    void testCreateRole_Success() {

        RequestRoleBodyDTO requestDTO = new RequestRoleBodyDTO();
        requestDTO.setGroupPermissionIDs(Set.of(UUID.randomUUID(), UUID.randomUUID()));

        GroupPermission groupPermission1 = mock(GroupPermission.class);
        GroupPermission groupPermission2 = mock(GroupPermission.class);
        when(groupPermissionGateway.findById(any(UUID.class)))
                .thenReturn(Optional.of(groupPermission1), Optional.of(groupPermission2));

        Role role = mock(Role.class);
        when(modelMapper.map(requestDTO, Role.class)).thenReturn(role);

        when(roleGateway.save(role)).thenReturn(role);

        RoleDTO roleDTO = new RoleDTO();
        when(modelMapper.map(role, RoleDTO.class)).thenReturn(roleDTO);

        ResponseHttpDTO<RoleDTO> response = roleUseCase.createRole(requestDTO);

        assertEquals("200", response.getStatus());
        assertEquals("Rol creado correctamente", response.getMessage());
    }


    @Test
    void testCreateRole__Failure_GroupPermissionNotFound() {

        UUID groupId = UUID.randomUUID();
        Set<UUID> groupPermissionIDs = Set.of(groupId);
        RequestRoleBodyDTO roleBodyDTO = new RequestRoleBodyDTO();
        roleBodyDTO.setGroupPermissionIDs(groupPermissionIDs);

        when(groupPermissionGateway.findById(groupId)).thenReturn(Optional.empty());

        ResponseHttpDTO<RoleDTO> response = roleUseCase.createRole(roleBodyDTO);

        assertEquals("500", response.getStatus());
    }

    @Test
    void testGetAllRoles() {

        Role role = new Role();
        role.setId(UUID.randomUUID());
        List<Role> roleList = List.of(role);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());

        when(roleGateway.findAll()).thenReturn(roleList);
        when(modelMapper.map(role, RoleDTO.class)).thenReturn(roleDTO);

        ResponseHttpDTO<List<RoleDTO>> response = roleUseCase.getAllRoles();

        assertEquals("200", response.getStatus());
        assertEquals("Roles obtenidos correctamente", response.getMessage());
        assertNotNull(response.getResponse());
        assertEquals(1, response.getResponse().size());
    }

    @Test
    void testGetAllRoles_NoRoles() {

        when(roleGateway.findAll()).thenReturn(Collections.emptyList());

        ResponseHttpDTO<List<RoleDTO>> response = roleUseCase.getAllRoles();

        assertEquals("204", response.getStatus());
        assertEquals("No hay roles disponibles", response.getMessage());
    }

    @Test
    void testGetRoleById() {

        UUID roleId = UUID.randomUUID();
        Role role = new Role();
        role.setId(roleId);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(roleId);

        when(roleGateway.findById(roleId)).thenReturn(Optional.of(role));
        when(modelMapper.map(role, RoleDTO.class)).thenReturn(roleDTO);

        ResponseHttpDTO<RoleDTO> response = roleUseCase.getRoleById(roleId);

        assertEquals("200", response.getStatus());
        assertEquals("Rol obtenido correctamente", response.getMessage());
        assertNotNull(response.getResponse());
        assertEquals(roleId, response.getResponse().getId());
    }

    @Test
    void testGetRoleById_RoleNotFound() {

        UUID roleId = UUID.randomUUID();
        when(roleGateway.findById(roleId)).thenReturn(Optional.empty());

        ResponseHttpDTO<RoleDTO> response = roleUseCase.getRoleById(roleId);

        assertEquals("404", response.getStatus());
        assertEquals("El rol no existe", response.getMessage());
    }

    @Test
    void testDeleteRoleById() {

        UUID roleId = UUID.randomUUID();
        Role role = new Role();
        role.setId(roleId);

        when(roleGateway.findById(roleId)).thenReturn(Optional.of(role));

        ResponseHttpDTO<String> response = roleUseCase.deleteRoleById(roleId);

        assertEquals("200", response.getStatus());
        assertEquals("Rol eliminado correctamente", response.getMessage());
    }

    @Test
    void testDeleteRoleById_RoleNotFound() {

        UUID roleId = UUID.randomUUID();
        when(roleGateway.findById(roleId)).thenReturn(Optional.empty());

        ResponseHttpDTO<String> response = roleUseCase.deleteRoleById(roleId);

        assertEquals("404", response.getStatus());
        assertEquals("El rol no existe", response.getMessage());
    }
}


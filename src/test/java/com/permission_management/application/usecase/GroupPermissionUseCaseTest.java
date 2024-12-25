package com.permission_management.application.usecase;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.permission_management.application.dto.common.GroupPermissionDTO;
import com.permission_management.application.dto.common.RoleDTO;
import com.permission_management.application.dto.request.RequestAssignAndRemoveBodyDTO;
import com.permission_management.application.dto.request.RequestGroupPermissionBodyDTO;
import com.permission_management.application.dto.response.ResponseHttpDTO;
import com.permission_management.domain.models.GroupPermissionGateway;
import com.permission_management.domain.models.PermissionGateway;
import com.permission_management.domain.models.RoleGateway;
import com.permission_management.infrastructure.persistence.entity.GroupPermission;
import com.permission_management.infrastructure.persistence.entity.Permission;
import com.permission_management.infrastructure.persistence.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;

import java.util.*;

public class GroupPermissionUseCaseTest {

    @Mock
    private GroupPermissionGateway groupPermissionGateway;

    @Mock
    private PermissionGateway permissionGateway;

    @Mock
    private RoleGateway roleGateway;

    @Mock
    private ModelMapper modelMapper;

    private GroupPermissionUseCase groupPermissionUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        groupPermissionUseCase = new GroupPermissionUseCase(groupPermissionGateway, permissionGateway, roleGateway, modelMapper);
    }

    @Test
    void testCreateGroupPermission_Success() {

        RequestGroupPermissionBodyDTO requestDTO = new RequestGroupPermissionBodyDTO();
        requestDTO.setPermissionIds(Set.of(UUID.randomUUID(), UUID.randomUUID()));

        Permission permission1 = mock(Permission.class);
        Permission permission2 = mock(Permission.class);

        when(permissionGateway.findById(any())).thenReturn(Optional.of(permission1), Optional.of(permission2));

        GroupPermission groupPermission = mock(GroupPermission.class);
        when(modelMapper.map(requestDTO, GroupPermission.class)).thenReturn(groupPermission);
        when(groupPermissionGateway.save(groupPermission)).thenReturn(groupPermission);
        when(modelMapper.map(groupPermission, GroupPermissionDTO.class)).thenReturn(new GroupPermissionDTO());

        ResponseHttpDTO<GroupPermissionDTO> response = groupPermissionUseCase.createGroupPermission(requestDTO);

        assertEquals("200", response.getStatus());
        assertEquals("Grupo de permisos creado correctamente", response.getMessage());
    }
    @Test
    void testCreateGroupPermission_Failure_PermissionNotFound() {

        RequestGroupPermissionBodyDTO requestDTO = new RequestGroupPermissionBodyDTO();
        requestDTO.setPermissionIds(Set.of(UUID.randomUUID(), UUID.randomUUID()));

        when(permissionGateway.findById(any())).thenReturn(Optional.empty());

        ResponseHttpDTO<GroupPermissionDTO> response = groupPermissionUseCase.createGroupPermission(requestDTO);

        assertEquals("500", response.getStatus());
    }
    @Test
    void testGetAllGroupsPermissions_Success() {

        List<GroupPermission> groupPermissions = new ArrayList<>();
        GroupPermission groupPermission = mock(GroupPermission.class);
        groupPermissions.add(groupPermission);

        when(groupPermissionGateway.findAll()).thenReturn(groupPermissions);
        when(modelMapper.map(groupPermission, GroupPermissionDTO.class)).thenReturn(new GroupPermissionDTO());

        ResponseHttpDTO<List<GroupPermissionDTO>> response = groupPermissionUseCase.getAllGroupsPermissions();

        assertEquals("200", response.getStatus());
        assertEquals("Grupos de permisos obtenidos correctamente", response.getMessage());
        assertNotNull(response.getResponse());
        assertEquals(1, response.getResponse().size());
    }

    @Test
    void testGetGroupPermissionById_Success() {

        UUID groupPermissionId = UUID.randomUUID();
        GroupPermission groupPermission = mock(GroupPermission.class);
        when(groupPermissionGateway.findById(groupPermissionId)).thenReturn(Optional.of(groupPermission));
        when(modelMapper.map(groupPermission, GroupPermissionDTO.class)).thenReturn(new GroupPermissionDTO());

        ResponseHttpDTO<GroupPermissionDTO> response = groupPermissionUseCase.getGroupPermissionById(groupPermissionId);

        assertEquals("200", response.getStatus());
        assertEquals("Grupo de permisos obtenido correctamente", response.getMessage());
    }

    @Test
    void testGetGroupPermissionById_NotFound() {
        // Arrange
        UUID groupPermissionId = UUID.randomUUID();
        when(groupPermissionGateway.findById(groupPermissionId)).thenReturn(Optional.empty());

        ResponseHttpDTO<GroupPermissionDTO> response = groupPermissionUseCase.getGroupPermissionById(groupPermissionId);

        assertEquals("404", response.getStatus());
        assertEquals("El grupo de permisos no existe", response.getMessage());
    }

    @Test
    void testDeleteGroupPermissionById_Success() {

        UUID groupPermissionId = UUID.randomUUID();
        GroupPermission groupPermission = mock(GroupPermission.class);
        when(groupPermissionGateway.findById(groupPermissionId)).thenReturn(Optional.of(groupPermission));

        ResponseHttpDTO<String> response = groupPermissionUseCase.deleteGroupPermissionById(groupPermissionId);

        assertEquals("200", response.getStatus());
        assertEquals("Grupo de permisos eliminado correctamente", response.getMessage());
    }

    @Test
    void testDeleteGroupPermissionById_NotFound() {

        UUID groupPermissionId = UUID.randomUUID();
        when(groupPermissionGateway.findById(groupPermissionId)).thenReturn(Optional.empty());

        ResponseHttpDTO<String> response = groupPermissionUseCase.deleteGroupPermissionById(groupPermissionId);

        assertEquals("404", response.getStatus());
        assertEquals("El grupo de permisos no existe", response.getMessage());
    }

    @Test
    void testAssignGroupPermissionToRole_Success() {

        RequestAssignAndRemoveBodyDTO body = mock(RequestAssignAndRemoveBodyDTO.class);
        Set<UUID> resourcesIds = Set.of(UUID.randomUUID(), UUID.randomUUID());
        when(body.getResourcesIds()).thenReturn(resourcesIds);
        when(roleGateway.findById(any())).thenReturn(Optional.of(mock(Role.class)));

        Set<GroupPermission> groupPermissions = new HashSet<>();
        for (UUID id : resourcesIds) {
            groupPermissions.add(mock(GroupPermission.class));
        }
        when(groupPermissionGateway.findById(any())).thenReturn(Optional.of(mock(GroupPermission.class)));

        Role role = mock(Role.class);
        when(role.getGroupPermissions()).thenReturn(new HashSet<>());
        when(roleGateway.save(role)).thenReturn(role);

        ResponseHttpDTO<RoleDTO> response = groupPermissionUseCase.assignGroupPermissionToRole(body);

        assertEquals("200", response.getStatus());
        assertEquals("Grupos de permisos asignados correctamente al rol", response.getMessage());
    }
}

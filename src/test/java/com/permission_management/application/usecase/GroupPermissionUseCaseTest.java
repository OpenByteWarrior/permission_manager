package com.permission_management.application.usecase;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.permission_management.application.dto.common.GroupPermissionDTO;
import com.permission_management.application.dto.request.RequestGroupPermissionBodyDTO;
import com.permission_management.application.dto.response.ResponseHttpDTO;
import com.permission_management.application.service.CrudService;
import com.permission_management.domain.models.GroupPermissionGateway;
import com.permission_management.domain.models.PermissionGateway;
import com.permission_management.infrastructure.persistence.entity.GroupPermission;
import com.permission_management.infrastructure.persistence.entity.Permission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class GroupPermissionUseCaseTest {

    @Mock
    private GroupPermissionGateway groupPermissionGateway;

    @Mock
    private PermissionGateway permissionGateway;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CrudService crudService;

    @InjectMocks
    private GroupPermissionUseCase groupPermissionUseCase;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testCreateGroupPermission_Success() {
        RequestGroupPermissionBodyDTO request = new RequestGroupPermissionBodyDTO();
        request.setName("group1");
        request.setPermissionIds(Collections.singleton(UUID.randomUUID()));

        Permission permission = new Permission();
        permission.setId(UUID.randomUUID());
        GroupPermission groupPermission = new GroupPermission();
        groupPermission.setId(UUID.randomUUID());

        // Configura los mocks con valores específicos
        when(permissionGateway.findById(any(UUID.class))).thenReturn(Optional.of(permission));
        when(modelMapper.map(request, GroupPermission.class)).thenReturn(groupPermission);
        when(crudService.create(any(GroupPermission.class), eq(groupPermissionGateway), eq(GroupPermissionDTO.class), eq("grupo de permisos")))
                .thenReturn(new ResponseHttpDTO<>("200", "grupo de permisos creado correctamente", new GroupPermissionDTO()));

        ResponseHttpDTO<GroupPermissionDTO> response = groupPermissionUseCase.createGroupPermission(request);

        assertEquals("200", response.getStatus());
        assertEquals("grupo de permisos creado correctamente", response.getMessage());
        assertNotNull(response.getResponse());
    }


    @Test
    void testGetAllGroupsPermissions_Success() {

        when(crudService.getAllResource(any(GroupPermissionGateway.class), eq(GroupPermissionDTO.class), eq("grupos de permisos")))
                .thenReturn(new ResponseHttpDTO<>("200", "grupos de permisos obtenidos correctamente", new ArrayList<>()))
        ;

        ResponseHttpDTO<List<GroupPermissionDTO>> response = groupPermissionUseCase.getAllGroupsPermissions();

        assertEquals("200", response.getStatus());
        assertEquals("grupos de permisos obtenidos correctamente", response.getMessage());
        assertNotNull(response.getResponse());
    }

    @Test
    void testGetGroupPermissionById_Success() {

        UUID id = UUID.randomUUID();
        GroupPermission groupPermission = new GroupPermission();
        groupPermission.setId(id);

        when(crudService.getResourceById(eq(id), any(GroupPermissionGateway.class), eq(GroupPermissionDTO.class), eq("grupo de permisos")))
                .thenReturn(new ResponseHttpDTO<>("200", "grupo de permisos obtenido correctamente", new GroupPermissionDTO()));

        ResponseHttpDTO<GroupPermissionDTO> response = groupPermissionUseCase.getGroupPermissionById(id);

        assertEquals("200", response.getStatus());
        assertEquals("grupo de permisos obtenido correctamente", response.getMessage());
        assertNotNull(response.getResponse());
    }

    @Test
    void testDeleteGroupPermissionById_Success() {

        UUID id = UUID.randomUUID();

        when(crudService.deleteResourceById(eq(id), any(GroupPermissionGateway.class), eq("grupo de permisos")))
                .thenReturn(new ResponseHttpDTO<>("200", "grupo de permisos eliminado correctamente", "OK"));

        ResponseHttpDTO<String> response = groupPermissionUseCase.deleteGroupPermissionById(id);

        assertEquals("200", response.getStatus());
        assertEquals("grupo de permisos eliminado correctamente", response.getMessage());
        assertEquals("OK", response.getResponse());
    }
}

package com.permission_management.application.usecase;

import com.permission_management.application.dto.request.RequestAssignAndRemoveBodyDTO;
import com.permission_management.application.dto.common.GroupPermissionDTO;
import com.permission_management.application.dto.common.ModuleComponentDTO;
import com.permission_management.application.dto.response.ResponseHttpDTO;
import com.permission_management.infrastructure.persistence.entity.GroupPermission;
import com.permission_management.infrastructure.persistence.entity.ModuleComponent;
import com.permission_management.domain.models.GroupPermissionGateway;
import com.permission_management.domain.models.ModuleComponentGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuleComponentUseCaseTest {

    @Mock
    private ModuleComponentGateway moduleComponentGateway;

    @Mock
    private GroupPermissionGateway groupPermissionGateway;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ModuleComponentUseCase moduleComponentUseCase;

    private ModuleComponent moduleComponent;
    private GroupPermission groupPermission;
    private ModuleComponentDTO moduleComponentDTO;
    private GroupPermissionDTO groupPermissionDTO;

    @BeforeEach
    void setUp() {
        moduleComponent = new ModuleComponent(UUID.randomUUID(), "Component1", "Description1", new HashSet<>());
        groupPermission = new GroupPermission(UUID.randomUUID(), "Group1", "Description1", new HashSet<>(), new HashSet<>(), new HashSet<>());
        moduleComponentDTO = new ModuleComponentDTO(moduleComponent.getId(), moduleComponent.getName(), moduleComponent.getDescription());
        groupPermissionDTO = new GroupPermissionDTO(groupPermission.getId(), groupPermission.getName(), groupPermission.getDescription(), new HashSet<>(), new HashSet<>());
    }

    @Test
    void testCreateComponent_Success() {
        when(moduleComponentGateway.save(any(ModuleComponent.class))).thenReturn(moduleComponent);
        when(modelMapper.map(any(ModuleComponent.class), eq(ModuleComponentDTO.class))).thenReturn(moduleComponentDTO);

        ResponseHttpDTO<ModuleComponentDTO> response = moduleComponentUseCase.createComponent(moduleComponent);

        assertEquals("200", response.getStatus());
        assertEquals("Componente creado correctamente", response.getMessage());
        assertNotNull(response.getResponse());
    }

    @Test
    void testCreateComponent_DataAccessException() {
        when(moduleComponentGateway.save(any(ModuleComponent.class))).thenThrow(new DataAccessException("Database error") {});

        ResponseHttpDTO<ModuleComponentDTO> response = moduleComponentUseCase.createComponent(moduleComponent);

        assertEquals("500", response.getStatus());
        assertEquals("Error al guardar el componente: Database error", response.getMessage());
    }

    @Test
    void testAssignComponentToGroup_Success() {
        RequestAssignAndRemoveBodyDTO body = new RequestAssignAndRemoveBodyDTO(groupPermission.getId(), Set.of(moduleComponent.getId()));
        when(groupPermissionGateway.findById(groupPermission.getId())).thenReturn(Optional.of(groupPermission));
        when(moduleComponentGateway.findById(moduleComponent.getId())).thenReturn(Optional.of(moduleComponent));
        when(modelMapper.map(any(GroupPermission.class), eq(GroupPermissionDTO.class))).thenReturn(groupPermissionDTO);

        ResponseHttpDTO<GroupPermissionDTO> response = moduleComponentUseCase.assignComponentToGroup(body);

        assertEquals("200", response.getStatus());
        assertEquals("Componentes asignados correctamente al grupo de permisos ", response.getMessage());
    }

    @Test
    void testAssignComponentToGroup_ComponentAlreadyAssigned() {
        groupPermission.getComponents().add(moduleComponent);
        RequestAssignAndRemoveBodyDTO body = new RequestAssignAndRemoveBodyDTO(groupPermission.getId(), Set.of(moduleComponent.getId()));

        when(groupPermissionGateway.findById(groupPermission.getId())).thenReturn(Optional.of(groupPermission));
        when(moduleComponentGateway.findById(moduleComponent.getId())).thenReturn(Optional.of(moduleComponent));

        ResponseHttpDTO<GroupPermissionDTO> response = moduleComponentUseCase.assignComponentToGroup(body);

        assertEquals("400", response.getStatus());
        assertEquals("Algunos componentes ya están asignados al grupo de permisos", response.getMessage());
    }

    @Test
    void testAssignComponentToGroup_GroupNotFound() {
        RequestAssignAndRemoveBodyDTO body = new RequestAssignAndRemoveBodyDTO(UUID.randomUUID(), Set.of(moduleComponent.getId()));

        when(groupPermissionGateway.findById(body.getIdContainer())).thenReturn(Optional.empty());

        ResponseHttpDTO<GroupPermissionDTO> response = moduleComponentUseCase.assignComponentToGroup(body);

        assertEquals("400", response.getStatus());
        assertEquals("El grupo de permisos no existe", response.getMessage());
    }

    @Test
    void testRemoveComponentToGroup_Success() {
        groupPermission.getComponents().add(moduleComponent);
        RequestAssignAndRemoveBodyDTO body = new RequestAssignAndRemoveBodyDTO(groupPermission.getId(), Set.of(moduleComponent.getId()));

        when(groupPermissionGateway.findById(groupPermission.getId())).thenReturn(Optional.of(groupPermission));
        when(moduleComponentGateway.findById(moduleComponent.getId())).thenReturn(Optional.of(moduleComponent));
        when(modelMapper.map(any(GroupPermission.class), eq(GroupPermissionDTO.class))).thenReturn(groupPermissionDTO);

        ResponseHttpDTO<GroupPermissionDTO> response = moduleComponentUseCase.removeComponentToGroup(body);

        assertEquals("200", response.getStatus());
        assertEquals("Componentes eliminados correctamente del grupo de permisos", response.getMessage());
    }

    @Test
    void testRemoveComponentToGroup_ComponentNotFoundInGroup() {
        RequestAssignAndRemoveBodyDTO body = new RequestAssignAndRemoveBodyDTO(groupPermission.getId(), Set.of(moduleComponent.getId()));

        when(groupPermissionGateway.findById(groupPermission.getId())).thenReturn(Optional.of(groupPermission));
        when(moduleComponentGateway.findById(moduleComponent.getId())).thenReturn(Optional.of(moduleComponent));

        ResponseHttpDTO<GroupPermissionDTO> response = moduleComponentUseCase.removeComponentToGroup(body);

        assertEquals("400", response.getStatus());
        assertEquals("Ninguno de los componentes especificados está asignado al grupo de permisos", response.getMessage());
    }
}

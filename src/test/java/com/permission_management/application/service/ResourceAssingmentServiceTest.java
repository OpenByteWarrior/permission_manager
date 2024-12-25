package com.permission_management.application.service;

import com.permission_management.application.dto.response.ResponseHttpDTO;
import com.permission_management.domain.models.Gateway;
import com.permission_management.domain.models.Resource;
import com.permission_management.domain.models.ResourceContainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceAssignmentServiceTest {

    @Mock
    private Gateway<ResourceContainer<Resource>> containerGateway;

    @Mock
    private Gateway<Resource> resourceGateway;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ResourceAssignmentService resourceAssignmentService;

    @Test
    void testAssignResource_Success() {
        // Arrange
        UUID containerId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();
        ResourceContainer<Resource> mockContainer = mock(ResourceContainer.class);
        Resource mockResource = mock(Resource.class);
        Set<UUID> resourceIds = Set.of(resourceId);

        when(containerGateway.findById(containerId)).thenReturn(Optional.of(mockContainer));
        when(resourceGateway.findById(resourceId)).thenReturn(Optional.of(mockResource));
        when(mockContainer.getResources()).thenReturn(new HashSet<>());
        when(modelMapper.map(any(), any(Class.class))).thenReturn(new Object());

        // Act
        ResponseHttpDTO<?> response = resourceAssignmentService.assignResource(
                containerId, resourceIds, containerGateway, resourceGateway, Object.class, "Contenedor"
        );

        // Assert
        assertEquals("200", response.getStatus());
        assertEquals("Recursos asignados correctamente al Contenedor", response.getMessage());
        verify(containerGateway).save(mockContainer);
    }

    @Test
    void testAssignResource_ResourceAlreadyAssigned() {
        // Arrange
        UUID containerId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();
        ResourceContainer<Resource> mockContainer = mock(ResourceContainer.class);
        Resource mockResource = mock(Resource.class);
        Set<UUID> resourceIds = Set.of(resourceId);

        when(containerGateway.findById(containerId)).thenReturn(Optional.of(mockContainer));
        when(resourceGateway.findById(resourceId)).thenReturn(Optional.of(mockResource));
        when(mockContainer.getResources()).thenReturn(Set.of(mockResource));

        // Act
        ResponseHttpDTO<?> response = resourceAssignmentService.assignResource(
                containerId, resourceIds, containerGateway, resourceGateway, Object.class, "Contenedor"
        );

        // Assert
        assertEquals("400", response.getStatus());
        assertEquals("Algunos recursos ya están asignados al Contenedor", response.getMessage());
        verify(containerGateway, never()).save(mockContainer);
    }

    @Test
    void testRemoveResource_ResourceNotAssigned() {
        // Arrange
        UUID containerId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();
        ResourceContainer<Resource> mockContainer = mock(ResourceContainer.class);
        Resource mockResource = mock(Resource.class);
        Set<UUID> resourceIds = Set.of(resourceId);

        when(containerGateway.findById(containerId)).thenReturn(Optional.of(mockContainer));
        when(resourceGateway.findById(resourceId)).thenReturn(Optional.of(mockResource));
        when(mockContainer.getResources()).thenReturn(new HashSet<>());

        // Act
        ResponseHttpDTO<?> response = resourceAssignmentService.removeResource(
                containerId, resourceIds, containerGateway, resourceGateway, Object.class, "Contenedor"
        );

        // Assert
        assertEquals("400", response.getStatus());
        assertEquals("Ninguno de los recursos especificados está asignado al Contenedor", response.getMessage());
        verify(containerGateway, never()).save(mockContainer);
    }
}

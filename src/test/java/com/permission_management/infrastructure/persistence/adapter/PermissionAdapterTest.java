package com.permission_management.infrastructure.persistence.adapter;

import com.permission_management.infrastructure.persistence.entity.Permission;
import com.permission_management.infrastructure.persistence.repository.PermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PermissionAdapterTest {

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionAdapter permissionAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        // Arrange
        Permission permission = new Permission();
        permission.setId(UUID.randomUUID());
        permission.setName("READ");
        when(permissionRepository.save(permission)).thenReturn(permission);

        // Act
        Permission result = permissionAdapter.save(permission);

        // Assert
        assertNotNull(result);
        assertEquals(permission.getId(), result.getId());
        assertEquals("READ", result.getName());
        verify(permissionRepository, times(1)).save(permission);
    }

    @Test
    void testFindAll() {
        // Arrange
        List<Permission> permissions = List.of(new Permission(), new Permission());
        when(permissionRepository.findAll()).thenReturn(permissions);

        // Act
        List<Permission> result = permissionAdapter.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(permissionRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // Arrange
        UUID id = UUID.randomUUID();
        Permission permission = new Permission();
        permission.setId(id);
        permission.setName("WRITE");
        when(permissionRepository.findById(id)).thenReturn(Optional.of(permission));

        // Act
        Optional<Permission> result = permissionAdapter.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertEquals("WRITE", result.get().getName());
        verify(permissionRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteById() {
        // Arrange
        UUID id = UUID.randomUUID();
        doNothing().when(permissionRepository).deleteById(id);

        // Act
        permissionAdapter.deleteById(id);

        // Assert
        verify(permissionRepository, times(1)).deleteById(id);
    }
}
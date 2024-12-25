package com.permission_management.infrastructure.persistence.adapter;

import com.permission_management.infrastructure.persistence.entity.GroupPermission;
import com.permission_management.infrastructure.persistence.repository.GroupPermissionRepository;
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

class GroupAdapterTest {

    @Mock
    private GroupPermissionRepository groupPermissionRepository;

    @InjectMocks
    private GroupAdapter groupAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        // Arrange
        GroupPermission groupPermission = new GroupPermission();
        groupPermission.setId(UUID.randomUUID());
        when(groupPermissionRepository.save(groupPermission)).thenReturn(groupPermission);

        // Act
        GroupPermission result = groupAdapter.save(groupPermission);

        // Assert
        assertNotNull(result);
        assertEquals(groupPermission.getId(), result.getId());
        verify(groupPermissionRepository, times(1)).save(groupPermission);
    }

    @Test
    void testFindAll() {
        // Arrange
        List<GroupPermission> groupPermissions = List.of(new GroupPermission(), new GroupPermission());
        when(groupPermissionRepository.findAll()).thenReturn(groupPermissions);

        // Act
        List<GroupPermission> result = groupAdapter.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(groupPermissionRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // Arrange
        UUID id = UUID.randomUUID();
        GroupPermission groupPermission = new GroupPermission();
        groupPermission.setId(id);
        when(groupPermissionRepository.findById(id)).thenReturn(Optional.of(groupPermission));

        // Act
        Optional<GroupPermission> result = groupAdapter.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(groupPermissionRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteById() {
        // Arrange
        UUID id = UUID.randomUUID();
        doNothing().when(groupPermissionRepository).deleteById(id);

        // Act
        groupAdapter.deleteById(id);

        // Assert
        verify(groupPermissionRepository, times(1)).deleteById(id);
    }
}

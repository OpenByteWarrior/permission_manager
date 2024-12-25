package com.permission_management.infrastructure.persistence.adapter;

import com.permission_management.infrastructure.persistence.entity.Role;
import com.permission_management.infrastructure.persistence.repository.RoleRepository;
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

class RoleAdapterTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleAdapter roleAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        // Arrange
        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setName("ADMIN");
        when(roleRepository.save(role)).thenReturn(role);

        // Act
        Role result = roleAdapter.save(role);

        // Assert
        assertNotNull(result);
        assertEquals(role.getId(), result.getId());
        assertEquals("ADMIN", result.getName());
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void testFindAll() {
        // Arrange
        List<Role> roles = List.of(new Role(), new Role());
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        List<Role> result = roleAdapter.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // Arrange
        UUID id = UUID.randomUUID();
        Role role = new Role();
        role.setId(id);
        role.setName("USER");
        when(roleRepository.findById(id)).thenReturn(Optional.of(role));

        // Act
        Optional<Role> result = roleAdapter.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertEquals("USER", result.get().getName());
        verify(roleRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteById() {
        // Arrange
        UUID id = UUID.randomUUID();
        doNothing().when(roleRepository).deleteById(id);

        // Act
        roleAdapter.deleteById(id);

        // Assert
        verify(roleRepository, times(1)).deleteById(id);
    }
}

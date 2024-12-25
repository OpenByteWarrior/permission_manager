package com.permission_management.infrastructure.persistence.adapter;

import com.permission_management.infrastructure.persistence.entity.ModuleComponent;
import com.permission_management.infrastructure.persistence.repository.ModuleComponentRepository;
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

class ModuleComponentAdapterTest {

    @Mock
    private ModuleComponentRepository moduleComponentRepository;

    @InjectMocks
    private ModuleComponentAdapter moduleComponentAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        // Arrange
        ModuleComponent moduleComponent = new ModuleComponent();
        moduleComponent.setId(UUID.randomUUID());
        moduleComponent.setName("Component A");
        when(moduleComponentRepository.save(moduleComponent)).thenReturn(moduleComponent);

        // Act
        ModuleComponent result = moduleComponentAdapter.save(moduleComponent);

        // Assert
        assertNotNull(result);
        assertEquals(moduleComponent.getId(), result.getId());
        assertEquals("Component A", result.getName());
        verify(moduleComponentRepository, times(1)).save(moduleComponent);
    }

    @Test
    void testFindAll() {
        // Arrange
        List<ModuleComponent> components = List.of(new ModuleComponent(), new ModuleComponent());
        when(moduleComponentRepository.findAll()).thenReturn(components);

        // Act
        List<ModuleComponent> result = moduleComponentAdapter.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(moduleComponentRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // Arrange
        UUID id = UUID.randomUUID();
        ModuleComponent moduleComponent = new ModuleComponent();
        moduleComponent.setId(id);
        moduleComponent.setName("Component B");
        when(moduleComponentRepository.findById(id)).thenReturn(Optional.of(moduleComponent));

        // Act
        Optional<ModuleComponent> result = moduleComponentAdapter.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertEquals("Component B", result.get().getName());
        verify(moduleComponentRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteById() {
        // Arrange
        UUID id = UUID.randomUUID();
        doNothing().when(moduleComponentRepository).deleteById(id);

        // Act
        moduleComponentAdapter.deleteById(id);

        // Assert
        verify(moduleComponentRepository, times(1)).deleteById(id);
    }
}

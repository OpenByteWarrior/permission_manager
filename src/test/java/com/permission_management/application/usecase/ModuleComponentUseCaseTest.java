package com.permission_management.application.usecase;

import com.permission_management.application.dto.common.ModuleComponentDTO;
import com.permission_management.application.dto.response.ResponseHttpDTO;
import com.permission_management.application.service.CrudService;
import com.permission_management.infrastructure.persistence.entity.ModuleComponent;
import com.permission_management.domain.models.ModuleComponentGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuleComponentUseCaseTest {

    @Mock
    private ModuleComponentGateway moduleComponentGateway;

    @Mock
    private CrudService crudService;

    @InjectMocks
    private ModuleComponentUseCase moduleComponentUseCase;

    private ModuleComponent moduleComponent;
    private ModuleComponentDTO moduleComponentDTO;

    @BeforeEach
    void setUp() {
        moduleComponent = new ModuleComponent(UUID.randomUUID(), "Component1", "Description1", null);
        moduleComponentDTO = new ModuleComponentDTO(moduleComponent.getId(), moduleComponent.getName(), moduleComponent.getDescription());
    }

    @Test
    void testCreateComponent_Success() {
        when(crudService.create(moduleComponent, moduleComponentGateway, ModuleComponentDTO.class, "componente"))
                .thenReturn(new ResponseHttpDTO<>("200", "componente creado correctamente", moduleComponentDTO));

        ResponseHttpDTO<ModuleComponentDTO> response = moduleComponentUseCase.createComponent(moduleComponent);

        assertEquals("200", response.getStatus());
        assertEquals("componente creado correctamente", response.getMessage());
        assertNotNull(response.getResponse());
    }
}

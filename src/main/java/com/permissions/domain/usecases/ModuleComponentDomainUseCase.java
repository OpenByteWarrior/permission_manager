package com.permissions.domain.usecases;


import com.permissions.domain.models.ModuleComponentGateway;
import com.permissions.domain.models.dto.ModuleComponentDTO;
import com.permissions.domain.models.dto.ResponseHttpDTO;
import com.permissions.infrastructure.driven_adapter.bd.entity.ModuleComponent;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuleComponentDomainUseCase {
    private final ModuleComponentGateway moduleComponentGateway;
    private final ModelMapper modelMapper;

    public ResponseHttpDTO<ModuleComponentDTO> createComponent(ModuleComponentDTO moduleComponentDTO) {
        try {
            ModuleComponent moduleComponent = modelMapper.map(moduleComponentDTO, ModuleComponent.class);
            moduleComponentGateway.save(moduleComponent);
            return new ResponseHttpDTO<>("200", "Componente creado correctamente", moduleComponentDTO);

        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al guardar el componente: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<List<ModuleComponentDTO>> getAllComponents() {
        try {
            List<ModuleComponentDTO> allComponents = moduleComponentGateway.findAll().stream()
                    .map(value -> modelMapper.map(value, ModuleComponentDTO.class)).collect(Collectors.toList());

            if (allComponents.isEmpty()) {
                return new ResponseHttpDTO<>("204", "No hay componentes disponibles", null);
            }
            return new ResponseHttpDTO<>("200", "Componentes obtenidos correctamente", allComponents);

        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al obtener los componentes: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<ModuleComponentDTO> getComponentById(UUID id) {

        try {
            return moduleComponentGateway.findById(id).map(
                            moduleComponent -> {
                                ModuleComponentDTO moduleComponentDTO = modelMapper.map(moduleComponent, ModuleComponentDTO.class);
                                return new ResponseHttpDTO<>("200", "Componente obtenido correctamente", moduleComponentDTO);
                            })
                    .orElseGet(() -> new ResponseHttpDTO<>("404", "El componente no existe", null));
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al obtener la informacion del componente: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<String> deleteComponentById(UUID id) {
        try {
            return moduleComponentGateway.findById(id).map(moduleComponent -> {
                moduleComponentGateway.deleteById(id);
                return new ResponseHttpDTO<String>("200", "Componente eliminado correctamente", "OK");
            }).orElseGet(() -> new ResponseHttpDTO<>("404", "El componente no existe", null));
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al eliminar el componente: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<ModuleComponentDTO> updateComponentById(UUID id, ModuleComponentDTO moduleComponentDTO) {
        try {
            return moduleComponentGateway.findById(id).map(
                    moduleComponent -> {
                        modelMapper.getConfiguration().setSkipNullEnabled(true);
                        modelMapper.map(moduleComponentDTO, moduleComponent);
                        ModuleComponent updatedModuleComponent = moduleComponentGateway.save(moduleComponent);
                        ModuleComponentDTO updatedModuleComponentDTO = modelMapper.map(updatedModuleComponent, ModuleComponentDTO.class);
                        return new ResponseHttpDTO<>("200", "Componente actualizado correctamente", updatedModuleComponentDTO);
                    }
            ).orElseGet(() -> new ResponseHttpDTO<>("404", "El componente no existe", null));
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500",
                    "Error al actualizar la información del componente: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500",
                    "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }
}

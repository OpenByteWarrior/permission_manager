package com.permission_management.application.usecase;


import com.permission_management.application.dto.request.RequestAssignAndRemoveBodyDTO;
import com.permission_management.application.dto.common.GroupPermissionDTO;
import com.permission_management.domain.models.*;
import com.permission_management.application.dto.common.ModuleComponentDTO;
import com.permission_management.application.dto.response.ResponseHttpDTO;
import com.permission_management.infrastructure.persistence.entity.GroupPermission;
import com.permission_management.infrastructure.persistence.entity.ModuleComponent;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuleComponentUseCase {
    private final ModuleComponentGateway moduleComponentGateway;
    private final GroupPermissionGateway groupPermissionGateway;
    private final ModelMapper modelMapper;

    public ResponseHttpDTO<ModuleComponentDTO> createComponent(ModuleComponent moduleComponent) {
        try {

            ModuleComponent savedComponent = moduleComponentGateway.save(moduleComponent);
            ModuleComponentDTO ComponentDTO = modelMapper.map(savedComponent, ModuleComponentDTO.class);
            return new ResponseHttpDTO<>("200", "Componente creado correctamente", ComponentDTO);

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

    public ResponseHttpDTO<GroupPermissionDTO> assignComponentToGroup(RequestAssignAndRemoveBodyDTO body) {
        try {
            GroupPermission groupPermission = groupPermissionGateway.findById(body.getIdContainer())
                    .orElseThrow(() -> new IllegalArgumentException("El grupo de permisos no existe"));

            Set<ModuleComponent> resources = body.getResourcesIds().stream()
                    .map(id -> moduleComponentGateway.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("El componente con el ID " + id + " no existe")))
                    .collect(Collectors.toSet());

            Set<ModuleComponent> alreadyAssignedResources = resources.stream()
                    .filter(groupPermission.getComponents()::contains)
                    .collect(Collectors.toSet());

            if (!alreadyAssignedResources.isEmpty()) {
                return new ResponseHttpDTO<>("400", "Algunos componentes ya están asignados al grupo de permisos", null);
            }

            groupPermission.getComponents().addAll(resources);
            groupPermissionGateway.save(groupPermission);

            GroupPermissionDTO groupPermissionDTO = modelMapper.map(groupPermission, GroupPermissionDTO.class);

            return new ResponseHttpDTO<>("200", "Componentes asignados correctamente al grupo de permisos ", groupPermissionDTO);
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al acceder a la base de datos: " + e.getMessage(), null);
        } catch (IllegalArgumentException e) {
            return new ResponseHttpDTO<>("400", e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<GroupPermissionDTO> removeComponentToGroup(RequestAssignAndRemoveBodyDTO body) {
        try {
            GroupPermission groupPermission = groupPermissionGateway.findById(body.getIdContainer())
                    .orElseThrow(() -> new IllegalArgumentException("El grupo de permisos no existe"));

            Set<ModuleComponent> resourcesToRemove = body.getResourcesIds().stream()
                    .map(id -> moduleComponentGateway.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("El componente con el ID " + id + " no existe")))
                    .collect(Collectors.toSet());

            Set<ModuleComponent> assignedResources = resourcesToRemove.stream()
                    .filter(groupPermission.getComponents()::contains)
                    .collect(Collectors.toSet());

            if (assignedResources.isEmpty()) {
                return new ResponseHttpDTO<>("400", "Ninguno de los componentes especificados está asignado al grupo de permisos", null);
            }

            groupPermission.getComponents().removeAll(assignedResources);
            groupPermissionGateway.save(groupPermission);

            GroupPermissionDTO groupPermissionDTO = modelMapper.map(groupPermission, GroupPermissionDTO.class);

            return new ResponseHttpDTO<>("200", "Componentes eliminados correctamente del grupo de permisos", groupPermissionDTO);
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al acceder a la base de datos: " + e.getMessage(), null);
        } catch (IllegalArgumentException e) {
            return new ResponseHttpDTO<>("400", e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }
}

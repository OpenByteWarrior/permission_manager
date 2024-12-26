package com.permission_management.application.usecase;


import com.permission_management.application.dto.request.RequestAssignAndRemoveBodyDTO;
import com.permission_management.application.dto.common.GroupPermissionDTO;
import com.permission_management.application.service.CrudService;
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
    private final CrudService crudService;
    private final ModelMapper modelMapper;

    public ResponseHttpDTO<ModuleComponentDTO> createComponent(ModuleComponent moduleComponent) {
       return crudService.create(moduleComponent, moduleComponentGateway, ModuleComponentDTO.class, "componente");
    }

    public ResponseHttpDTO<List<ModuleComponentDTO>> getAllComponents() {
       return crudService.getAllResource(moduleComponentGateway, ModuleComponentDTO.class, "componentes");
    }

    public ResponseHttpDTO<ModuleComponentDTO> getComponentById(UUID id) {
        return crudService.getResourceById(id,moduleComponentGateway, ModuleComponentDTO.class, "componente");
    }

    public ResponseHttpDTO<String> deleteComponentById(UUID id) {
        return crudService.deleteResourceById(id, moduleComponentGateway, "componente");
    }

    public ResponseHttpDTO<ModuleComponentDTO> updateComponentById(UUID id, ModuleComponentDTO moduleComponentDTO) {
        return crudService.updateResourceById(id, moduleComponentDTO, moduleComponentGateway, ModuleComponentDTO.class, "componente");
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

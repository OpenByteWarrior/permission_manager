package com.permission_management.application.service;

import com.permission_management.domain.models.Gateway;
import com.permission_management.domain.models.Resource;
import com.permission_management.domain.models.ResourceContainer;
import com.permission_management.application.dto.ResponseHttpDTO;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ResourceAssignmentService {
    private ModelMapper modelMapper;

    public <T extends ResourceContainer<R>, R extends Resource, D> ResponseHttpDTO<D> assignResource(
            UUID containerId,
            Set<UUID> ids,
            Gateway<T> containerGateway,
            Gateway<R> resourceGateway,
            Class<D> dtoClass,
            String containerType
    ) {
        try {
            T container = containerGateway.findById(containerId)
                    .orElseThrow(() -> new IllegalArgumentException(containerType + " no existe"));

            Set<R> resources = ids.stream()
                    .map(id -> resourceGateway.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("El recurso con el ID " + id + " no existe")))
                    .collect(Collectors.toSet());

            Set<R> alreadyAssignedResources = resources.stream()
                    .filter(container.getResources()::contains)
                    .collect(Collectors.toSet());

            if (!alreadyAssignedResources.isEmpty()) {
                return new ResponseHttpDTO<>("400", "Algunos recursos ya están asignados al " + containerType, null);
            }

            container.getResources().addAll(resources);
            containerGateway.save(container);

            D containerDTO = modelMapper.map(container, dtoClass);

            return new ResponseHttpDTO<>("200", "Recursos asignados correctamente al " + containerType, containerDTO);
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al acceder a la base de datos: " + e.getMessage(), null);
        } catch (IllegalArgumentException e) {
            return new ResponseHttpDTO<>("400", e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public <T extends ResourceContainer<R>, R extends Resource, D> ResponseHttpDTO<D> removeResource(
            UUID containerId,
            Set<UUID> ids,
            Gateway<T> containerGateway,
            Gateway<R> resourceGateway,
            Class<D> dtoClass,
            String containerType
    ) {
        try {
            T container = containerGateway.findById(containerId)
                    .orElseThrow(() -> new IllegalArgumentException(containerType + " no existe"));

            Set<R> resourcesToRemove = ids.stream()
                    .map(id -> resourceGateway.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("El recurso con el ID " + id + " no existe")))
                    .collect(Collectors.toSet());

            Set<R> assignedResources = resourcesToRemove.stream()
                    .filter(container.getResources()::contains)
                    .collect(Collectors.toSet());

            if (assignedResources.isEmpty()) {
                return new ResponseHttpDTO<>("400", "Ninguno de los recursos especificados está asignado al " + containerType, null);
            }

            container.getResources().removeAll(assignedResources);
            containerGateway.save(container);

            D containerDTO = modelMapper.map(container, dtoClass);

            return new ResponseHttpDTO<>("200", "Recursos eliminados correctamente del " + containerType, containerDTO);
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al acceder a la base de datos: " + e.getMessage(), null);
        } catch (IllegalArgumentException e) {
            return new ResponseHttpDTO<>("400", e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }
}

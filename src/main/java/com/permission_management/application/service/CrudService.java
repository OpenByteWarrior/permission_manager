package com.permission_management.application.service;

import com.permission_management.application.dto.response.ResponseHttpDTO;
import com.permission_management.domain.models.Gateway;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CrudService {
    private final ModelMapper modelMapper;

    public <T, K> ResponseHttpDTO<T> create(K entity, Gateway<K> gateway, Class<T> targetClass, String nameResource) {
        try {
            K objectSave = gateway.save(entity);
            T objectDTO = modelMapper.map(objectSave, targetClass);
            return new ResponseHttpDTO<>("200", nameResource + " creado correctamente", objectDTO);
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al guardar el " + nameResource + ":" + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public <T, K> ResponseHttpDTO<List<T>> getAllResource(Gateway<K> gateway, Class<T> targetClass, String nameResource) {
        {
            try {
                List<T> allResource = gateway.findAll().stream()
                        .map(value -> modelMapper.map(value, targetClass)).toList();
                if (allResource.isEmpty()) {
                    return new ResponseHttpDTO<>("204", "No hay " + nameResource + " disponibles", null);
                }
                return new ResponseHttpDTO<>("200", nameResource + " obtenidos correctamente", allResource);

            } catch (DataAccessException e) {
                return new ResponseHttpDTO<>("500", "Error al obtener los " + nameResource + ": " + e.getMessage(), null);
            } catch (Exception e) {
                return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
            }
        }
    }

    public <T, K> ResponseHttpDTO<T> getResourceById(UUID id, Gateway<K> gateway, Class<T> targetClass, String nameResource) {

        try {
            return gateway.findById(id).map(entity -> {
                T objectDTO = modelMapper.map(entity, targetClass);
                return new ResponseHttpDTO<>("200", nameResource + " obtenido correctamente", objectDTO);
            }).orElseGet(() -> new ResponseHttpDTO<>("404", "El " + nameResource + " no existe", null));
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al obtener la informacion del " + nameResource + ": " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }

    }

    public <K> ResponseHttpDTO<String> deleteResourceById(UUID id, Gateway<K> gateway, String nameResource) {
        try {
            return gateway.findById(id).map(entity -> {
                gateway.deleteById(id);
                return new ResponseHttpDTO<>("200", nameResource + " eliminado correctamente", HttpStatus.OK.getReasonPhrase());
            }).orElseGet(() -> new ResponseHttpDTO<>("404", nameResource + " no existe", HttpStatus.NOT_FOUND.getReasonPhrase()));
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al eliminar el " + nameResource + ": " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
    }

    public <T, K> ResponseHttpDTO<T> updateResourceById(UUID id, T objectDTO, Gateway<K> gateway, Class<T> targetClass, String nameResource) {
        try {
            return gateway.findById(id).map(existingResource -> {
                modelMapper.map(objectDTO, existingResource);
                K updatedResource = gateway.save(existingResource);
                T updatedPermissionDTO = modelMapper.map(updatedResource, targetClass);

                return new ResponseHttpDTO<>("200", nameResource + " actualizado correctamente", updatedPermissionDTO);
            }).orElseGet(() -> new ResponseHttpDTO<>("404", "El " + nameResource + " no existe", null));
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al actualizar la información del " + nameResource + ": " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }
}

package com.permissions.domain.usecases;

import com.permissions.domain.models.GroupPermissionGateway;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.permissions.domain.models.dto.ResponseHttpDTO;
import com.permissions.domain.models.dto.GroupPermissionBodyDTO;
import com.permissions.domain.models.dto.GroupPermissionDTO;
import com.permissions.infrastructure.driven_adapter.bd.entity.GroupPermission;
import com.permissions.infrastructure.driven_adapter.bd.entity.Permission;
import com.permissions.infrastructure.driven_adapter.bd.repository.PermissionRepository;

@Service
@RequiredArgsConstructor
public class GroupPermissionDomainUseCase {
    private final GroupPermissionGateway groupPermissionGateway;
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    public ResponseHttpDTO<GroupPermissionDTO> createGroupPermission(GroupPermissionBodyDTO groupPermissionBodyDTO) {
        try {
            Set<Permission> permissions = groupPermissionBodyDTO.getPermissionIds()
                    .stream()
                    .map(permissionRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get).collect(Collectors.toSet());

            GroupPermission groupPermission = modelMapper.map(groupPermissionBodyDTO, GroupPermission.class);
            groupPermission.setPermissions(permissions);
            groupPermissionGateway.save(groupPermission);

            GroupPermissionDTO saveGroupPermission = modelMapper.map(groupPermission, GroupPermissionDTO.class);

            return new ResponseHttpDTO<>("200", "Grupo de permisos creado correctamente", saveGroupPermission);

        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al guardar el grupo de permisos: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<List<GroupPermissionDTO>> getAllGroupsPermissions() {
        try {
            List<GroupPermissionDTO> allGroupPermissions = groupPermissionGateway.findAll().stream()
                    .map(value -> modelMapper.map(value, GroupPermissionDTO.class)).collect(Collectors.toList());

            if (allGroupPermissions.isEmpty()) {
                return new ResponseHttpDTO<>("204", "No hay grupos de permisos disponibles", null);
            }
            return new ResponseHttpDTO<>("200", "Grupos de permisos obtenidos correctamente", allGroupPermissions);

        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al obtener los grupos de permisos: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<GroupPermissionDTO> getGroupPermissionById(UUID id) {

        try {
            return groupPermissionGateway.findById(id).map(
                            groupPermission -> {
                                GroupPermissionDTO groupPermissionDTO = modelMapper.map(groupPermission, GroupPermissionDTO.class);
                                return new ResponseHttpDTO<>("200", "Grupo de permisos obtenido correctamente", groupPermissionDTO);
                            })
                    .orElseGet(() -> new ResponseHttpDTO<>("404", "El Grupo de permisos no existe", null));
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al obtener la informacion del grupo de permisos: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<GroupPermission> deleteGroupPermissionById(UUID id) {
        try {
            return groupPermissionGateway.findById(id).map(groupPermission -> {
                groupPermissionGateway.deleteById(id);
                return new ResponseHttpDTO<GroupPermission>("200", "Grupo de permisos eliminado correctamente", null);
            }).orElseGet(() -> new ResponseHttpDTO<>("404", "El grupo de permisos no existe", null));
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al eliminar el grupo de permisos: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<GroupPermissionDTO> updateGroupPermission(UUID id, GroupPermissionDTO groupPermissionDTO) {
        try {

            return groupPermissionGateway.findById(id).map(
                    groupPermission -> {
                        modelMapper.getConfiguration().setSkipNullEnabled(true);
                        modelMapper.map(groupPermissionDTO, groupPermission);
                        GroupPermission updatedGroupPermission = groupPermissionGateway.save(groupPermission);
                        GroupPermissionDTO updatedGroupPermissionDTO = modelMapper.map(updatedGroupPermission, GroupPermissionDTO.class);
                        return new ResponseHttpDTO<>("200", "Grupo de permiso actualizado correctamente", updatedGroupPermissionDTO);
                    }
            ).orElseGet(() -> new ResponseHttpDTO<>("404", "El permiso no existe", null));
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500",
                    "Error al actualizar la información del grupo de permiso: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500",
                    "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }
}

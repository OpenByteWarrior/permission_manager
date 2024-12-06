package com.permission_management.application.usecase;

import com.permission_management.application.dto.*;
import com.permission_management.application.service.ResourceAssignmentService;
import com.permission_management.domain.models.GroupPermissionGateway;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.permission_management.domain.models.ModuleComponentGateway;
import com.permission_management.domain.models.PermissionGateway;
import com.permission_management.domain.models.RoleGateway;
import com.permission_management.infrastructure.persistence.entity.ModuleComponent;
import com.permission_management.infrastructure.persistence.entity.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.permission_management.infrastructure.persistence.entity.GroupPermission;
import com.permission_management.infrastructure.persistence.entity.Permission;

@Service
@RequiredArgsConstructor
public class GroupPermissionDomainUseCase {
    private final GroupPermissionGateway groupPermissionGateway;
    private final PermissionGateway permissionGateway;
    private final ModuleComponentGateway moduleComponentGateway;
    private final RoleGateway roleGateway;
    private final ResourceAssignmentService resourceAssignmentService;
    private final ModelMapper modelMapper;

    public ResponseHttpDTO<GroupPermissionDTO> createGroupPermission(GroupPermissionBodyDTO groupPermissionBodyDTO) {
        try {
            Set<Permission> permissions = groupPermissionBodyDTO.getPermissionIds()
                    .stream()
                    .map(permissionGateway::findById)
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
                    .orElseGet(() -> new ResponseHttpDTO<>("404", "El grupo de permisos no existe", null));
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al obtener la informacion del grupo de permisos: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<String> deleteGroupPermissionById(UUID id) {
        try {
            return groupPermissionGateway.findById(id).map(groupPermission -> {
                groupPermissionGateway.deleteById(id);
                return new ResponseHttpDTO<>("200", "Grupo de permisos eliminado correctamente", "OK");
            }).orElseGet(() -> new ResponseHttpDTO<>("404", "El grupo de permisos no existe", "NOT FOUND"));
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al eliminar el grupo de permisos: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<GroupPermissionDTO> updateGroupPermissionById(UUID id, GroupPermissionDTO groupPermissionDTO) {
        try {

            return groupPermissionGateway.findById(id).map(
                    groupPermission -> {
                        modelMapper.getConfiguration().setSkipNullEnabled(true);
                        modelMapper.map(groupPermissionDTO, groupPermission);
                        GroupPermission updatedGroupPermission = groupPermissionGateway.save(groupPermission);
                        GroupPermissionDTO updatedGroupPermissionDTO = modelMapper.map(updatedGroupPermission, GroupPermissionDTO.class);
                        return new ResponseHttpDTO<>("200", "Grupo de permisos actualizado correctamente", updatedGroupPermissionDTO);
                    }
            ).orElseGet(() -> new ResponseHttpDTO<>("404", "El grupo de permisos no existe", null));
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500",
                    "Error al actualizar la información del grupo de permisos: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500",
                    "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<RoleDTO> assignGroupPermissionToRole(AssignAndRemoveBodyDTO body) {
            try {
                Role role = roleGateway.findById(body.getIdContainer())
                        .orElseThrow(() -> new IllegalArgumentException("El rol no existe"));

                Set<GroupPermission> resources = body.getResourcesIds().stream()
                        .map(id -> groupPermissionGateway.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("El grupo de permisos con el ID " + id + " no existe")))
                        .collect(Collectors.toSet());

                Set<GroupPermission> alreadyAssignedResources = resources.stream()
                        .filter(role.getGroupPermissions()::contains)
                        .collect(Collectors.toSet());

                if (!alreadyAssignedResources.isEmpty()) {
                    return new ResponseHttpDTO<>("400", "Algunos grupos de permisos ya están asignados al rol", null);
                }

                role.getGroupPermissions().addAll(resources);
                roleGateway.save(role);

                RoleDTO roleDTO = modelMapper.map(role, RoleDTO.class);

                return new ResponseHttpDTO<>("200", "Grupos de permisos asignados correctamente al rol", roleDTO);
            } catch (DataAccessException e) {
                return new ResponseHttpDTO<>("500", "Error al acceder a la base de datos: " + e.getMessage(), null);
            } catch (IllegalArgumentException e) {
                return new ResponseHttpDTO<>("400", e.getMessage(), null);
            } catch (Exception e) {
                return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
            }
    }

    public ResponseHttpDTO<RoleDTO> removeGroupPermissionToRole(AssignAndRemoveBodyDTO body) {
            try {
                Role role = roleGateway.findById(body.getIdContainer())
                        .orElseThrow(() -> new IllegalArgumentException("El rol no existe"));

                Set<GroupPermission> resourcesToRemove = body.getResourcesIds().stream()
                        .map(id -> groupPermissionGateway.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("El grupo de permisos con el ID " + id + " no existe")))
                        .collect(Collectors.toSet());

                Set<GroupPermission> assignedResources = resourcesToRemove.stream()
                        .filter(role.getGroupPermissions()::contains)
                        .collect(Collectors.toSet());

                if (assignedResources.isEmpty()) {
                    return new ResponseHttpDTO<>("400", "Ninguno de los grupos de permisos especificados está asignado al rol", null);
                }

                role.getGroupPermissions().removeAll(assignedResources);
                roleGateway.save(role);

                RoleDTO roleDTO = modelMapper.map(role, RoleDTO.class);

                return new ResponseHttpDTO<>("200", "Grupos de permisos eliminados correctamente del rol", roleDTO);
            } catch (DataAccessException e) {
                return new ResponseHttpDTO<>("500", "Error al acceder a la base de datos: " + e.getMessage(), null);
            } catch (IllegalArgumentException e) {
                return new ResponseHttpDTO<>("400", e.getMessage(), null);
            } catch (Exception e) {
                return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
            }
    }
}

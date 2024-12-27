package com.permission_management.application.usecase;

import com.permission_management.application.dto.common.GroupPermissionDTO;
import com.permission_management.application.dto.common.RoleDTO;
import com.permission_management.application.dto.request.RequestAssignAndRemoveBodyDTO;
import com.permission_management.application.dto.request.RequestGroupPermissionBodyDTO;
import com.permission_management.application.dto.response.ResponseHttpDTO;
import com.permission_management.application.service.CrudService;
import com.permission_management.domain.models.GroupPermissionGateway;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.permission_management.domain.models.PermissionGateway;
import com.permission_management.domain.models.RoleGateway;
import com.permission_management.infrastructure.persistence.entity.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.permission_management.infrastructure.persistence.entity.GroupPermission;
import com.permission_management.infrastructure.persistence.entity.Permission;

@Service
@RequiredArgsConstructor
public class GroupPermissionUseCase {
    private final GroupPermissionGateway groupPermissionGateway;
    private final PermissionGateway permissionGateway;
    private final RoleGateway roleGateway;
    private final CrudService crudService;
    private final ModelMapper modelMapper;

    public ResponseHttpDTO<GroupPermissionDTO> createGroupPermission(RequestGroupPermissionBodyDTO groupPermissionBodyDTO) {
        Set<Permission> permissions = groupPermissionBodyDTO.getPermissionIds()
                .stream()
                .map(permissionGateway::findById)
                .filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toSet());
        GroupPermission groupPermission = modelMapper.map(groupPermissionBodyDTO, GroupPermission.class);
        groupPermission.setPermissions(permissions);
        return crudService.create(groupPermission, groupPermissionGateway, GroupPermissionDTO.class, "grupo de permisos");
    }

    public ResponseHttpDTO<List<GroupPermissionDTO>> getAllGroupsPermissions() {
        return crudService.getAllResource(groupPermissionGateway, GroupPermissionDTO.class, "grupos de permisos");
    }

    public ResponseHttpDTO<GroupPermissionDTO> getGroupPermissionById(UUID id) {
        return crudService.getResourceById(id, groupPermissionGateway, GroupPermissionDTO.class, "grupo de permisos");
    }

    public ResponseHttpDTO<String> deleteGroupPermissionById(UUID id) {
        return crudService.deleteResourceById(id, groupPermissionGateway, "grupo de permisos");
    }

    public ResponseHttpDTO<GroupPermissionDTO> updateGroupPermissionById(UUID id, GroupPermissionDTO groupPermissionDTO) {
        return crudService.updateResourceById(id, groupPermissionDTO, groupPermissionGateway, GroupPermissionDTO.class, "grupo de permisos");
    }

    public ResponseHttpDTO<RoleDTO> assignGroupPermissionToRole(RequestAssignAndRemoveBodyDTO body) {
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

    public ResponseHttpDTO<RoleDTO> removeGroupPermissionToRole(RequestAssignAndRemoveBodyDTO body) {
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

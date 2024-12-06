package com.permission_management.application.usecase;

import com.permission_management.application.dto.*;
import com.permission_management.domain.models.GroupPermissionGateway;
import com.permission_management.domain.models.RoleGateway;
import com.permission_management.infrastructure.persistence.entity.GroupPermission;
import com.permission_management.infrastructure.persistence.entity.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleDomainUseCase {

    private final RoleGateway roleGateway;
    private final GroupPermissionGateway groupPermissionGateway;
    private final ModelMapper modelMapper;

    public ResponseHttpDTO<RoleDTO> createRole(RoleBodyDTO roleBodyDTO) {
        try {
            Set<GroupPermission> groupPermissions = roleBodyDTO.getGroupPermissionIDs()
                    .stream()
                    .map(groupPermissionGateway::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get).collect(Collectors.toSet());
            Role role = modelMapper.map(roleBodyDTO, Role.class);
            role.setGroupPermissions(groupPermissions);

            roleGateway.save(role);

            RoleDTO saveRole = modelMapper.map(role, RoleDTO.class);

            return new ResponseHttpDTO<>("200", "Rol creado correctamente", saveRole);

        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al guardar el rol: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<List<RoleDTO>> getAllRoles() {
        try {
            List<RoleDTO> AllRoles = roleGateway.findAll().stream()
                    .map(value -> modelMapper.map(value, RoleDTO.class)).collect(Collectors.toList());

            if (AllRoles.isEmpty()) {
                return new ResponseHttpDTO<>("204", "No hay roles disponibles", null);
            }
            return new ResponseHttpDTO<>("200", "Roles obtenidos correctamente", AllRoles);

        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al obtener los roles: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<RoleDTO> getRoleById(UUID id) {

        try {
            return roleGateway.findById(id).map(
                            role -> {
                                RoleDTO roleDTO = modelMapper.map(role, RoleDTO.class);
                                return new ResponseHttpDTO<>("200", "Rol obtenido correctamente", roleDTO);
                            })
                    .orElseGet(() -> new ResponseHttpDTO<>("404", "El rol no existe", null));
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al obtener la informacion del rol: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<String> deleteRoleById(UUID id) {
        try {
            return roleGateway.findById(id).map(role -> {
                roleGateway.deleteById(id);
                return new ResponseHttpDTO<String>("200", "Rol eliminado correctamente", "OK");
            }).orElseGet(() -> new ResponseHttpDTO<>("404", "El rol no existe", null));
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500", "Error al eliminar el rol: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500", "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }

    public ResponseHttpDTO<RoleDTO> updateRoleById(UUID id, RoleDTO roleDTO) {
        try {

            return roleGateway.findById(id).map(
                    role -> {
                        modelMapper.getConfiguration().setSkipNullEnabled(true);
                        modelMapper.map(roleDTO, role);
                        Role updatedRole = roleGateway.save(role);
                        RoleDTO updatedRoleDTO = modelMapper.map(updatedRole, RoleDTO.class);
                        return new ResponseHttpDTO<>("200", "Rol actualizado correctamente", updatedRoleDTO);
                    }
            ).orElseGet(() -> new ResponseHttpDTO<>("404", "El rol no existe", null));
        } catch (DataAccessException e) {
            return new ResponseHttpDTO<>("500",
                    "Error al actualizar la información del rol: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseHttpDTO<>("500",
                    "Ocurrió un error inesperado: " + e.getMessage(), null);
        }
    }
}

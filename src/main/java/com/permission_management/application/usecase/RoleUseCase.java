package com.permission_management.application.usecase;

import com.permission_management.application.dto.common.RoleDTO;
import com.permission_management.application.dto.request.RequestRoleBodyDTO;
import com.permission_management.application.dto.response.ResponseHttpDTO;
import com.permission_management.application.service.CrudService;
import com.permission_management.domain.models.GroupPermissionGateway;
import com.permission_management.domain.models.RoleGateway;
import com.permission_management.infrastructure.persistence.entity.GroupPermission;
import com.permission_management.infrastructure.persistence.entity.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleUseCase {

    private final RoleGateway roleGateway;
    private final GroupPermissionGateway groupPermissionGateway;
    private final ModelMapper modelMapper;
    private final CrudService crudService;

    public ResponseHttpDTO<RoleDTO> createRole(RequestRoleBodyDTO roleBodyDTO) {
            Set<GroupPermission> groupPermissions = roleBodyDTO.getGroupPermissionIDs()
                    .stream()
                    .map(groupPermissionGateway::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get).collect(Collectors.toSet());
            Role role = modelMapper.map(roleBodyDTO, Role.class);
            role.setGroupPermissions(groupPermissions);
            roleGateway.save(role);
            return crudService.create(role, roleGateway, RoleDTO.class, "rol");
    }

    public ResponseHttpDTO<List<RoleDTO>> getAllRoles() {
        return crudService.getAllResource(roleGateway, RoleDTO.class, "roles");
    }

    public ResponseHttpDTO<RoleDTO> getRoleById(UUID id) {
        return crudService.getResourceById(id, roleGateway, RoleDTO.class, "rol");
    }

    public ResponseHttpDTO<String> deleteRoleById(UUID id) {
        return crudService.deleteResourceById(id, roleGateway, "rol");
    }

    public ResponseHttpDTO<RoleDTO> updateRoleById(UUID id, RoleDTO roleDTO) {
        return crudService.updateResourceById(id, roleDTO, roleGateway, RoleDTO.class, "rol");
    }
}

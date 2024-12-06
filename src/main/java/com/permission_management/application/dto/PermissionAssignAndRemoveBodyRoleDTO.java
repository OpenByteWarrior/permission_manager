package com.permission_management.application.dto;

import java.util.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PermissionAssignAndRemoveBodyRoleDTO {
    private UUID idRole;
    private Set<UUID> permissionIds;
}
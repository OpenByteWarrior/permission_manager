package com.permissions.domain.models.dto;

import java.util.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RoleDTO {
    private UUID id;
    private String name;
    private List<GroupPermissionDTO> groupPermissions;
    private List<PermissionDTO> permissions;
}

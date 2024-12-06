package com.permission_management.application.dto;

import java.util.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PermissionAssignAndRemoveBodyGroupDTO {
    private UUID idGroup;
    private Set<UUID> permissionIds;
}

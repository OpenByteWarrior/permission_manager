package com.permissions.domain.models.dto;

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

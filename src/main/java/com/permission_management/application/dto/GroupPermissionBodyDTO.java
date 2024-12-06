package com.permission_management.application.dto;

import java.util.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class GroupPermissionBodyDTO {
    private UUID id;
    private String name;
    private String description;
    private Set<UUID> permissionIds;
}

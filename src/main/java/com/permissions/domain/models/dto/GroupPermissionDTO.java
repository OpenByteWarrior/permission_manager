package com.permissions.domain.models.dto;

import java.util.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class GroupPermissionDTO {
    private UUID id;
    private String name;
    private String description;
    private Set<UUID> permissionIds;
}

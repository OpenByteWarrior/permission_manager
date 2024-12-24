package com.permission_management.application.dto.common;

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
    private Set<PermissionDTO> permissions;
    private Set<ModuleComponentDTO> components;
}

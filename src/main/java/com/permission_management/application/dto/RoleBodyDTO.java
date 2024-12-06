package com.permission_management.domain.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RoleBodyDTO {
    UUID id;
    String name;
    String description;
    Set<UUID> groupPermissionIDs;
    Set<UUID> permissionIDs;
}

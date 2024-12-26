package com.permission_management.application.dto.request;

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

public class RequestRoleBodyDTO {
    private UUID id;
    private String name;
    private String description;
    private Set<UUID> groupPermissionIDs;
}

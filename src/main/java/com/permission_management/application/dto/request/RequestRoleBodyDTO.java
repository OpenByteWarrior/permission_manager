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
    UUID id;
    String name;
    String description;
    Set<UUID> groupPermissionIDs;
}
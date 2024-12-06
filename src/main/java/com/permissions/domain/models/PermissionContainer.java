package com.permissions.domain.models;

import com.permissions.infrastructure.driven_adapter.bd.entity.Permission;

import java.util.Set;

public interface PermissionContainer {
    Set<Permission> getPermissions();
    void setPermissions(Set<Permission> permissions);
}

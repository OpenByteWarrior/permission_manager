
package com.permissions.infrastructure.driven_adapter.bd.entity;

import java.util.UUID;

import com.permissions.domain.models.PermissionContainer;
import jakarta.persistence.*;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Role implements PermissionContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;
    private String name;
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<GroupPermission> groupPermissions;

    @ManyToMany
    @JoinTable(
            name = "permissions_roles",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;
}

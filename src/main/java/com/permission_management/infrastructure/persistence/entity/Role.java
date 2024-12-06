
package com.permission_management.infrastructure.persistence.entity;

import java.util.UUID;

import com.permission_management.domain.models.ResourceContainer;
import jakarta.persistence.*;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Role implements ResourceContainer<Permission> {

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

    @Override
    public Set<Permission> getResources() {
        return permissions;  // Los recursos de un Role son los permisos
    }

    @Override
    public void setResources(Set<Permission> resources) {
        this.permissions = resources;  // Establecemos los permisos en el Role
    }
}

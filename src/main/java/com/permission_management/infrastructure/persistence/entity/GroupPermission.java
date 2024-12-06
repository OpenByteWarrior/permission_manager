package com.permission_management.infrastructure.persistence.entity;

import com.permission_management.domain.models.ResourceContainer;
import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class GroupPermission implements ResourceContainer<Permission>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "module_component_id")
    private ModuleComponent moduleComponent;

    @ManyToMany
    @JoinTable(name = "permission_group_permission",
            joinColumns = @JoinColumn(name = "group_permission_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions;

    @ManyToMany
    @JoinTable(name = "roles_group_permission",
            joinColumns = @JoinColumn(name = "group_permission_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Override
    public Set<Permission> getResources() {
        return permissions;
    }
    @Override
    public void setResources(Set<Permission> resources) {
        this.permissions = resources;
    }
}
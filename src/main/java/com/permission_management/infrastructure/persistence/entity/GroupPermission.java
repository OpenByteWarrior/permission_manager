package com.permission_management.infrastructure.persistence.entity;

import com.permission_management.domain.models.Resource;
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
public class GroupPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;
    private String name;
    private String description;

    @ManyToMany
    @JoinTable(name="component_group_permission",
            joinColumns = @JoinColumn(name = "group_permission_id"),
            inverseJoinColumns = @JoinColumn(name = "component_id"))
    private Set<ModuleComponent> components;

    @ManyToMany
    @JoinTable(name = "permission_group_permission",
            joinColumns = @JoinColumn(name = "group_permission_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions;

    @ManyToMany(mappedBy = "groupPermissions")
    private Set<Role> roles;
}
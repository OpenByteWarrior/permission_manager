package com.permissions.domain.models;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;
import lombok.*;

@Data
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
    @JoinTable(name = "permission_group_permission_relations", joinColumns = @JoinColumn(name = "group_permission_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions;

    @ManyToMany
    @JoinTable(name = "role_group_permission_relations", joinColumns = @JoinColumn(name = "group_permission_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}
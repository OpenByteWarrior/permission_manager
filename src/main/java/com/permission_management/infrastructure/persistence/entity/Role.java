
package com.permission_management.infrastructure.persistence.entity;

import java.util.HashSet;
import java.util.UUID;

import com.permission_management.domain.models.ResourceContainer;
import jakarta.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Role{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;
    private String name;
    private String description;

    @ManyToMany
    @JoinTable(name = "group_permission_roles",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "group_permission_id"))
    private Set<GroupPermission> groupPermissions;
}


package com.permissions.domain.models;

import java.util.UUID;

import jakarta.persistence.*;
import java.util.Set;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;
    private String name;
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<GroupPermission> groupPermissions;
}

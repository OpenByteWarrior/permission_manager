package com.permissions.domain.models;

import jakarta.persistence.*;
import java.util.UUID;
import java.util.Set;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;
    private String name;
    private String description;

    @ManyToMany(mappedBy = "permissions")
    private Set<GroupPermission> groupPermissions;
}

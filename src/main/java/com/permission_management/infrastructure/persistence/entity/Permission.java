package com.permission_management.infrastructure.persistence.entity;

import com.permission_management.domain.models.Resource;
import jakarta.persistence.*;
import java.util.UUID;
import java.util.Set;
import lombok.*;

@Getter
@Setter
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

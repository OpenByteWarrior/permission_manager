package com.permission_management.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssignAndRemoveBodyDTO {
    private UUID idContainer;
    private Set<UUID> resourcesIds;
}
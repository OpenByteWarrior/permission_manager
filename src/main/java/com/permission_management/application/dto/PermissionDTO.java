package com.permission_management.domain.models.dto;

import java.util.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PermissionDTO {
    private UUID id;
    private String name;
    private String description;
}
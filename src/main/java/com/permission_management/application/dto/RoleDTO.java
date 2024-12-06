package com.permission_management.application.dto;

import java.util.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RoleDTO {
    private UUID id;
    private String name;
    private List<GroupPermissionDTO> groupPermissions;
}

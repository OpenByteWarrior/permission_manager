package com.permissions.application.dto;

import java.util.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RolDTO {
    private UUID id;
    private String name;
    private List<GroupPermissionDTO> groups_permissions;
}

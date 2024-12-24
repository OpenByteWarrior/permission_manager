package com.permission_management.application.dto.request;

import java.util.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RequestGroupPermissionBodyDTO {
    private UUID id;
    private String name;
    private String description;
    private Set<UUID> permissionIds;
}

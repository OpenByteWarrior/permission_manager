package com.permission_management.infrastructure.persistence.repository;

import com.permission_management.infrastructure.persistence.entity.GroupPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupPermissionRepository extends JpaRepository<GroupPermission, UUID> {

    @Query("SELECT gp FROM GroupPermission gp LEFT JOIN FETCH gp.permissions")
    List<GroupPermission> findAllWithPermissions();
}

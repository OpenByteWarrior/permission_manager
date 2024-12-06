package com.permission_management.infrastructure.persistence.repository;

import com.permission_management.infrastructure.persistence.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

}

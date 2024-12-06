package com.permissions.infrastructure.driven_adapter.bd.repository;

import com.permissions.infrastructure.driven_adapter.bd.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

}

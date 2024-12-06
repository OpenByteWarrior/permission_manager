package com.permissions.infrastructure.driven_adapter.bd.repository;

import com.permissions.domain.models.RoleGateway;
import com.permissions.infrastructure.driven_adapter.bd.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RoleAdapter implements RoleGateway {

    private final RoleRepository roleRepository;

    @Override
    public Role save(Role role) {
        return this.roleRepository.save(role);
    }

    @Override
    public List<Role> findAll() {
        return this.roleRepository.findAll();
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return this.roleRepository.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
        this.roleRepository.deleteById(id);
    }

}

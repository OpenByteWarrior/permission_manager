package com.permissions.infrastructure.driven_adapter.bd.repository;


import com.permissions.domain.models.ModuleComponentGateway;
import com.permissions.infrastructure.driven_adapter.bd.entity.ModuleComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ModuleComponentAdapter implements ModuleComponentGateway {

    private final ModuleComponentRepository moduleComponentRepository;
    @Override
    public ModuleComponent save(ModuleComponent moduleComponent) {
        return this.moduleComponentRepository.save(moduleComponent);
    }

    @Override
    public List<ModuleComponent> findAll() {
        return this.moduleComponentRepository.findAll();
    }

    @Override
    public Optional<ModuleComponent> findById(UUID id) {
        return this.moduleComponentRepository.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
        this.moduleComponentRepository.deleteById(id);
    }
}

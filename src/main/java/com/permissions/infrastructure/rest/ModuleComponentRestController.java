package com.permissions.infrastructure.rest;

import com.permissions.domain.models.dto.ModuleComponentDTO;
import com.permissions.domain.models.dto.ResponseHttpDTO;
import com.permissions.domain.models.dto.RoleBodyDTO;
import com.permissions.domain.models.dto.RoleDTO;
import com.permissions.domain.usecases.ModuleComponentDomainUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/components")
@RequiredArgsConstructor
public class ModuleComponentRestController {
    private final ModuleComponentDomainUseCase moduleComponentDomainUseCase;

    @PostMapping
    public ResponseEntity<ResponseHttpDTO<ModuleComponentDTO>> createComponent(@RequestBody ModuleComponentDTO moduleComponentDTO) {
        return new ResponseEntity<>(moduleComponentDomainUseCase.createComponent(moduleComponentDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponseHttpDTO<List<ModuleComponentDTO>>> getAllComponents() {
        return new ResponseEntity<>(moduleComponentDomainUseCase.getAllComponents(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseHttpDTO<ModuleComponentDTO>> getComponentById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(moduleComponentDomainUseCase.getComponentById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseHttpDTO<String>> deleteComponentById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(moduleComponentDomainUseCase.deleteComponentById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseHttpDTO<ModuleComponentDTO>> updateComponentById(@PathVariable("id") UUID id, @RequestBody ModuleComponentDTO moduleComponentDTO) {
        return new ResponseEntity<>(moduleComponentDomainUseCase.updateComponentById(id, moduleComponentDTO), HttpStatus.OK);
    }
}

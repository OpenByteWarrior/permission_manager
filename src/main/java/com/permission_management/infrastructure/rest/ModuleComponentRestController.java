package com.permission_management.infrastructure.rest;

import com.permission_management.application.dto.AssignAndRemoveBodyDTO;
import com.permission_management.application.dto.GroupPermissionDTO;
import com.permission_management.application.dto.ModuleComponentDTO;
import com.permission_management.application.dto.ResponseHttpDTO;
import com.permission_management.application.usecase.ModuleComponentDomainUseCase;
import com.permission_management.infrastructure.persistence.entity.ModuleComponent;
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
    public ResponseEntity<ResponseHttpDTO<ModuleComponentDTO>> createComponent(@RequestBody ModuleComponent component) {
        return new ResponseEntity<>(moduleComponentDomainUseCase.createComponent(component), HttpStatus.CREATED);
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
    @PutMapping("/assign")
    public ResponseEntity<ResponseHttpDTO<GroupPermissionDTO>> assignComponentToGroup(@RequestBody AssignAndRemoveBodyDTO body) {
        return new ResponseEntity<>(moduleComponentDomainUseCase.assignComponentToGroup(body), HttpStatus.OK);
    }
    @DeleteMapping("/remove")
    public ResponseEntity<ResponseHttpDTO<GroupPermissionDTO>> removeComponentToGroup( @RequestBody AssignAndRemoveBodyDTO body) {
        return new ResponseEntity<>(moduleComponentDomainUseCase.removeComponentToGroup(body), HttpStatus.OK);
    }
}

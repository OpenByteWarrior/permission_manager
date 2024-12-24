package com.permission_management.infrastructure.api;
import org.springframework.web.bind.annotation.*;
@RestController
public class HomeRestController {
    @GetMapping("/")
    public String home() {
        return "Bienvenido a la API de Gestion de Permisos";
    }
}
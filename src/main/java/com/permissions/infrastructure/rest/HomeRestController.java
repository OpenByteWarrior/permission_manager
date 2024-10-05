package com.permissions.infrastructure.rest;
import org.springframework.web.bind.annotation.*;
@RestController
public class HomeRestController {
    @GetMapping("/")
    public String home() {
        return "Bienvenido a la API de Permisos";
    }
}
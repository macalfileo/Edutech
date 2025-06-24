package com.edutech.community_service.controller;

import com.edutech.community_service.model.Publicacion;
import com.edutech.community_service.service.PublicacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionController {

    @Autowired
    private PublicacionService service;

    @PostMapping
    public ResponseEntity<Publicacion> crear(@Valid @RequestBody Publicacion publicacion) {
        publicacion.setFechaPublicacion(LocalDate.now());
        return ResponseEntity.ok(service.crear(publicacion));
    }

    @GetMapping
    public List<Publicacion> listar() {
        return service.listarTodas();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.eliminar(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
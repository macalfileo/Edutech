package com.edutech.community_service.controller;

import com.edutech.community_service.model.Publicacion;
import com.edutech.community_service.service.PublicacionService;

import jakarta.validation.Valid; 

import com.edutech.community_service.repository.PublicacionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Provider.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/publicaciones")

public class PublicacionController {

    @Autowired
    private PublicacionService service;

    @PostMapping
    public ResponseEntity<Publicacion> crear(@Valid @RequestBody Publicacion publicacion) {
        publicacion.setFechaPublicacion( LocalDate.now()); 
        return ResponseEntity.ok(service.crear(publicacion));
    }

    @GetMapping
    public List<Publicacion> listar() {
        return service.listarTodas();
    }

    // Eliminar publicación por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = service.eliminar(id);
        if (eliminado) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

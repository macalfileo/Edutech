package com.edutech.community_service.controller;

import com.edutech.community_service.model.Publicacion;
import com.edutech.community_service.service.PublicacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

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

    @Operation(summary = "Crear una nueva publicación", description = "Permite crear una nueva publicación en el sistema.")
    @ApiResponse(responseCode = "200", description = "Publicación creada exitosamente", content = @Content(schema = @Schema(implementation = Publicacion.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados", content = @Content(schema = @Schema(implementation = String.class)))
    @PostMapping
    public ResponseEntity<Publicacion> crear(@Valid @RequestBody Publicacion publicacion) {
        publicacion.setFechaPublicacion(LocalDate.now());
        return ResponseEntity.ok(service.crear(publicacion));
    }

    @Operation(summary = "Obtener todas las publicaciones", description = "Devuelve una lista de todas las publicaciones registradas.")
    @ApiResponse(responseCode = "200", description = "Lista de publicaciones obtenida exitosamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Publicacion.class))))
    @GetMapping
    public List<Publicacion> listar() {
        return service.listarTodas();
    }

    @Operation(summary = "Eliminar publicación por ID", description = "Permite eliminar una publicación existente usando su ID.")
    @ApiResponse(responseCode = "200", description = "Publicación eliminada exitosamente", content = @Content)
    @ApiResponse(responseCode = "404", description = "Publicación no encontrada", content = @Content(schema = @Schema(implementation = String.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.eliminar(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
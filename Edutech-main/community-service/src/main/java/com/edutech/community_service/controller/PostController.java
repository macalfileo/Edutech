package com.edutech.community_service.controller;

import com.edutech.community_service.model.Post;
import com.edutech.community_service.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/community")
public class PostController {

    @Autowired
    private PostService postService;

    @Operation(summary = "Crear publicación", description = "Permite a un usuario crear una publicación en el foro de un curso.")
    @ApiResponse(responseCode = "201", description = "Post creado exitosamente", content = @Content(schema = @Schema(implementation = Post.class)))
    @ApiResponse(responseCode = "400", description = "Error de validación", content = @Content(schema = @Schema(implementation = String.class)))
    @PreAuthorize("hasAnyRole('CLIENTE', 'INSTRUCTOR')")
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Post post, @RequestHeader("Authorization") String token) {
        try {
            Post nuevo = postService.crear(post, token);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Listar publicaciones", description = "Devuelve todas las publicaciones.")
    @ApiResponse(responseCode = "200", description = "Listado de posts", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Post.class))))
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE_CURSOS')")
    @GetMapping
    public ResponseEntity<List<Post>> obtenerTodos() {
        return ResponseEntity.ok(postService.obtenerTodos());
    }

    @Operation(summary = "Buscar por curso", description = "Obtiene publicaciones asociadas a un curso.")
    @ApiResponse(responseCode = "200", description = "Posts del curso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Post.class))))
    @PreAuthorize("hasAnyRole('CLIENTE', 'INSTRUCTOR', 'GERENTE_CURSOS')")
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Post>> porCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(postService.obtenerPorCurso(cursoId));
    }

    @Operation(summary = "Buscar por usuario", description = "Obtiene publicaciones hechas por un usuario.")
    @ApiResponse(responseCode = "200", description = "Posts del usuario", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Post.class))))
    @PreAuthorize("hasAnyRole('CLIENTE', 'INSTRUCTOR', 'ADMINISTRADOR')")
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<Post>> porUsuario(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.obtenerPorUsuario(userId));
    }

    @Operation(summary = "Eliminar publicación", description = "Elimina una publicación por ID.")
    @ApiResponse(responseCode = "200", description = "Post eliminado")
    @ApiResponse(responseCode = "404", description = "Post no encontrado")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(postService.eliminar(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
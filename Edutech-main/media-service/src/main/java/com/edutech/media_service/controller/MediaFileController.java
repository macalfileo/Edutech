package com.edutech.media_service.controller;

import com.edutech.media_service.model.MediaFile;
import com.edutech.media_service.service.MediaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/media")
public class MediaFileController {

    @Autowired
    private MediaService mediaFileService;

    @Operation(summary = "Subir archivo", description = "Sube un nuevo archivo multimedia validando su origen.")
    @ApiResponse(responseCode = "201", description = "Archivo creado exitosamente", content = @Content(schema = @Schema(implementation = MediaFile.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos o entidad relacionada no existe", content = @Content(schema = @Schema(implementation = String.class)))
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE_CURSOS', 'CLIENTE')")
    @PostMapping
    public ResponseEntity<?> subirArchivo(@RequestBody MediaFile archivo, @RequestHeader("Authorization") String token) {
        try {
            MediaFile nuevo = mediaFileService.guardarArchivo(archivo, token);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Obtener archivo por ID", description = "Retorna un archivo multimedia por su ID.")
    @ApiResponse(responseCode = "200", description = "Archivo encontrado", content = @Content(schema = @Schema(implementation = MediaFile.class)))
    @ApiResponse(responseCode = "404", description = "Archivo no encontrado")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE_CURSOS', 'CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<MediaFile> archivo = mediaFileService.obtenerPorId(id);
    
        if (archivo.isPresent()) {
            return ResponseEntity.ok(archivo.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Archivo no encontrado");
        }
    }

    @Operation(summary = "Listar archivos", description = "Lista todos los archivos multimedia.")
    @ApiResponse(responseCode = "200", description = "Archivos listados correctamente", content = @Content(schema = @Schema(implementation = MediaFile.class)))
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<MediaFile>> listarArchivos() {
        return ResponseEntity.ok(mediaFileService.listarTodos());
    }

    @Operation(summary = "Eliminar archivo", description = "Elimina un archivo multimedia por ID.")
    @ApiResponse(responseCode = "204", description = "Archivo eliminado")
    @ApiResponse(responseCode = "404", description = "Archivo no encontrado")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE_CURSOS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            mediaFileService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Archivos por curso
    @Operation(summary = "Obtener archivos por curso", description = "Lista los archivos asociados a un curso.")
    @ApiResponse(responseCode = "200", description = "Archivos encontrados", content = @Content(schema = @Schema(implementation = MediaFile.class)))
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE_CURSOS')")
    @GetMapping("/curso/{courseId}")
    public ResponseEntity<List<MediaFile>> obtenerPorCurso(@PathVariable Long courseId) {
        return ResponseEntity.ok(mediaFileService.obtenerPorCurso(courseId));
    }

    // Archivos por evaluación
    @Operation(summary = "Obtener archivos por evaluación", description = "Lista los archivos asociados a una evaluación.")
    @ApiResponse(responseCode = "200", description = "Archivos encontrados", content = @Content(schema = @Schema(implementation = MediaFile.class)))
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE_CURSOS')")
    @GetMapping("/evaluacion/{evaluationId}")
    public ResponseEntity<List<MediaFile>> obtenerPorEvaluacion(@PathVariable Long evaluationId) {
        return ResponseEntity.ok(mediaFileService.obtenerPorEvaluacion(evaluationId));
    }

    // Archivos por usuario
    @Operation(summary = "Obtener archivos por usuario", description = "Lista los archivos subidos por un usuario.")
    @ApiResponse(responseCode = "200", description = "Archivos encontrados", content = @Content(schema = @Schema(implementation = MediaFile.class)))
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CLIENTE')")
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<MediaFile>> obtenerPorUsuario(@PathVariable Long userId) {
        return ResponseEntity.ok(mediaFileService.obtenerPorUsuario(userId));
    }

}
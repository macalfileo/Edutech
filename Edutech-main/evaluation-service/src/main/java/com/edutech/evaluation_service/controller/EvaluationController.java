package com.edutech.evaluation_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutech.evaluation_service.model.Evaluation;
import com.edutech.evaluation_service.service.EvaluationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/v1/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    //Crear evaluación
    @Operation(summary = "Crear evaluación", description = "Crea una nueva evaluación.")
    @ApiResponse(responseCode = "201", description = "Evaluación creada", content = @Content(schema = @Schema(implementation = Evaluation.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('GERENTE_CURSOS')")
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Evaluation evaluacion, @RequestHeader("Authorization") String token) {
        try {
            Evaluation nueva = evaluationService.crearEvaluacion(evaluacion, token);
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Obtener por ID
    @Operation(summary = "Obtener evaluación por ID", description = "Obtiene una evaluación específica usando su ID.")
    @ApiResponse(responseCode = "200", description = "Evaluación encontrada", content = @Content(schema = @Schema(implementation = Evaluation.class)))
    @ApiResponse(responseCode = "404", description = "Evaluación no encontrada", content = @Content(schema = @Schema(implementation = String.class)))
    @GetMapping("/{id}")
    public ResponseEntity<Evaluation> obtenerPorId(@PathVariable Long id) {
        return evaluationService.obtenerEvaluacionPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

     //Listar todas
    @Operation(summary = "Listar todas las evaluaciones", description = "Devuelve una lista de todas las evaluaciones.")
    @ApiResponse(responseCode = "200", description = "Lista de evaluaciones obtenida exitosamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Evaluation.class))))
    @GetMapping
    public ResponseEntity<List<Evaluation>> listar() {
        List<Evaluation> evaluaciones = evaluationService.listarEvaluaciones();
        return ResponseEntity.ok(evaluaciones);
    }

    //Actualizar evaluación
    @Operation(summary = "Actualizar evaluación", description = "Actualiza los detalles de una evaluación específica.")
    @ApiResponse(responseCode = "200", description = "Evaluación actualizada", content = @Content(schema = @Schema(implementation = Evaluation.class)))
    @ApiResponse(responseCode = "404", description = "Evaluación no encontrada", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('GERENTE_CURSOS')")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Evaluation evaluacion, @RequestHeader("Authorization") String token) {
        try {
            return evaluationService.actualizarEvaluacion(id, evaluacion, token)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

        //Eliminar evaluación
    @Operation(summary = "Eliminar evaluación", description = "Elimina una evaluación existente por ID.")
    @ApiResponse(responseCode = "204", description = "Evaluación eliminada")
    @ApiResponse(responseCode = "404", description = "Evaluación no encontrada", content = @Content)
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('GERENTE_CURSOS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (evaluationService.eliminarEvaluacion(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evaluación no encontrada");
    }

    @Operation(summary = "Obtener evaluaciones por curso", description = "Devuelve todas las evaluaciones asociadas a un curso.")
    @ApiResponse(responseCode = "200", description = "Evaluaciones encontradas", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Evaluation.class))))
    @GetMapping("/curso/{courseId}")
    public ResponseEntity<List<Evaluation>> obtenerPorCurso(@PathVariable Long courseId) {
        return ResponseEntity.ok(evaluationService.listarEvaluacionesPorCurso(courseId));
    }

    @Operation(summary = "Eliminar evaluaciones por curso", description = "Elimina todas las evaluaciones asociadas a un curso.")
    @ApiResponse(responseCode = "200", description = "Evaluaciones eliminadas", content = @Content(schema = @Schema(implementation = String.class)))
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/curso/{courseId}")
    public ResponseEntity<String> eliminarPorCurso(@PathVariable Long courseId) {
        evaluationService.eliminarEvaluacionesPorCurso(courseId);
        return ResponseEntity.ok("Evaluaciones eliminadas para el curso " + courseId);
    }

}

package com.edutech.evaluation_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping("/api/evaluaciones")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    //Crear evaluación
    @Operation(summary = "Crear evaluación", description = "Crea una nueva evaluación para un curso o estudiante.")
    @ApiResponse(responseCode = "200", description = "Evaluación creada exitosamente", content = @Content(schema = @Schema(implementation = Evaluation.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados", content = @Content(schema = @Schema(implementation = String.class)))
    @PostMapping
    public ResponseEntity<Evaluation> crear(@RequestBody Evaluation evaluacion) {
        Evaluation nueva = evaluationService.crearEvaluacion(evaluacion);
        return ResponseEntity.ok(nueva);
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
    public List<Evaluation> listar() {
        return evaluationService.listarEvaluaciones();
    }

    //Actualizar evaluación
    @Operation(summary = "Actualizar evaluación", description = "Actualiza los detalles de una evaluación específica.")
    @ApiResponse(responseCode = "200", description = "Evaluación actualizada exitosamente", content = @Content(schema = @Schema(implementation = Evaluation.class)))
    @ApiResponse(responseCode = "404", description = "Evaluación no encontrada", content = @Content(schema = @Schema(implementation = String.class)))
    @PutMapping("/{id}")
    public ResponseEntity<Evaluation> actualizar(@PathVariable Long id, @RequestBody Evaluation evaluacion) {
        return evaluationService.actualizarEvaluacion(id, evaluacion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

        //Eliminar evaluación
        @Operation(summary = "Eliminar evaluación", description = "Elimina una evaluación específica usando su ID.")
    @ApiResponse(responseCode = "200", description = "Evaluación eliminada exitosamente", content = @Content)
    @ApiResponse(responseCode = "404", description = "Evaluación no encontrada", content = @Content(schema = @Schema(implementation = String.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (evaluationService.eliminarEvaluacion(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }



}

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

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/evaluaciones")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    //Crear evaluación
    @PostMapping
    public ResponseEntity<Evaluation> crear(@RequestBody Evaluation evaluacion) {
        Evaluation nueva = evaluationService.crearEvaluacion(evaluacion);
        return ResponseEntity.ok(nueva);
    }

    //Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<Evaluation> obtenerPorId(@PathVariable Long id) {
        return evaluationService.obtenerEvaluacionPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

     //Listar todas
    @GetMapping
    public List<Evaluation> listar() {
        return evaluationService.listarEvaluaciones();
    }

    //Actualizar evaluación
    @PutMapping("/{id}")
    public ResponseEntity<Evaluation> actualizar(@PathVariable Long id, @RequestBody Evaluation evaluacion) {
        return evaluationService.actualizarEvaluacion(id, evaluacion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

        //Eliminar evaluación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (evaluationService.eliminarEvaluacion(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }



}

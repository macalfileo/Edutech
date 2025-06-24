package com.edutech.evaluation_service.service;

import com.edutech.evaluation_service.model.Evaluation;
import com.edutech.evaluation_service.repository.EvaluationRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class EvaluationService {

       private final EvaluationRepository evaluationRepository;

    public EvaluationService(EvaluationRepository evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }

     //Crear nueva evaluación
    public Evaluation crearEvaluacion(Evaluation evaluacion) {
        return evaluationRepository.save(evaluacion);
    }

    //Obtener evaluación por ID
    public Optional<Evaluation> obtenerEvaluacionPorId(Long id) {
        return evaluationRepository.findById(id);
    }

    //Listar todas las evaluaciones
    public List<Evaluation> listarEvaluaciones() {
        return evaluationRepository.findAll();
    }

    //Actualizar evaluación existente
    public Optional<Evaluation> actualizarEvaluacion(Long id, Evaluation nuevaEvaluacion) {
        return evaluationRepository.findById(id).map(evaluacionExistente -> {
            evaluacionExistente.setTitle(nuevaEvaluacion.getTitle());
            evaluacionExistente.setDescription(nuevaEvaluacion.getDescription());
            return evaluationRepository.save(evaluacionExistente);
        });
    }

      //Eliminar evaluación
    public boolean eliminarEvaluacion(Long id) {
        if (evaluationRepository.existsById(id)) {
            evaluationRepository.deleteById(id);
            return true;
        }
        return false;
    }

 
}

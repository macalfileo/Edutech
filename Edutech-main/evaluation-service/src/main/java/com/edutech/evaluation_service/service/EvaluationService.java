package com.edutech.evaluation_service.service;

import com.edutech.evaluation_service.model.Evaluation;
import com.edutech.evaluation_service.repository.EvaluationRepository;
import com.edutech.evaluation_service.webclient.AuthClient;
import com.edutech.evaluation_service.webclient.CourseClient;
import com.edutech.evaluation_service.webclient.EnrollmentClient;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EvaluationService {
    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private CourseClient courseClient;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private EnrollmentClient enrollmentClient;

    // Crear nueva evaluación
    public Evaluation crearEvaluacion(Evaluation evaluacion, String authHeader) {
        if (!authClient.usuarioPuedeCrearEvaluacion(authHeader)) {
            throw new RuntimeException("No tienes permiso para crear evaluaciones.");
        }

        if (!authClient.usuarioExiste(evaluacion.getUserId(), authHeader)) {
            throw new RuntimeException("Usuario no válido o no encontrado.");
        }

        if (!courseClient.cursoExiste(evaluacion.getCourseId(), authHeader)) {
            throw new RuntimeException("El curso no existe o no se puede acceder.");
        }

        if (!enrollmentClient.tieneInscripciones(evaluacion.getCourseId())) {
            throw new RuntimeException("No hay estudiantes inscritos en este curso.");
        }
        return evaluationRepository.save(evaluacion);
    }

    // Obtener evaluación por ID
    public Optional<Evaluation> obtenerEvaluacionPorId(Long id) {
        return evaluationRepository.findById(id);
    }

    // Listar todas las evaluaciones
    public List<Evaluation> listarEvaluaciones() {
        return evaluationRepository.findAll();
    }

    // Listar evaluaciones por curso
    public List<Evaluation> listarEvaluacionesPorCurso(Long courseId) {
        return evaluationRepository.findByCourseId(courseId);
    }

    // Eliminar evaluaciones por curso
    public void eliminarEvaluacionesPorCurso(Long courseId) {
        List<Evaluation> evaluaciones = evaluationRepository.findByCourseId(courseId);
        evaluationRepository.deleteAll(evaluaciones);
    }

    // Actualizar evaluación existente
    public Optional<Evaluation> actualizarEvaluacion(Long id, Evaluation nuevaEvaluacion, String authHeader) {
        return evaluationRepository.findById(id).map(evaluacionExistente -> {
            if (!courseClient.cursoExiste(nuevaEvaluacion.getCourseId(), authHeader)) {
                throw new RuntimeException("El curso no existe o no se puede acceder.");
            }
            evaluacionExistente.setTitulo(nuevaEvaluacion.getTitulo());
            evaluacionExistente.setDescripcion(nuevaEvaluacion.getDescripcion());
            evaluacionExistente.setCourseId(nuevaEvaluacion.getCourseId());
            return evaluationRepository.save(evaluacionExistente);
        });
    }

    // Eliminar evaluación
    public boolean eliminarEvaluacion(Long id) {
        if (evaluationRepository.existsById(id)) {
            evaluationRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

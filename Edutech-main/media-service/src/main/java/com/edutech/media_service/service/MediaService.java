package com.edutech.media_service.service;

import com.edutech.media_service.model.MediaFile;
import com.edutech.media_service.repository.MediaFileRepository;
import com.edutech.media_service.webclient.CourseClient;
import com.edutech.media_service.webclient.EvaluationClient;
import com.edutech.media_service.webclient.UserProfileClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MediaService {
    
    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Autowired
    private CourseClient courseClient;

    @Autowired
    private EvaluationClient evaluationClient;

    @Autowired
    private UserProfileClient userProfileClient;

    // Crear y validar archivo
    public MediaFile guardarArchivo(MediaFile archivo, String authHeader) {
        if (archivo.getOrigen().equalsIgnoreCase("CURSO")) {
            if (archivo.getCourseId() == null || !courseClient.cursoExiste(archivo.getCourseId(), authHeader)) {
                throw new RuntimeException("Curso no válido o no existe.");
            }
        } else if (archivo.getOrigen().equalsIgnoreCase("EVALUACION")) {
            if (archivo.getEvaluationId() == null || !evaluationClient.evaluacionExiste(archivo.getEvaluationId(), authHeader)) {
                throw new RuntimeException("Evaluación no válida o no existe.");
            }
        } else if (archivo.getOrigen().equalsIgnoreCase("USUARIO")) {
            if (archivo.getUserId() == null || !userProfileClient.usuarioExiste(archivo.getUserId(), authHeader)) {
                throw new RuntimeException("Usuario no válido o no existe.");
            }
        } else {
            throw new RuntimeException("Origen inválido. Debe ser CURSO, EVALUACION o USUARIO.");
        }

        return mediaFileRepository.save(archivo);
    }

    public Optional<MediaFile> obtenerPorId(Long id) {
        return mediaFileRepository.findById(id);
    }

    public List<MediaFile> listarTodos() {
        return mediaFileRepository.findAll();
    }

    public void eliminarPorId(Long id) {
        if (!mediaFileRepository.existsById(id)) {
            throw new RuntimeException("Archivo no encontrado");
        }
        mediaFileRepository.deleteById(id);
    }

    public List<MediaFile> obtenerPorCurso(Long courseId) {
        return mediaFileRepository.findByCourseId(courseId);
    }

    public List<MediaFile> obtenerPorEvaluacion(Long evaluationId) {
        return mediaFileRepository.findByEvaluationId(evaluationId);
    }

    public List<MediaFile> obtenerPorUsuario(Long userId) {
        return mediaFileRepository.findByUserId(userId);
    }
}
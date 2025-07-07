package com.edutech.enrollment_service.service;

import com.edutech.enrollment_service.model.Enrollment;
import com.edutech.enrollment_service.repository.EnrollmentRepository;
import com.edutech.enrollment_service.webclient.AuthClient;
import com.edutech.enrollment_service.webclient.CourseClient;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private CourseClient courseClient;

    // Obtener todas las inscripciones
    public List<Enrollment> obtenerEnrollments() {
        return enrollmentRepository.findAll();
    }

    // Obtener inscripción por ID
    public Enrollment obtenerEnrollmentPorId(Long id) {
        return enrollmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Inscripción no encontrada Id: " + id));
    }

    // Obtener inscripciones por usuario
    public List<Enrollment> obtenerPorUsuario(Long userId) {
        return enrollmentRepository.findByUserId(userId);
    }

    // Obtener inscripciones por curso
    public List<Enrollment> obtenerPorCurso(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    // Crear nueva inscripción
    public Enrollment crearEnrollment(Enrollment enrollment, String authHeader) {
        if (enrollment.getUserId() == null || enrollment.getCourseId() == null) {
            throw new RuntimeException("El ID del usuario y del curso son obligatorios");
        }

        if (enrollmentRepository.existsByUserIdAndCourseId(enrollment.getUserId(), enrollment.getCourseId())) {
            throw new RuntimeException("El usuario ya está inscrito en este curso");
        }

        if (!authClient.usuarioExiste(enrollment.getUserId(), authHeader)) {
        throw new RuntimeException("El usuario no existe o no tiene permisos.");
    }

    if (!courseClient.cursoExiste(enrollment.getCourseId(), authHeader)) {
        throw new RuntimeException("El curso no existe o no se puede acceder.");
    }

        enrollment.setFechaInscripcion(LocalDateTime.now());
        enrollment.setEstado("ACTIVA");
        enrollment.setProgreso(0);
        enrollment.setCertificadoEmitido(false);

        return enrollmentRepository.save(enrollment);
    }

    // Actualizar progreso y nota
    public Enrollment actualizarEnrollment(Long id, Integer progreso, Double notaFinal, String estado, Boolean certificadoEmitido) {
        Enrollment inscripcion = enrollmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Inscripción no encontrada Id: " + id));

        if (progreso != null && progreso >= 0 && progreso <= 100) {
            inscripcion.setProgreso(progreso);
        }

        if (notaFinal != null && notaFinal >= 0 && notaFinal <= 7) {
            inscripcion.setNotaFinal(notaFinal);
        }

        if (estado != null && !estado.trim().isEmpty()) {
            inscripcion.setEstado(estado);
        }

        if (certificadoEmitido != null) {
            inscripcion.setCertificadoEmitido(certificadoEmitido);
        }

        return enrollmentRepository.save(inscripcion);
    }

    // Eliminar inscripción
    public String eliminarEnrollment(Long id) {
        Enrollment inscripcion = enrollmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Inscripción no encontrada Id: " + id));
        enrollmentRepository.delete(inscripcion);
        return "Inscripción eliminada";
    }
}

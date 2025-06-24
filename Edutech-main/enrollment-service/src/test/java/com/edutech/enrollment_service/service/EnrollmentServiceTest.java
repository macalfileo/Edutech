package com.edutech.enrollment_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.edutech.enrollment_service.model.Enrollment;
import com.edutech.enrollment_service.repository.EnrollmentRepository;
import com.edutech.enrollment_service.webclient.AuthClient;
import com.edutech.enrollment_service.webclient.CourseClient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private AuthClient authClient;

    @Mock
    private CourseClient courseClient;

    @InjectMocks
    private EnrollmentService enrollmentService;

    @Test
    void crearEnrollment_valido_devuelveInscripcion() {
        Enrollment inscripcion = new Enrollment();
        inscripcion.setUserId(1L);
        inscripcion.setCourseId(2L);
        inscripcion.setEstado("activo");
        inscripcion.setFechaInscripcion(java.time.LocalDateTime.now());

        when(authClient.existeUsuario(1L)).thenReturn(true);
        when(courseClient.existeCurso(2L)).thenReturn(true);
        when(enrollmentRepository.existsByUserIdAndCourseId(1L, 2L)).thenReturn(false);
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(inscripcion);

        Enrollment resultado = enrollmentService.crearEnrollment(inscripcion);

        assertEquals(1L, resultado.getUserId());
        assertEquals(2L, resultado.getCourseId());
        assertEquals("activo", resultado.getEstado());
    }

    @Test
    void crearEnrollment_usuarioNoExiste_lanzaExcepcion() {
        Enrollment inscripcion = new Enrollment();
        inscripcion.setUserId(1L);
        inscripcion.setCourseId(2L);

        when(authClient.existeUsuario(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            enrollmentService.crearEnrollment(inscripcion);
        });

        assertEquals("Usuario no encontrado ID: 1", ex.getMessage());
    }

    @Test
    void crearEnrollment_cursoNoExiste_lanzaExcepcion() {
        Enrollment inscripcion = new Enrollment();
        inscripcion.setUserId(1L);
        inscripcion.setCourseId(2L);

        when(authClient.existeUsuario(1L)).thenReturn(true);
        when(courseClient.existeCurso(2L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            enrollmentService.crearEnrollment(inscripcion);
        });

        assertEquals("Curso no encontrado ID: 2", ex.getMessage());
    }

    @Test
    void crearEnrollment_duplicado_lanzaExcepcion() {
        Enrollment inscripcion = new Enrollment();
        inscripcion.setUserId(1L);
        inscripcion.setCourseId(2L);

        when(authClient.existeUsuario(1L)).thenReturn(true);
        when(courseClient.existeCurso(2L)).thenReturn(true);
        when(enrollmentRepository.existsByUserIdAndCourseId(1L, 2L)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            enrollmentService.crearEnrollment(inscripcion);
        });

        assertEquals("El usuario ya está inscrito en este curso", ex.getMessage());
    }

    @Test
    void eliminarEnrollment_existente_noLanzaExcepcion() {
        Enrollment inscripcion = new Enrollment();
        inscripcion.setId(5L);
        when(enrollmentRepository.findById(5L)).thenReturn(Optional.of(inscripcion));
        doNothing().when(enrollmentRepository).delete(inscripcion);

        assertDoesNotThrow(() -> enrollmentService.eliminarEnrollment(5L));
    }

    @Test
    void obtenerEnrollmentPorId_valido_devuelveInscripcion() {
        Enrollment inscripcion = new Enrollment();
        inscripcion.setId(7L);

        when(enrollmentRepository.findById(7L)).thenReturn(Optional.of(inscripcion));

        Enrollment resultado = enrollmentService.obtenerEnrollmentPorId(7L);

        assertEquals(7L, resultado.getId());
    }

    @Test
    void obtenerEnrollmentPorUsuario() {
        Enrollment e1 = new Enrollment();
        e1.setUserId(1L);
        Enrollment e2 = new Enrollment();
        e2.setUserId(1L);
        when(enrollmentRepository.findByUserId(1L)).thenReturn(Arrays.asList(e1, e2));

        List<Enrollment> resultado = enrollmentService.obtenerPorUsuario(1L);
        assertEquals(2, resultado.size());
    }

    @Test
    void actualizarProgreso_nota_estado() {
        Enrollment inscripcion = new Enrollment();
        inscripcion.setId(3L);

        when(enrollmentRepository.findById(3L)).thenReturn(Optional.of(inscripcion));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(inscripcion);

        Enrollment resultado = enrollmentService.actualizarEnrollment(3L, 75, 8.5, "completado", null);

        assertEquals(75, resultado.getProgreso());
        assertEquals(8.5, resultado.getNotaFinal());
        assertEquals("completado", resultado.getEstado());
    }
}

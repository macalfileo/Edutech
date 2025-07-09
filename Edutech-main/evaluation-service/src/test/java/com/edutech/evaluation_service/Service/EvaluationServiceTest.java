package com.edutech.evaluation_service.Service;

import com.edutech.evaluation_service.model.Evaluation;
import com.edutech.evaluation_service.repository.EvaluationRepository;
import com.edutech.evaluation_service.service.EvaluationService;
import com.edutech.evaluation_service.webclient.AuthClient;
import com.edutech.evaluation_service.webclient.CourseClient;
import com.edutech.evaluation_service.webclient.EnrollmentClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EvaluationServiceTest {

    @Mock
    private EvaluationRepository evaluationRepository;

    @Mock
    private CourseClient courseClient;

    @Mock
    private AuthClient authClient;

    @Mock
    private EnrollmentClient enrollmentClient;

    @InjectMocks
    private EvaluationService evaluationService;

    @Test
    void crearEvaluacion_valida() {
        Evaluation e = new Evaluation();
        e.setCourseId(1L);
        e.setUserId(99L); // ✅ Establece un userId

        when(authClient.usuarioPuedeCrearEvaluacion("token123")).thenReturn(true);
        when(authClient.usuarioExiste(eq(99L), eq("token123"))).thenReturn(true);
        when(courseClient.cursoExiste(eq(1L), eq("token123"))).thenReturn(true);
        when(enrollmentClient.tieneInscripciones(1L)).thenReturn(true);
        when(evaluationRepository.save(any(Evaluation.class))).thenReturn(e);

        Evaluation result = evaluationService.crearEvaluacion(e, "token123");

        assertEquals(1L, result.getCourseId());
        assertEquals(99L, result.getUserId());
    }

    @Test
    void crearEvaluacion_sinPermiso() {
        Evaluation e = new Evaluation();
        e.setCourseId(1L);

        when(authClient.usuarioPuedeCrearEvaluacion("token123")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            evaluationService.crearEvaluacion(e, "token123");
        });

        assertEquals("No tienes permiso para crear evaluaciones.", ex.getMessage());
    }

    @Test
    void actualizarEvaluacion_cursoInvalido() {
        Evaluation nueva = new Evaluation();
        nueva.setCourseId(77L);

        Evaluation existente = new Evaluation();
        existente.setId(1L);

        when(evaluationRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(courseClient.cursoExiste(77L, "token123")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            evaluationService.actualizarEvaluacion(1L, nueva, "token123");
        });

        assertEquals("El curso no existe o no se puede acceder.", ex.getMessage());
    }

    @Test
    void eliminarEvaluacion_existe() {
        when(evaluationRepository.existsById(1L)).thenReturn(true);
        boolean result = evaluationService.eliminarEvaluacion(1L);
        assertTrue(result);
        verify(evaluationRepository).deleteById(1L);
    }

    @Test
    void eliminarEvaluacion_noExiste() {
        when(evaluationRepository.existsById(1L)).thenReturn(false);
        boolean result = evaluationService.eliminarEvaluacion(1L);
        assertFalse(result);
    }

    @Test
    void obtenerEvaluacionPorId_existente() {
        Evaluation e = new Evaluation();
        e.setId(2L);
        when(evaluationRepository.findById(2L)).thenReturn(Optional.of(e));
        Optional<Evaluation> result = evaluationService.obtenerEvaluacionPorId(2L);
        assertTrue(result.isPresent());
        assertEquals(2L, result.get().getId());
    }

    @Test
    void listarEvaluaciones_returnLista() {
        when(evaluationRepository.findAll()).thenReturn(List.of(new Evaluation(), new Evaluation()));
        List<Evaluation> result = evaluationService.listarEvaluaciones();
        assertEquals(2, result.size());
    }

}

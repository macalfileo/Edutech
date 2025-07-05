
package com.edutech.course_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.edutech.course_service.model.Course;
import com.edutech.course_service.repository.CourseRepository;
import com.edutech.course_service.webclient.AuthClient;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private CourseService courseService;

    @Test // Crear curso válido
    void crear_CursoValido() {
        Course esperado = new Course();
        esperado.setTitulo("Java Básico");
        esperado.setDescripcion("Curso de introducción a Java");
        esperado.setInstructorId(1L);
        esperado.setDuracionMinutos(60);
        esperado.setCategoria("Programación");

        String token = "Bearer mockToken";

        when(authClient.usuarioPuedeModificarCurso(token, 1L)).thenReturn(true);
        when(courseRepository.save(any(Course.class))).thenReturn(esperado);

        Course resultado = courseService.crearCourse(
            token,
            "Java Básico",
            "Curso de introducción a Java",
            1L,
            60,
            "Programación"
        );

        assertEquals(esperado.getTitulo(), resultado.getTitulo());
        assertEquals(esperado.getDescripcion(), resultado.getDescripcion());
        assertEquals(esperado.getInstructorId(), resultado.getInstructorId());
        assertEquals(esperado.getDuracionMinutos(), resultado.getDuracionMinutos());
        assertEquals(esperado.getCategoria(), resultado.getCategoria());
    }

    @Test // Instructor no tiene permiso
    void crear_InstructorSinPermisos() {
        String token = "Bearer token";
        when(authClient.usuarioPuedeModificarCurso(token, 99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            courseService.crearCourse(token, "Java", "Intro", 99L, 60, "Programación");
        });

        assertEquals("El instructor con ID 99 no tiene permisos para este curso", ex.getMessage());
    }


    @Test // Datos inválidos
    void crear_DatosInvalidos() {
        String token = "Bearer token";
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            courseService.crearCourse(token, null, null, null, 30, null);
        });

        assertEquals("Todos los campos son obligatorios y la duración debe ser al menos 45 minutos", ex.getMessage());
    }


    @Test // Obtener curso por ID existente
    void obtenerCoursePorId_existente() {
        Course curso = new Course();
        curso.setId(1L);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(curso));

        Course resultado = courseService.obtenerCoursePorId(1L);
        assertEquals(1L, resultado.getId());
    }

    @Test // Obtener curso por ID inexistente
    void obtenerCoursePorId_noExiste() {
        when(courseRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            courseService.obtenerCoursePorId(2L);
        });

        assertEquals("Curso no encontrado Id: 2", ex.getMessage());
    }

    @Test // Eliminar curso exitosamente con permisos
    void eliminarCourse_exitoso() {
        String token = "Bearer mockToken";
        Long courseId = 10L;

        Course curso = new Course();
        curso.setId(courseId);
        curso.setInstructorId(3L); // El ID del instructor al que hay que validar

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(curso));
        when(authClient.usuarioPuedeModificarCurso(token, 3L)).thenReturn(true);

        String mensaje = courseService.eliminarCourse(token, courseId);

        assertEquals("Curso eliminado", mensaje);
        verify(courseRepository).delete(curso);
    }

    @Test
    void crearCurso_instructorSinPermiso_lanzaExcepcion() {
        when(authClient.usuarioPuedeModificarCurso("token123", 9L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            courseService.crearCourse("token123", "Curso X", "Descripción", 9L, 60, "DevOps");
        });

        assertEquals("El instructor con ID 9 no tiene permisos para este curso", ex.getMessage());
    }

    @Test
    void actualizarCurso_sinPermiso_lanzaExcepcion() {
        Course existente = new Course();
        existente.setId(4L);
        existente.setInstructorId(2L); // Instructor asignado al curso

        when(courseRepository.findById(4L)).thenReturn(Optional.of(existente));
        when(authClient.usuarioPuedeModificarCurso("abc123", 2L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            courseService.actualizarCourse("abc123", 4L, "Nuevo", "Actualizado", 2L, 80, "Java");
        });

        assertEquals("No tienes permiso para modificar este curso", ex.getMessage());
    }

}

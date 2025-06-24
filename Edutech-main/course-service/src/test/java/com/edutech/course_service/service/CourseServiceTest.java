
package com.edutech.course_service.service;

import static org.junit.jupiter.api.Assertions.*;
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

        when(authClient.existeInstructor(1L)).thenReturn(true);
        when(courseRepository.save(any(Course.class))).thenReturn(esperado);

        Course resultado = courseService.crearCourse(
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

    @Test // Instructor no existe
    void crear_InstructorNoExiste() {
        when(authClient.existeInstructor(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            courseService.crearCourse("Java", "Intro", 99L, 60, "Programación");
        });

        assertEquals("El instructor con ID 99 no existe", ex.getMessage());
    }

    @Test // Datos inválidos
    void crear_DatosInvalidos() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            courseService.crearCourse(null, null, null, 30, null);
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

    @Test // Eliminar curso
    void eliminarCourse_exitoso() {
        Course curso = new Course();
        curso.setId(10L);
        when(courseRepository.findById(10L)).thenReturn(Optional.of(curso));

        String mensaje = courseService.eliminarCourse(10L);

        assertEquals("Curso eliminado", mensaje);
        verify(courseRepository).delete(curso);
    }
}

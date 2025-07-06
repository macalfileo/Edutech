
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

import com.edutech.course_service.model.Modulo;
import com.edutech.course_service.model.Course;
import com.edutech.course_service.repository.ModuloRepository;
import com.edutech.course_service.webclient.AuthClient;
import com.edutech.course_service.repository.CourseRepository;

@ExtendWith(MockitoExtension.class)
public class ModuloServiceTest {

    @Mock
    private ModuloRepository moduloRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private ModuloService moduloService;
    
    @Test // Crear módulo válido con la autorización
    void crearModulo_valido() {
        Course curso = new Course();
        curso.setId(1L);
        curso.setInstructorId(1L);

        Modulo esperado = new Modulo();
        esperado.setTitulo("Intro");
        esperado.setDescripcion("Primer módulo");
        esperado.setOrden(1);
        esperado.setCurso(curso);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(moduloRepository.save(any(Modulo.class))).thenReturn(esperado);

        when(authClient.usuarioPuedeModificarCurso("Bearer token", 1L)).thenReturn(true); // 👈 Debe coincidir con el instructorId del curso

        Modulo resultado = moduloService.crearModulo("Bearer token", "Intro", "Primer módulo", 1, 1L);

        assertEquals("Intro", resultado.getTitulo());
        assertEquals("Primer módulo", resultado.getDescripcion());
        assertEquals(1, resultado.getOrden());
       assertEquals(curso, resultado.getCurso());
}



    @Test // Curso no existe
    void crearModulo_cursoNoExiste() {
        when(courseRepository.findById(5L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            moduloService.crearModulo("Bearer token", "Intro", "Primer módulo", 1, 5L);
        });

        assertEquals("Curso no encontrado Id: 5", ex.getMessage());
    }


    @Test // Obtener módulo por ID no existente
    void obtenerModuloPorId_noExiste() {
        when(moduloRepository.findById(8L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            moduloService.obtenerModuloPorId(8L);
        });

        assertEquals("Nódulo no encontrado Id: 8", ex.getMessage());
    }

    @Test // Eliminar módulo exitosamente con permisos
    void eliminarModulo_exitoso() {
        String token = "Bearer mockToken";

        Course curso = new Course();
        curso.setId(1L);
        curso.setInstructorId(3L);

        Modulo modulo = new Modulo();
        modulo.setId(10L);
        modulo.setCurso(curso);

        when(moduloRepository.findById(10L)).thenReturn(Optional.of(modulo));
        when(authClient.usuarioPuedeModificarCurso(token, 3L)).thenReturn(true);

        String mensaje = moduloService.eliminarModulo(token, 10L);

        assertEquals("Módulo eliminado", mensaje);
        verify(moduloRepository).delete(modulo);
    }

}

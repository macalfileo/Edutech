
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

import com.edutech.course_service.model.Contenido;
import com.edutech.course_service.model.Course;
import com.edutech.course_service.model.Modulo;
import com.edutech.course_service.repository.ContenidoRepository;
import com.edutech.course_service.repository.ModuloRepository;
import com.edutech.course_service.webclient.AuthClient;

@ExtendWith(MockitoExtension.class)
public class ContenidoServiceTest {

    @Mock
    private ContenidoRepository contenidoRepository;

    @Mock
    private ModuloRepository moduloRepository;

    @Mock
    private AuthClient authClient; // Asumiendo que AuthClient es necesario para verificar permisos

    @InjectMocks
    private ContenidoService contenidoService;

    @Test
    void crearContenido_valido() {
        String token = "Bearer abc123";
        Modulo modulo = new Modulo();
        modulo.setId(1L);
        modulo.setCurso(new Course());
        modulo.getCurso().setInstructorId(99L);

        Contenido esperado = new Contenido();
        esperado.setTitulo("Video Intro");
        esperado.setTipo("VIDEO");
        esperado.setUrl("https://example.com");
        esperado.setModulo(modulo);

        when(moduloRepository.findById(1L)).thenReturn(Optional.of(modulo));
        when(authClient.usuarioPuedeModificarCurso(token, 99L)).thenReturn(true);
        when(contenidoRepository.save(any(Contenido.class))).thenReturn(esperado);

        Contenido resultado = contenidoService.crearContenido(token, "Video Intro", "VIDEO", "https://example.com", 1L);

        assertEquals("Video Intro", resultado.getTitulo());
        assertEquals("VIDEO", resultado.getTipo());
        assertEquals("https://example.com", resultado.getUrl());
        assertEquals(modulo, resultado.getModulo());
    }


    @Test
    void crearContenido_moduloNoExiste() {
        when(moduloRepository.findById(5L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            contenidoService.crearContenido("Bearer abc", "Intro", "VIDEO", "url", 5L);
        });

        assertEquals("Módulo no encontrado", ex.getMessage());
    }


    @Test
    void eliminarContenido_exitoso() {
        Contenido contenido = new Contenido();
        contenido.setId(20L);
        contenido.setModulo(new Modulo());
        contenido.getModulo().setCurso(new Course());
        contenido.getModulo().getCurso().setInstructorId(88L);

        when(contenidoRepository.findById(20L)).thenReturn(Optional.of(contenido));
        when(authClient.usuarioPuedeModificarCurso("Bearer xyz", 88L)).thenReturn(true);

        String mensaje = contenidoService.eliminarContenido("Bearer xyz", 20L);

        assertEquals("Contenido eliminado", mensaje);
        verify(contenidoRepository).delete(contenido);
    }


    @Test
    void crearContenido_sinPermiso() {
        Modulo modulo = new Modulo();
        modulo.setId(2L);
        modulo.setCurso(new Course());
        modulo.getCurso().setInstructorId(99L);
    
        when(moduloRepository.findById(2L)).thenReturn(Optional.of(modulo));
        when(authClient.usuarioPuedeModificarCurso("Bearer abc", 99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            contenidoService.crearContenido("Bearer abc", "Intro", "VIDEO", "url", 2L);
        });

        assertEquals("No tienes permiso para agregar contenido a este módulo", ex.getMessage());
    }

}

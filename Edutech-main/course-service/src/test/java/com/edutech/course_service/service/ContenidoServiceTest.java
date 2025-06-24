
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
import com.edutech.course_service.model.Modulo;
import com.edutech.course_service.repository.ContenidoRepository;
import com.edutech.course_service.repository.ModuloRepository;

@ExtendWith(MockitoExtension.class)
public class ContenidoServiceTest {

    @Mock
    private ContenidoRepository contenidoRepository;

    @Mock
    private ModuloRepository moduloRepository;

    @InjectMocks
    private ContenidoService contenidoService;

    @Test // Crear contenido válido
    void crearContenido_valido() {
        Modulo modulo = new Modulo();
        modulo.setId(1L);

        Contenido esperado = new Contenido();
        esperado.setTitulo("Video Intro");
        esperado.setTipo("VIDEO");
        esperado.setUrl("https://example.com");
        esperado.setModulo(modulo);

        when(moduloRepository.findById(1L)).thenReturn(Optional.of(modulo));
        when(contenidoRepository.save(any(Contenido.class))).thenReturn(esperado);

        Contenido resultado = contenidoService.crearContenido("Video Intro", "VIDEO", "https://example.com", 1L);

        assertEquals("Video Intro", resultado.getTitulo());
        assertEquals("VIDEO", resultado.getTipo());
        assertEquals("https://example.com", resultado.getUrl());
        assertEquals(modulo, resultado.getModulo());
    }

    @Test // Modulo no existe
    void crearContenido_moduloNoExiste() {
        when(moduloRepository.findById(5L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            contenidoService.crearContenido("Intro", "VIDEO", "url", 5L);
        });

        assertEquals("Módulo no encontrado Id: 5", ex.getMessage());
    }

    @Test // Eliminar contenido exitoso
    void eliminarContenido_exitoso() {
        Contenido contenido = new Contenido();
        contenido.setId(20L);
        when(contenidoRepository.findById(20L)).thenReturn(Optional.of(contenido));

        String mensaje = contenidoService.eliminarContenido(20L);

        assertEquals("Contenido eliminado", mensaje);
        verify(contenidoRepository).delete(contenido);
    }
}

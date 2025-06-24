package com.edutech.community_service.service;

import com.edutech.community_service.model.Publicacion;
import com.edutech.community_service.repository.PublicacionRepository;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PublicacionServiceTest {

    @Mock
    private PublicacionRepository repository;

    @InjectMocks
    private PublicacionService service;

    public PublicacionServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrear() {
        Publicacion p = new Publicacion();
        p.setMensaje("Hola");
        p.setFechaPublicacion(LocalDate.now());

        when(repository.save(p)).thenReturn(p);

        Publicacion resultado = service.crear(p);
        assertEquals("Hola", resultado.getMensaje());
    }

    @Test
    void testListarTodas() {
        Publicacion p1 = new Publicacion();
        p1.setMensaje("Mensaje 1");

        Publicacion p2 = new Publicacion();
        p2.setMensaje("Mensaje 2");

        when(repository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Publicacion> publicaciones = service.listarTodas();
        assertEquals(2, publicaciones.size());
    }
}
package com.edutech.media_service.service;

import com.edutech.media_service.model.MediaFile;
import com.edutech.media_service.repository.MediaFileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MediaServiceTest {

    @InjectMocks
    private MediaService mediaService;

    @Mock
    private MediaFileRepository mediaFileRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarArchivo() throws IOException {
        MockMultipartFile archivo = new MockMultipartFile("file", "test.txt", "text/plain", "contenido".getBytes());
        MediaFile media = new MediaFile();
        media.setNombreArchivo("test.txt");
        media.setTipoArchivo("text/plain");
        media.setContenido("contenido".getBytes());

        when(mediaFileRepository.save(any())).thenReturn(media);

        MediaFile guardado = mediaService.guardarArchivo(archivo);

        assertNotNull(guardado);
        assertEquals("test.txt", guardado.getNombreArchivo());
        verify(mediaFileRepository).save(any());
    }

    @Test
    void testListarArchivos() {
        MediaFile mediaFile1 = new MediaFile();
        mediaFile1.setId(1L);
        mediaFile1.setNombreArchivo("a.png");
        mediaFile1.setTipoArchivo("image/png");
        mediaFile1.setContenido(new byte[]{});

        MediaFile mediaFile2 = new MediaFile();
        mediaFile2.setId(2L);
        mediaFile2.setNombreArchivo("b.jpg");
        mediaFile2.setTipoArchivo("image/jpeg");
        mediaFile2.setContenido(new byte[]{});

        List<MediaFile> lista = Arrays.asList(
                mediaFile1,
                mediaFile2
        );
        when(mediaFileRepository.findAll()).thenReturn(lista);

        List<MediaFile> resultado = mediaService.listarArchivos();

        assertEquals(2, resultado.size());
        verify(mediaFileRepository).findAll();
    }

    @Test
    void testActualizarArchivoExistente() throws IOException {
        MediaFile existente = new MediaFile();
        existente.setId(1L);
        existente.setNombreArchivo("viejo.txt");
        existente.setTipoArchivo("text/plain");
        existente.setContenido("abc".getBytes());
        MockMultipartFile nuevoArchivo = new MockMultipartFile("file", "nuevo.txt", "text/plain", "nuevo".getBytes());

        when(mediaFileRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(mediaFileRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        MediaFile actualizado = mediaService.actualizarArchivo(1L, nuevoArchivo);

        assertNotNull(actualizado);
        assertEquals("nuevo.txt", actualizado.getNombreArchivo());
        assertArrayEquals("nuevo".getBytes(), actualizado.getContenido());
    }

    @Test
    void testActualizarArchivoInexistente() throws IOException {
        MockMultipartFile nuevoArchivo = new MockMultipartFile("file", "nuevo.txt", "text/plain", "nuevo".getBytes());

        when(mediaFileRepository.findById(99L)).thenReturn(Optional.empty());

        MediaFile resultado = mediaService.actualizarArchivo(99L, nuevoArchivo);

        assertNull(resultado);
    }

    @Test
    void testEliminarArchivoExistente() {
        when(mediaFileRepository.existsById(1L)).thenReturn(true);

        boolean resultado = mediaService.eliminarArchivo(1L);

        assertTrue(resultado);
        verify(mediaFileRepository).deleteById(1L);
    }

    @Test
    void testEliminarArchivoInexistente() {
        when(mediaFileRepository.existsById(99L)).thenReturn(false);

        boolean resultado = mediaService.eliminarArchivo(99L);

        assertFalse(resultado);
        verify(mediaFileRepository, never()).deleteById(any());
    }
}

package com.edutech.media_service.service;

import com.edutech.media_service.model.MediaFile;
import com.edutech.media_service.repository.MediaFileRepository;
import com.edutech.media_service.webclient.CourseClient;
import com.edutech.media_service.webclient.EvaluationClient;
import com.edutech.media_service.webclient.UserProfileClient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MediaServiceTest {

    @Mock private MediaFileRepository mediaFileRepository;
    @Mock private CourseClient courseClient;
    @Mock private EvaluationClient evaluationClient;
    @Mock private UserProfileClient userProfileClient;

    @InjectMocks private MediaService mediaService;

    @Test
    void guardarArchivo_origenCurso_valido() {
        MediaFile archivo = new MediaFile();
        archivo.setCourseId(2L);
        archivo.setNombreArchivo("archivo.jpg");
        archivo.setOrigen("CURSO");

        when(courseClient.cursoExiste(2L, "token")).thenReturn(true);
        when(mediaFileRepository.save(any())).thenReturn(archivo);

        MediaFile resultado = mediaService.guardarArchivo(archivo, "token");
        assertEquals("archivo.jpg", resultado.getNombreArchivo());
    }

    @Test
    void guardarArchivo_origenCurso_invalido() {
        MediaFile archivo = new MediaFile();
        archivo.setCourseId(999L);
        archivo.setOrigen("CURSO");

        when(courseClient.cursoExiste(999L, "token")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            mediaService.guardarArchivo(archivo, "token");
        });

        assertEquals("Curso no válido o no existe.", ex.getMessage());
    }

    @Test
    void obtenerPorId_existente() {
        MediaFile archivo = new MediaFile();
        archivo.setId(1L);

        when(mediaFileRepository.findById(1L)).thenReturn(Optional.of(archivo));
        Optional<MediaFile> resultado = mediaService.obtenerPorId(1L);
        assertTrue(resultado.isPresent());
    }

    @Test
    void listarTodos_returnLista() {
        MediaFile archivo = new MediaFile();
        archivo.setId(1L);

        when(mediaFileRepository.findAll()).thenReturn(List.of(archivo));
        List<MediaFile> lista = mediaService.listarTodos();
        assertEquals(1, lista.size());
    }

    @Test
    void eliminarPorId_existe() {
        when(mediaFileRepository.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() -> mediaService.eliminarPorId(1L));
        verify(mediaFileRepository).deleteById(1L);
    }

    @Test
    void eliminarPorId_noExiste() {
        when(mediaFileRepository.existsById(2L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            mediaService.eliminarPorId(2L);
        });

        assertEquals("Archivo no encontrado", ex.getMessage());
    }

    @Test
    void obtenerPorCurso_returnLista() {
        MediaFile archivo = new MediaFile();
        archivo.setCourseId(2L);

        when(mediaFileRepository.findByCourseId(2L)).thenReturn(List.of(archivo));
        List<MediaFile> lista = mediaService.obtenerPorCurso(2L);
        assertEquals(1, lista.size());
    }

    @Test
    void obtenerPorEvaluacion_returnLista() {
        MediaFile archivo = new MediaFile();
        archivo.setEvaluationId(5L);
        archivo.setOrigen("EVALUACION");

        when(mediaFileRepository.findByEvaluationId(5L)).thenReturn(List.of(archivo));
        List<MediaFile> lista = mediaService.obtenerPorEvaluacion(5L);
        assertEquals(1, lista.size());
    }

    @Test
    void obtenerPorUsuario_returnLista() {
        MediaFile archivo = new MediaFile();
        archivo.setUserId(10L);
        archivo.setOrigen("USUARIO");

        when(mediaFileRepository.findByUserId(10L)).thenReturn(List.of(archivo));
        List<MediaFile> lista = mediaService.obtenerPorUsuario(10L);
        assertEquals(1, lista.size());
    }
}
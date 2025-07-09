package com.edutech.media_service.controller;

import com.edutech.media_service.model.MediaFile;
import com.edutech.media_service.service.MediaService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

@WebMvcTest(MediaFileController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MediaFileControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private MediaService mediaService;

    @Test // Subir archivo exitoso
    void subirArchivo_returnCreated() throws Exception {
        MediaFile m = new MediaFile();
        m.setId(1L); m.setNombreArchivo("test.jpg");
        when(mediaService.guardarArchivo(any(), eq("token"))).thenReturn(m);

        String body = """
        {
            "nombreArchivo": "test.jpg",
            "tipoArchivo": "image/jpeg",
            "contenido": "dGVzdA==",
            "courseId": 2,
            "origen": "CURSO"
        }
        """;

        mockMvc.perform(post("/api/v1/media")
                .header("Authorization", "token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nombreArchivo").value("test.jpg"));
    }

    @Test // Obtener por ID existente
    void obtenerPorId_existente() throws Exception {
        MediaFile archivo = new MediaFile(); archivo.setId(1L); archivo.setNombreArchivo("foto.png");
        when(mediaService.obtenerPorId(1L)).thenReturn(Optional.of(archivo));

        mockMvc.perform(get("/api/v1/media/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombreArchivo").value("foto.png"));
    }

    @Test // Obtener por ID no existente
    void obtenerPorId_noExiste() throws Exception {
        when(mediaService.obtenerPorId(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/media/2"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Archivo no encontrado"));
    }

    @Test // Listar archivos
    void listarArchivos_returnLista() throws Exception {
        MediaFile m = new MediaFile(); m.setNombreArchivo("doc.pdf");
        when(mediaService.listarTodos()).thenReturn(List.of(m));

        mockMvc.perform(get("/api/v1/media"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombreArchivo").value("doc.pdf"));
    }

    @Test // Eliminar archivo exitoso
    void eliminarArchivo_existe() throws Exception {
        doNothing().when(mediaService).eliminarPorId(1L);

        mockMvc.perform(delete("/api/v1/media/1"))
            .andExpect(status().isNoContent());
    }

    @Test // Eliminar archivo no existe
    void eliminarArchivo_noExiste() throws Exception {
        doThrow(new RuntimeException("Archivo no encontrado")).when(mediaService).eliminarPorId(2L);

        mockMvc.perform(delete("/api/v1/media/2"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Archivo no encontrado"));
    }

    @Test // Obtener por curso
    void obtenerPorCurso_returnLista() throws Exception {
        MediaFile m = new MediaFile(); m.setCourseId(2L); m.setNombreArchivo("imagen.jpg");
        when(mediaService.obtenerPorCurso(2L)).thenReturn(List.of(m));

        mockMvc.perform(get("/api/v1/media/curso/2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombreArchivo").value("imagen.jpg"));
    }

    @Test // Obtener por evaluación
    void obtenerPorEvaluacion_returnLista() throws Exception {
        MediaFile m = new MediaFile(); m.setEvaluationId(5L); m.setNombreArchivo("eval.pdf");
        when(mediaService.obtenerPorEvaluacion(5L)).thenReturn(List.of(m));

        mockMvc.perform(get("/api/v1/media/evaluacion/5"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombreArchivo").value("eval.pdf"));
    }

    @Test // Obtener por usuario
    void obtenerPorUsuario_returnLista() throws Exception {
        MediaFile m = new MediaFile(); m.setUserId(9L); m.setNombreArchivo("perfil.png");
        when(mediaService.obtenerPorUsuario(9L)).thenReturn(List.of(m));

        mockMvc.perform(get("/api/v1/media/usuario/9"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombreArchivo").value("perfil.png"));
    }
}

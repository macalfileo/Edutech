package com.edutech.media_service.controller;

import com.edutech.media_service.model.MediaFile;
import com.edutech.media_service.service.MediaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockBean;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MediaController.class)
public class MediaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MediaService mediaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
    }

    // Test para el endpoint de subir archivo
    @Test
    void testSubirArchivo() throws Exception {
        // Prepara el archivo para la solicitud
        MockMultipartFile archivo = new MockMultipartFile("archivo", "test.txt", "text/plain", "contenido".getBytes());

        // Crea un objeto MediaFile que será devuelto por el mock del servicio
        MediaFile media = new MediaFile();
        media.setNombreArchivo("test.txt");
        media.setTipoArchivo("text/plain");
        media.setContenido("contenido".getBytes());

        // Mockea el servicio para que devuelva el archivo simulado
        when(mediaService.guardarArchivo(any())).thenReturn(media);

        // Realiza la solicitud de subida de archivo usando multipart/form-data
        mockMvc.perform(multipart("/api/archivos/subir")
                        .file(archivo)  // Enviamos el archivo correctamente
                        .contentType("multipart/form-data"))  // Asegúrate de enviar el tipo correcto
                .andExpect(status().isOk())  // Se espera que el status sea 200 OK
                .andExpect(jsonPath("$.nombreArchivo").value("test.txt"));  // Verifica que el nombre del archivo es correcto
    }

    // Test para el endpoint de listar archivos
    @Test
    void testListarArchivos() throws Exception {
        // Crea una lista simulada de archivos
        MediaFile file1 = new MediaFile();
        file1.setId(1L);
        file1.setNombreArchivo("a.png");
        file1.setTipoArchivo("image/png");
        file1.setContenido(new byte[]{});

        MediaFile file2 = new MediaFile();
        file2.setId(2L);
        file2.setNombreArchivo("b.jpg");
        file2.setTipoArchivo("image/jpeg");
        file2.setContenido(new byte[]{});

        List<MediaFile> lista = Arrays.asList(file1, file2);
        when(mediaService.listarArchivos()).thenReturn(lista);  // Simula la respuesta del servicio

        // Realiza la solicitud para listar los archivos
        mockMvc.perform(get("/api/archivos"))
                .andExpect(status().isOk())  // Se espera que el status sea 200 OK
                .andExpect(jsonPath("$.length()").value(2));  // Verifica que la lista de archivos tenga 2 elementos
    }

    // Test para el endpoint de actualizar archivo
    @Test
    void testActualizarArchivo() throws Exception {
        MockMultipartFile nuevoArchivo = new MockMultipartFile("archivo", "nuevo.txt", "text/plain", "nuevo contenido".getBytes());

        // Crea un objeto MediaFile que será devuelto por el mock del servicio
        MediaFile media = new MediaFile();
        media.setId(1L);
        media.setNombreArchivo("nuevo.txt");
        media.setTipoArchivo("text/plain");
        media.setContenido("nuevo contenido".getBytes());

        // Mockea el servicio para que devuelva el archivo actualizado
        when(mediaService.actualizarArchivo(1L, nuevoArchivo)).thenReturn(media);

        // Realiza la solicitud de actualización de archivo usando PUT
        mockMvc.perform(multipart("/api/archivos/1")
                        .file(nuevoArchivo))  // Enviamos el archivo actualizado
                .andExpect(status().isOk())  // Se espera que el status sea 200 OK
                .andExpect(jsonPath("$.nombreArchivo").value("nuevo.txt"));  // Verifica que el nombre del archivo sea correcto
    }

    // Test para el caso de archivo no encontrado al actualizar
    @Test
    void testActualizarArchivoNoEncontrado() throws Exception {
        MockMultipartFile nuevoArchivo = new MockMultipartFile("archivo", "nuevo.txt", "text/plain", "nuevo contenido".getBytes());

        // Mockea el servicio para devolver null (archivo no encontrado)
        when(mediaService.actualizarArchivo(99L, nuevoArchivo)).thenReturn(null);

        // Realiza la solicitud de actualización de archivo usando PUT
        mockMvc.perform(multipart("/api/archivos/99")
                        .file(nuevoArchivo))  // Enviamos el archivo
                .andExpect(status().isNotFound());  // Se espera que el status sea 404 Not Found
    }
}

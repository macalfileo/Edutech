package com.edutech.community_service.controller;

// import com.edutech.community_service.controller.PublicacionController;
import com.edutech.community_service.model.Publicacion;
import com.edutech.community_service.service.PublicacionService;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
public class PublicacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublicacionService publicacionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test // Obtener todas las publicaciones
    void obtenerTodas_returnOKAndJson() throws Exception {
        Publicacion pub1 = new Publicacion(1L, "Ana", "Primer post", LocalDate.now(), 5);
        Publicacion pub2 = new Publicacion(2L, "Luis", "Segundo post", LocalDate.now(), 5);
        List<Publicacion> lista = Arrays.asList(pub1, pub2);

        when(publicacionService.listarTodas()).thenReturn(lista);

        mockMvc.perform(get("/api/publicaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].autor").value("Ana"))
                .andExpect(jsonPath("$[1].mensaje").value("Segundo post"));
    }

    @Test // Crear publicación válida
    void crearPublicacion_returnOKAndJson() throws Exception {
        Publicacion entrada = new Publicacion();
        entrada.setAutor("Angelo");
        entrada.setMensaje("Nuevo contenido");
        entrada.setCantidadLikes(0);

        Publicacion salida = new Publicacion();
        salida.setId(10L);
        salida.setAutor("Angelo");
        salida.setMensaje("Nuevo contenido");
        salida.setFechaPublicacion(LocalDate.now());
        salida.setCantidadLikes(5);

        when(publicacionService.crear(any(Publicacion.class))).thenReturn(salida);

        mockMvc.perform(post("/api/publicaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.autor").value("Angelo"))
                .andExpect(jsonPath("$.mensaje").value("Nuevo contenido"))
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test // Eliminar publicación existente
    void eliminarExistente_returnOK() throws Exception {
        when(publicacionService.eliminar(5L)).thenReturn(true);

        mockMvc.perform(delete("/api/publicaciones/5"))
                .andExpect(status().isOk());
    }

    @Test // Eliminar publicación inexistente
    void eliminarInexistente_return404() throws Exception {
        when(publicacionService.eliminar(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/publicaciones/99"))
                .andExpect(status().isNotFound());
    }
}
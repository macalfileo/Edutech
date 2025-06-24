package com.edutech.community_service.controller;

import com.edutech.community_service.model.Publicacion;
import com.edutech.community_service.service.PublicacionService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublicacionController.class)
@ExtendWith(SpringExtension.class)
@WithMockUser
class PublicacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublicacionService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearPublicacion_deberiaRetornar200() throws Exception {
        Publicacion pub = new Publicacion();
        pub.setId(1L);
        pub.setAutor("Angelo");
        pub.setMensaje("Prueba de creación");
        pub.setFechaPublicacion(LocalDate.now());
        pub.setCantidadLikes(0L);

        when(service.crear(any(Publicacion.class))).thenReturn(pub);

        mockMvc.perform(post("/api/publicaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pub)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.autor").value("Angelo"))
                .andExpect(jsonPath("$.mensaje").value("Prueba de creación"));
    }
}
package com.edutech.community_service.controller;

import com.edutech.community_service.model.Post;
import com.edutech.community_service.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for testing
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearPost_exitoso() throws Exception {
        Post post = new Post();
        post.setUserId(1L);
        post.setCourseId(2L);
        post.setContenido("Hola a todos");

        when(postService.crear(any(Post.class), eq("token"))).thenReturn(post);

        mockMvc.perform(post("/api/v1/comunidad")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "token")
                .content(objectMapper.writeValueAsString(post)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.contenido").value("Hola a todos"));
    }

    @Test
    void obtenerTodos_return200() throws Exception {
        when(postService.obtenerTodos()).thenReturn(List.of(new Post(), new Post()));

        mockMvc.perform(get("/api/v1/comunidad"))
            .andExpect(status().isOk());
    }

    @Test
    void obtenerPorCurso_return200() throws Exception {
        when(postService.obtenerPorCurso(5L)).thenReturn(List.of(new Post()));

        mockMvc.perform(get("/api/v1/comunidad/curso/5"))
            .andExpect(status().isOk());
    }

    @Test
    void obtenerPorUsuario_return200() throws Exception {
        when(postService.obtenerPorUsuario(3L)).thenReturn(List.of(new Post()));

        mockMvc.perform(get("/api/v1/comunidad/usuario/3"))
            .andExpect(status().isOk());
    }

    @Test
    void eliminarPost_existe() throws Exception {
        when(postService.eliminar(10L)).thenReturn("Publicación eliminada correctamente");

        mockMvc.perform(delete("/api/v1/comunidad/10"))
            .andExpect(status().isOk())
            .andExpect(content().string("Publicación eliminada correctamente"));
    }

    @Test
    void eliminarPost_noExiste() throws Exception {
        when(postService.eliminar(55L)).thenThrow(new RuntimeException("Publicación no encontrada. ID: 55"));

        mockMvc.perform(delete("/api/v1/comunidad/55"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Publicación no encontrada. ID: 55"));
    }
}

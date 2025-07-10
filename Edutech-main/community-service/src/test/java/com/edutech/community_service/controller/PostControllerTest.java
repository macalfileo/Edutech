package com.edutech.community_service.controller;

import com.edutech.community_service.model.Post;
import com.edutech.community_service.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@AutoConfigureMockMvc(addFilters = false)
public class PostControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    private ObjectMapper mapper;
    private Post post;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        post = new Post();
        post.setUserId(1L);
        post.setCourseId(2L);
        post.setContenido("Hola a todos");
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void crearPost_exitoso() throws Exception {
        when(postService.crear(any(Post.class), eq("token"))).thenReturn(post);

        mockMvc.perform(post("/api/v1/comunidad")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "token")
                .content(mapper.writeValueAsString(post)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.contenido").value("Hola a todos"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void obtenerTodos_return200() throws Exception {
        when(postService.obtenerTodos()).thenReturn(List.of(post));

        mockMvc.perform(get("/api/v1/comunidad"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "INSTRUCTOR")
    void obtenerPorCurso_return200() throws Exception {
        when(postService.obtenerPorCurso(2L)).thenReturn(List.of(post));

        mockMvc.perform(get("/api/v1/comunidad/curso/2"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void obtenerPorUsuario_return200() throws Exception {
        when(postService.obtenerPorUsuario(1L)).thenReturn(List.of(post));

        mockMvc.perform(get("/api/v1/comunidad/usuario/1"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void eliminarPost_existe() throws Exception {
        when(postService.eliminar(10L)).thenReturn("Publicación eliminada correctamente");

        mockMvc.perform(delete("/api/v1/comunidad/10"))
            .andExpect(status().isOk())
            .andExpect(content().string("Publicación eliminada correctamente"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void eliminarPost_noExiste() throws Exception {
        when(postService.eliminar(55L)).thenThrow(new RuntimeException("Publicación no encontrada. ID: 55"));

        mockMvc.perform(delete("/api/v1/comunidad/55"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Publicación no encontrada. ID: 55"));
    }
}

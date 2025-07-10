package com.edutech.chat_service.controller;

import com.edutech.chat_service.model.ChatMessage;
import com.edutech.chat_service.service.ChatMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatMessageController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for testing
public class ChatMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatMessageService chatMessageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void enviarMensaje_retorna201() throws Exception {
        ChatMessage msg = new ChatMessage(null, 1L, 10L, "Hola!", LocalDateTime.now());

        Mockito.when(chatMessageService.enviarMensaje(any(ChatMessage.class), anyString()))
                .thenReturn(msg);

        mockMvc.perform(post("/api/v1/chat")
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(msg)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.contenido").value("Hola!"));
    }

    @Test
    void listarTodos_admin200() throws Exception {
        ChatMessage m = new ChatMessage(1L, 1L, 1L, "msg", LocalDateTime.now());

        Mockito.when(chatMessageService.obtenerTodos())
                .thenReturn(List.of(m));

        mockMvc.perform(get("/api/v1/chat"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].contenido").value("msg"));
    }

    @Test
    void porCurso_200() throws Exception {
        ChatMessage m = new ChatMessage(1L, 10L, 1L, "msg curso", LocalDateTime.now());

        Mockito.when(chatMessageService.obtenerPorCurso(10L))
                .thenReturn(List.of(m));

        mockMvc.perform(get("/api/v1/chat/curso/10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].contenido").value("msg curso"));
    }

    @Test
    void porUsuario_200() throws Exception {
        ChatMessage m = new ChatMessage(1L, 1L, 50L, "hola user", LocalDateTime.now());

        Mockito.when(chatMessageService.obtenerPorUsuario(50L))
                .thenReturn(List.of(m));

        mockMvc.perform(get("/api/v1/chat/usuario/50"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].contenido").value("hola user"));
    }

    @Test
    void eliminar_ok() throws Exception {
        Mockito.when(chatMessageService.eliminarMensaje(1L)).thenReturn("Mensaje eliminado correctamente");

        mockMvc.perform(delete("/api/v1/chat/1"))
            .andExpect(status().isOk())
            .andExpect(content().string("Mensaje eliminado correctamente"));
    }

    @Test
    void eliminar_noEncontrado() throws Exception {
        Mockito.when(chatMessageService.eliminarMensaje(999L))
                .thenThrow(new RuntimeException("Mensaje no encontrado. ID: 999"));

        mockMvc.perform(delete("/api/v1/chat/999"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Mensaje no encontrado. ID: 999"));
    }
}
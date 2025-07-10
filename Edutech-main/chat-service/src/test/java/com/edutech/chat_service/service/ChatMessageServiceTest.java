package com.edutech.chat_service.service;

import com.edutech.chat_service.model.ChatMessage;
import com.edutech.chat_service.repository.ChatMessageRepository;
import com.edutech.chat_service.webclient.AuthClient;
import com.edutech.chat_service.webclient.CourseClient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatMessageServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private AuthClient authClient;

    @Mock
    private CourseClient courseClient;

    @InjectMocks
    private ChatMessageService chatMessageService;

    @Test
    void enviarMensaje_valido() {
        ChatMessage msg = new ChatMessage(null, 1L, 10L, "Hola!", LocalDateTime.now());

        when(authClient.usuarioExiste(10L, "token")).thenReturn(true);
        when(courseClient.cursoExiste(1L, "token")).thenReturn(true);
        when(chatMessageRepository.save(any())).thenReturn(msg);

        ChatMessage result = chatMessageService.enviarMensaje(msg, "token");
        assertEquals("Hola!", result.getContenido());
    }

    @Test
    void enviarMensaje_usuarioInvalido() {
        ChatMessage msg = new ChatMessage(null, 1L, 10L, "Mensaje", LocalDateTime.now());

        when(authClient.usuarioExiste(10L, "token")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            chatMessageService.enviarMensaje(msg, "token");
        });

        assertEquals("El usuario no existe o no tiene permisos.", ex.getMessage());
    }

    @Test
    void enviarMensaje_cursoInvalido() {
        ChatMessage msg = new ChatMessage(null, 999L, 10L, "Hola", LocalDateTime.now());

        when(authClient.usuarioExiste(10L, "token")).thenReturn(true);
        when(courseClient.cursoExiste(999L, "token")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            chatMessageService.enviarMensaje(msg, "token");
        });

        assertEquals("El curso no existe o no es accesible.", ex.getMessage());
    }

    @Test
    void obtenerPorCurso_ok() {
        ChatMessage m1 = new ChatMessage(1L, 20L, 5L, "msg1", LocalDateTime.now());
        when(chatMessageRepository.findByCursoIdOrderByFechaEnvioAsc(20L)).thenReturn(List.of(m1));

        List<ChatMessage> lista = chatMessageService.obtenerPorCurso(20L);
        assertEquals(1, lista.size());
    }

    @Test
    void obtenerPorUsuario_ok() {
        ChatMessage m1 = new ChatMessage(1L, 30L, 15L, "msg2", LocalDateTime.now());
        when(chatMessageRepository.findByUserId(15L)).thenReturn(List.of(m1));

        List<ChatMessage> lista = chatMessageService.obtenerPorUsuario(15L);
        assertEquals(1, lista.size());
    }

    @Test
    void eliminarMensaje_existente() {
        ChatMessage m = new ChatMessage(5L, 1L, 1L, "msg", LocalDateTime.now());
        when(chatMessageRepository.findById(5L)).thenReturn(Optional.of(m));

        String respuesta = chatMessageService.eliminarMensaje(5L);
        assertEquals("Mensaje eliminado correctamente", respuesta);
        verify(chatMessageRepository).delete(m);
    }

    @Test
    void eliminarMensaje_noEncontrado() {
        when(chatMessageRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            chatMessageService.eliminarMensaje(999L);
        });

        assertEquals("Mensaje no encontrado. ID: 999", ex.getMessage());
    }
}

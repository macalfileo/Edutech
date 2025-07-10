package com.edutech.chat_service.service;

import com.edutech.chat_service.model.ChatMessage;
import com.edutech.chat_service.repository.ChatMessageRepository;
import com.edutech.chat_service.webclient.AuthClient;
import com.edutech.chat_service.webclient.CourseClient;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private CourseClient courseClient;

    // Enviar un nuevo mensaje
    public ChatMessage enviarMensaje(ChatMessage mensaje, String token) {
        if (mensaje.getUserId() == null || mensaje.getCourseId() == null || mensaje.getContenido() == null) {
            throw new RuntimeException("El mensaje debe incluir userId, cursoId y contenido.");
        }

        if (!authClient.usuarioExiste(mensaje.getUserId(), token)) {
            throw new RuntimeException("El usuario no existe o no tiene permisos.");
        }

        if (!courseClient.cursoExiste(mensaje.getCourseId(), token)) {
            throw new RuntimeException("El curso no existe o no es accesible.");
        }

        return chatMessageRepository.save(mensaje);
    }

    // Obtener todos los mensajes
    public List<ChatMessage> obtenerTodos() {
        return chatMessageRepository.findAll();
    }

    // Obtener mensajes por curso (ordenados)
    public List<ChatMessage> obtenerPorCurso(Long cursoId) {
        return chatMessageRepository.findByCursoIdOrderByFechaEnvioAsc(cursoId);
    }

    // Obtener mensajes por usuario
    public List<ChatMessage> obtenerPorUsuario(Long userId) {
        return chatMessageRepository.findByUserId(userId);
    }

    // Eliminar mensaje por ID
    public String eliminarMensaje(Long id) {
        ChatMessage msg = chatMessageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Mensaje no encontrado. ID: " + id));

        chatMessageRepository.delete(msg);
        return "Mensaje eliminado correctamente";
    }
}
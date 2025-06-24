package com.edutech.chat_service.controller;

import com.edutech.chat_service.dto.MensajeDTO;
import com.edutech.chat_service.model.Mensaje;
import com.edutech.chat_service.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/enviar")
    public ResponseEntity<Mensaje> enviar(@RequestBody MensajeDTO dto) {
        return ResponseEntity.ok(chatService.enviarMensaje(dto));
    }

    @GetMapping("/conversacion")
    public List<Mensaje> obtenerConversacion(@RequestParam Long remitenteId,
                                              @RequestParam Long destinatarioId) {
        return chatService.obtenerConversacion(remitenteId, destinatarioId);
    }

    @GetMapping("/todos")
    public List<Mensaje> obtenerTodos() {
        return chatService.obtenerTodos();
    }

    public ChatService getChatService() {
        return chatService;
    }

    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
    }

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }
}
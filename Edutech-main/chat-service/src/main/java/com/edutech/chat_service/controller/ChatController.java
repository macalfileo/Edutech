package com.edutech.chat_service.controller;

import com.edutech.chat_service.dto.MensajeDTO;
import com.edutech.chat_service.model.Mensaje;
import com.edutech.chat_service.service.ChatService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Operation(summary = "Enviar un mensaje", description = "Envía un mensaje entre dos usuarios.")
    @ApiResponse(responseCode = "200", description = "Mensaje enviado exitosamente", content = @Content(schema = @Schema(implementation = Mensaje.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados", content = @Content(schema = @Schema(implementation = String.class)))
    @PostMapping("/enviar")
    public ResponseEntity<Mensaje> enviar(@RequestBody MensajeDTO dto) {
        return ResponseEntity.ok(chatService.enviarMensaje(dto));
    }

    @Operation(summary = "Obtener la conversación entre dos usuarios", description = "Recupera todos los mensajes enviados entre dos usuarios especificados por sus IDs.")
    @ApiResponse(responseCode = "200", description = "Conversación obtenida exitosamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Mensaje.class))))
    @ApiResponse(responseCode = "404", description = "Conversación no encontrada", content = @Content(schema = @Schema(implementation = String.class)))
    @GetMapping("/conversacion")
    public List<Mensaje> obtenerConversacion(@RequestParam Long remitenteId,
                                              @RequestParam Long destinatarioId) {
        return chatService.obtenerConversacion(remitenteId, destinatarioId);
    }

    @Operation(summary = "Obtener todos los mensajes", description = "Recupera todos los mensajes en el sistema.")
    @ApiResponse(responseCode = "200", description = "Mensajes obtenidos exitosamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Mensaje.class))))
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
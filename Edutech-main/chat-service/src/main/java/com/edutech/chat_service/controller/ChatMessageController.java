package com.edutech.chat_service.controller;

import com.edutech.chat_service.model.ChatMessage;
import com.edutech.chat_service.service.ChatMessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatMessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    @Operation(summary = "Enviar mensaje", description = "Permite a un usuario enviar un mensaje al chat de un curso.")
    @ApiResponse(responseCode = "201", description = "Mensaje enviado", content = @Content(schema = @Schema(implementation = ChatMessage.class)))
    @ApiResponse(responseCode = "400", description = "Error de validación", content = @Content)
    @PreAuthorize("hasAnyRole('CLIENTE','INSTRUCTOR')")
    @PostMapping
    public ResponseEntity<?> enviar(@RequestBody ChatMessage mensaje, @RequestHeader("Authorization") String token) {
        try {
            ChatMessage nuevo = chatMessageService.enviarMensaje(mensaje, token);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Listar todos los mensajes", description = "Devuelve todos los mensajes del sistema.")
    @ApiResponse(responseCode = "200", description = "Mensajes encontrados", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChatMessage.class))))
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<ChatMessage>> listarTodos() {
        return ResponseEntity.ok(chatMessageService.obtenerTodos());
    }

    @Operation(summary = "Mensajes por curso", description = "Devuelve los mensajes de un curso en orden cronológico.")
    @ApiResponse(responseCode = "200", description = "Mensajes encontrados", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChatMessage.class))))
    @PreAuthorize("hasAnyRole('CLIENTE','INSTRUCTOR','GERENTE_CURSOS')")
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<ChatMessage>> porCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(chatMessageService.obtenerPorCurso(cursoId));
    }

    @Operation(summary = "Mensajes por usuario", description = "Devuelve los mensajes enviados por un usuario específico.")
    @ApiResponse(responseCode = "200", description = "Mensajes encontrados", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChatMessage.class))))
    @PreAuthorize("hasAnyRole('CLIENTE','INSTRUCTOR','ADMINISTRADOR')")
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<ChatMessage>> porUsuario(@PathVariable Long userId) {
        return ResponseEntity.ok(chatMessageService.obtenerPorUsuario(userId));
    }

    @Operation(summary = "Eliminar mensaje", description = "Elimina un mensaje por su ID.")
    @ApiResponse(responseCode = "200", description = "Mensaje eliminado")
    @ApiResponse(responseCode = "404", description = "Mensaje no encontrado")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            String mensaje = chatMessageService.eliminarMensaje(id);
            return ResponseEntity.ok(mensaje);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
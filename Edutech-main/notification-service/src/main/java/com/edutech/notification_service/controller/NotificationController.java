package com.edutech.notification_service.controller;

import com.edutech.notification_service.model.Notification;
import com.edutech.notification_service.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notificaciones")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Operation(summary = "Crear notificación", description = "Crea una nueva notificación para un usuario.")
    @ApiResponse(responseCode = "201", description = "Notificación creada", content = @Content(schema = @Schema(implementation = Notification.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CLIENTE' , 'SOPORTE')")
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Notification noti, @RequestHeader("Authorization") String token) {
        try {
            Notification nueva = notificationService.crear(token, noti.getTitulo(), noti.getMensaje(), noti.getUsuarioId(), noti.getTipo());
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Obtener notificación por ID", description = "Devuelve una notificación específica por su ID.")
    @ApiResponse(responseCode = "200", description = "Notificación encontrada", content = @Content(schema = @Schema(implementation = Notification.class)))
    @ApiResponse(responseCode = "404", description = "Notificación no encontrada", content = @Content(schema = @Schema(implementation = String.class)))
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CLIENTE' , 'SOPORTE', 'INSTRUCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Notification> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.obtenerPorId(id));
    }

    @Operation(summary = "Listar todas las notificaciones", description = "Obtiene todas las notificaciones del sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de notificaciones", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Notification.class))))
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOPORTE')")
    @GetMapping
    public ResponseEntity<List<Notification>> listar() {
        return ResponseEntity.ok(notificationService.obtenerTodas());
    }

    @Operation(summary = "Obtener notificaciones por usuario", description = "Obtiene las notificaciones de un usuario.")
    @ApiResponse(responseCode = "200", description = "Lista de notificaciones del usuario", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Notification.class))))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CLIENTE' , 'SOPORTE', 'GERENTE_CURSOS', 'INSTRUCTOR')")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notification>> porUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificationService.obtenerPorUsuario(usuarioId));
    }

    @Operation(summary = "Obtener no leídas por usuario", description = "Devuelve las notificaciones no leídas de un usuario.")
    @ApiResponse(responseCode = "200", description = "Notificaciones no leídas encontradas", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Notification.class))))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CLIENTE' , 'INSTRUCTOR')")
    @GetMapping("/usuario/{usuarioId}/no-leidas")
    public ResponseEntity<List<Notification>> noLeidas(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificationService.obtenerNoLeidas(usuarioId));
    }

    @Operation(summary = "Marcar notificación como leída", description = "Marca una notificación como leída.")
    @ApiResponse(responseCode = "200", description = "Notificación marcada como leída", content = @Content(schema = @Schema(implementation = Notification.class)))
    @ApiResponse(responseCode = "404", description = "Notificación no encontrada", content = @Content(schema = @Schema(implementation = String.class)))
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'CLIENTE')")
    @PutMapping("/{id}/leida")
    public ResponseEntity<Notification> marcarComoLeida(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.marcarComoLeida(id));
    }

    @Operation(summary = "Eliminar notificación", description = "Elimina una notificación existente por ID.")
    @ApiResponse(responseCode = "204", description = "Notificación eliminada")
    @ApiResponse(responseCode = "404", description = "Notificación no encontrada", content = @Content(schema = @Schema(implementation = String.class)))
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            notificationService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
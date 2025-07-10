package com.edutech.chat_service.model;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mensajes_chat")
@Schema(description = "Mensaje enviado en el chat del curso")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del mensaje", example = "1")
    private Long id;

    @NotNull(message = "El ID del curso es obligatorio")
    @Column(name = "curso_id", nullable = false)
    @Schema(description = "ID del curso al que pertenece el chat", example = "12")
    private Long courseId;

    @NotNull(message = "El ID del remitente es obligatorio")
    @Column(name = "remitente_id", nullable = false)
    @Schema(description = "ID del usuario que envió el mensaje", example = "7")
    private Long userId;

    @NotBlank(message = "El contenido no puede estar vacío")
    @Column(nullable = false, length = 1000)
    @Schema(description = "Contenido del mensaje", example = "Hola, tengo una duda sobre la tarea.")
    private String contenido;

    @Column(name = "fecha_envio", nullable = false)
    @Schema(description = "Fecha y hora en que se envió el mensaje", example = "2025-07-09T18:45:00")
    private LocalDateTime fechaEnvio = LocalDateTime.now();
}
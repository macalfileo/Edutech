package com.edutech.chat_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mensajes") // Puedes personalizar el nombre de la tabla
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del mensaje", example = "1")
    private Long id;

    @NotNull(message = "El ID del remitente es obligatorio")
    @Column(nullable = false)
    @Schema(description = "ID del usuario remitente", example = "12345")
    private Long remitenteId;

    @NotNull(message = "El ID del destinatario es obligatorio")
    @Column(nullable = false)
    @Schema(description = "ID del usuario destinatario", example = "67890")
    private Long destinatarioId;

    @NotBlank(message = "El contenido no puede estar vacío")
    @Size(max = 500, message = "El contenido no puede exceder los 500 caracteres")
    @Column(nullable = false, length = 500)
    @Schema(description = "Contenido del mensaje", example = "Hola, ¿cómo estás?")
    private String contenido;

    @CreationTimestamp
    @Column(updatable = false)
    @Schema(description = "Fecha y hora de creación del mensaje", example = "2025-06-24T12:34:00")
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    @Schema(description = "Fecha y hora de la última actualización del mensaje", example = "2025-06-24T12:35:00")
    private LocalDateTime actualizadoEn;
}
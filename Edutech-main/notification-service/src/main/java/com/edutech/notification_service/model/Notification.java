package com.edutech.notification_service.model;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="Notificaciones")
@AllArgsConstructor
@NoArgsConstructor

public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la notificación", example = "1")
    private Long id;
    
    @NotBlank(message = "El título es obligatorio")
    @Column(nullable = false)
    @Schema(description = "Título de la notificación", example = "¡Nuevo curso disponible!")
    private String titulo;

    @NotBlank(message = "El mensaje es obligatorio")
    @Column(nullable = false, length = 1000)
    @Schema(description = "Cuerpo del mensaje de la notificación", example = "Ya puedes inscribirte en el curso de Java Avanzado.")
    private String mensaje;

    @NotNull(message = "El ID del usuario es obligatorio")
    @Column(name = "usuario_id", nullable = false)
    @Schema(description = "ID del usuario que recibe la notificación", example = "15")
    private Long userId;

    @NotBlank(message = "El tipo de notificación es obligatorio")
    @Column(name = "tipo", nullable = false)
    @Schema(description = "Tipo de notificación (ej: CURSO, EVALUACION, SISTEMA)", example = "CURSO")
    private String tipo;

    @Column(name = "fecha_envio", nullable = false)
    @Schema(description = "Fecha y hora de envío", example = "2025-07-09T10:30:00")
    private LocalDateTime fechaEnvio = LocalDateTime.now();

    @Column(name = "leida", nullable = false)
    @Schema(description = "Indica si la notificación fue leída", example = "false")
    private boolean leida = false;
}



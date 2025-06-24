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

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mensajes") // Puedes personalizar el nombre de la tabla
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del remitente es obligatorio")
    @Column(nullable = false)
    private Long remitenteId;

    @NotNull(message = "El ID del destinatario es obligatorio")
    @Column(nullable = false)
    private Long destinatarioId;

    @NotBlank(message = "El contenido no puede estar vacío")
    @Size(max = 500, message = "El contenido no puede exceder los 500 caracteres")
    @Column(nullable = false, length = 500)
    private String contenido;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    private LocalDateTime actualizadoEn;
}
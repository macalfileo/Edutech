package com.edutech.community_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts_comunidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Publicación dentro del foro de un curso")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del post", example = "1")
    private Long id;

    @NotNull(message = "El ID del curso es obligatorio")
    @Column(name = "curso_id", nullable = false)
    @Schema(description = "ID del curso asociado", example = "100")
    private Long courseId;

    @NotNull(message = "El ID del autor es obligatorio")
    @Column(name = "autor_id", nullable = false)
    @Schema(description = "ID del usuario que crea el post", example = "45")
    private Long userId;

    @NotBlank(message = "El contenido no puede estar vacío")
    @Column(nullable = false, length = 1000)
    @Schema(description = "Contenido del post", example = "¿Alguien tiene dudas sobre la evaluación 2?")
    private String contenido;

    @Column(name = "fecha_creacion", nullable = false)
    @Schema(description = "Fecha y hora de creación del post", example = "2025-07-10T13:45:00")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}

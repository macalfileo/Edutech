package com.edutech.report_service.model;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Entity
@Table(name = "reportes")
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa un reporte generado para un usuario")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del reporte", example = "1")
    private Long id;

    @NotNull(message = "El ID del usuario es obligatorio")
    @Column(nullable = false)
    @Schema(description = "ID del usuario relacionado al reporte", example = "42")
    private Long userId;

    @NotBlank(message = "La descripción del reporte no puede estar vacía")
    @Column(nullable = false, length = 1000)
    @Schema(description = "Descripción del contenido del reporte", example = "El usuario ha completado 5 evaluaciones con promedio 6.1.")
    private String descripcion;

    @NotBlank(message = "El tipo de reporte es obligatorio")
    @Column(nullable = false)
    @Schema(description = "Tipo de reporte (ej: DESEMPEÑO, PROGRESO, ALERTA)", example = "DESEMPEÑO")
    private String tipo;

    @Column(nullable = false)
    @Schema(description = "Fecha y hora de creación del reporte", example = "2025-07-10T14:30:00")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
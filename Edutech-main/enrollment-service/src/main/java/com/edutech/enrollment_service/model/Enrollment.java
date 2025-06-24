package com.edutech.enrollment_service.model;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inscripciones")
@Schema(description = "Inscripción de un usuario a un curso")

public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la inscripción", example = "1")
    private Long id;

    @NotNull(message = "El ID del usuario es obligatorio")
    @Schema(description = "ID del usuario inscrito", example = "100")
    private Long userId;

    @NotNull(message = "El ID del curso es obligatorio")
    @Schema(description = "ID del curso al que se inscribe", example = "200")
    private Long courseId;

    @PastOrPresent
    @Schema(description = "Fecha y hora de inscripción", example = "2025-06-24T12:34:00")
    private LocalDateTime fechaInscripcion;

    @NotBlank
    @Schema(description = "Estado de la inscripción", example = "ACTIVA")
    private String estado;

    @Min(0)
    @Max(100)
    @Schema(description = "Progreso en el curso", example = "85")
    private int progreso;

    @DecimalMin("0.0")
    @DecimalMax("7.0")
    @Schema(description = "Nota final del curso", example = "6.2")
    private Double notaFinal;

    @Schema(description = "Si se emitió el certificado", example = "true")
    private Boolean certificadoEmitido;
}
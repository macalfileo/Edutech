package com.edutech.evaluation_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "evaluaciones")
@Data
public class Evaluation {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la evaluación", example = "1")
    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 100, message = "El título no puede tener más de 100 caracteres")
    @Column(name = "titulo", nullable = false, length = 100)
    @Schema(description = "Título de la evaluación", example = "Evaluación de Matemáticas")
    private String title;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 500, message = "La descripción no puede tener más de 500 caracteres")
    @Column(name = "descripcion", nullable = false, length = 500)
    @Schema(description = "Descripción detallada de la evaluación", example = "Esta es una evaluación que cubre temas de álgebra y geometría.")
    private String description;
}
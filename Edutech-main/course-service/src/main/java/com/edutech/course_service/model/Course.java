package com.edutech.course_service.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "cursos")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Curso con detalles como título, descripción, duración, categoría e instructor")

public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del curso", example = "1")
    private Long id;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "El titulo no puede estar vacío")
    @Schema(description = "Título del curso", example = "Introducción a Java")
    private String titulo;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "La descripción no puede estar vacía")
    @Schema(description = "Descripción breve del curso", example = "Este curso cubre los fundamentos de Java.")
    private String descripcion;

    @Column(nullable = false)
    @NotNull(message = "Debe especificar el ID del instructor")
    @Schema(description = "ID del instructor que dicta el curso", example = "10")
    private Long instructorId; // ID del profesor (relación con UserService)

    @Column(nullable = false)
    @Min(value = 45, message = "La duracion debe ser de al menos de 45 minutos")
    @Schema(description = "Duración total del curso en minutos", example = "90")
    private int duracionMinutos;

    @Column(nullable = false, length = 50)
    @Size(min = 4, message = "La categoría debe tener al menos 3 caracteres")
    @NotBlank(message = "La categoría no puede estar vacía")
    @Schema(description = "Categoría o área temática del curso", example = "Programación")
    private String categoria;

    @CreationTimestamp // Fecha en la que se creo
    @Column(updatable = false)
    @Schema(description = "Fecha de creación del curso", example = "2025-06-15T10:30:00")
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    @Schema(description = "Fecha de última actualización del curso", example = "2025-06-15T18:00:00")
    private LocalDateTime actualizadoEn; // Fecha en la que se actualizado

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL) // Cursos tiene muchos modulos, si se borra un curso se borraran todos los modulos
    @JsonIgnore // No se incluiran los modulos cuando se hagan las consultas
    @Schema(description = "Lista de módulos asociados al curso (no incluidos en respuestas por defecto)")
    private List<Modulo> modulos; // Lista de todos los modulos que tienen este curso
}
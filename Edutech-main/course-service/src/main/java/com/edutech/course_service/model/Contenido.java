package com.edutech.course_service.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "contenidos")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Contenido de un módulo, como video, lectura o recurso descargable")

public class Contenido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del contenido", example = "1")
    private Long id;
    
    @Column(nullable = false, length = 200)
    @NotBlank(message = "El titulo no puede estar vacío")
    @Schema(description = "Título del contenido", example = "Introducción al curso")
    private String titulo;

    @Column(nullable = false, length = 500)
    @NotBlank(message = "La descripción no puede estar vacía")
    @Schema(description = "Descripción del contenido", example = "Este módulo cubre los fundamentos del curso.")
    private String descripcion;

    @Column(nullable = false, length = 30)
    @NotBlank(message = "El tipo de contenido es obligatorio")
    @Schema(description = "Tipo de contenido (VIDEO, PDF, etc.)", example = "VIDEO")
    
    private String tipo; // Ej: "video", "texto", "quiz", "archivo"

    @Column(length = 300)
    @Schema(description = "Enlace al contenido", example = "https://example.com/video1")
    private String url; // Incluir recursos externos o internos (videos, PDFs, etc.)


    @CreationTimestamp // Fecha en la que se creo
    @Column(updatable =  false)
    @Schema(description = "Fecha de creación del contenido", example = "2025-06-09T10:00:00")
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    @Schema(description = "Fecha de última actualización del contenido", example = "2024-02-10T15:00:00")
    private LocalDateTime actualizadoEn; // Fecha en la que se actualizado
    
    @ManyToOne // Muchos contenidos pueden tener un solo modulo
    @JoinColumn(name = "modulo_id") // Tabla contenido, tendra una llave foranea
    @JsonIgnoreProperties("contenidos") // Previene ciclos infinitos
    @NotNull(message = "Debe asignarse a un módulo")
    @Schema(description = "Módulo al que pertenece este contenido")
    private Modulo modulo;
}

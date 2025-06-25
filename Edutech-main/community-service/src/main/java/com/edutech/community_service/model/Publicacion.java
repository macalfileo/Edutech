package com.edutech.community_service.model;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "publicaciones") // Puedes personalizar el nombre de la tabla
@AllArgsConstructor
public class Publicacion {


    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la publicación", example = "1")
    private Long id;

    @NotBlank(message = "el nombre del autor no puede estar vacio")
    @Size(max = 50, message = "el nombre del autor no puede exceder 50 caracteres")
    @Schema(description = "Nombre del autor de la publicación", example = "Juan Pérez")
    private String autor;

    @NotBlank(message = "El mensaje no puede estar vacío")
    @Size(min = 5, max = 500, message = "El mensaje debe tener entre 5 y 500 caracteres")
    @Schema(description = "Contenido de la publicación", example = "Este es un mensaje de prueba")
    private String mensaje;

    @PastOrPresent(message = "La fecha de publicación no puede ser futura")
    @Schema(description = "Fecha en la que se publicó la publicación", example = "2025-06-24")
    private LocalDate fechaPublicacion;

    @Min(value = 0, message = "Los likes no pueden ser negativos")
    @Schema(description = "Cantidad de likes que tiene la publicación", example = "45")
    private int cantidadLikes;
}
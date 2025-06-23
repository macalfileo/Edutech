package com.edutech.community_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Data;

@Data
@Entity
public class Publicacion {


    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlack(message = "el nombre del autor no puede estar vacio")
    @Size(mas = 50, message = "el nombre del autor no puede exceder 50 caracteres")
    private String autor;

    @NotBlank(message = "El mensaje no puede estar vacío")
    @Size(min = 5, max = 500, message = "El mensaje debe tener entre 5 y 500 caracteres")
    private String mensaje;

    @PastOrPresent(message = "La fecha de publicación no puede ser futura")
    private LocalDate fechaPublicacion;

    @Min(value = 0, message = "Los likes no pueden ser negativos")
    private int cantidadLikes;
}
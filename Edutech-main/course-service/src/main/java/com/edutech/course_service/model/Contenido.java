package com.edutech.course_service.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

public class Contenido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    @NotBlank(message = "El titulo no puede estar vacío")
    private String titulo;

    @Column(nullable = false, length = 500)
    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    @Column(nullable = false, length = 30)
    @NotBlank(message = "El tipo de contenido es obligatorio")
    private String tipo; // Ej: "video", "texto", "quiz", "archivo"

    @Column(length = 300)
    private String url; // Incluir recursos externos o internos (videos, PDFs, etc.)


    @CreationTimestamp // Fecha en la que se creo
    @Column(updatable =  false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    private LocalDateTime actualizadoEn; // Fecha en la que se actualizado
    
    @ManyToOne // Muchos contenidos pueden tener un solo modulo
    @JoinColumn(name = "modulo_id") // Tabla contenido, tendra una llave foranea
    @JsonIgnoreProperties("contenidos") // Previene ciclos infinitos
    @NotNull(message = "Debe asignarse a un módulo")
    private Modulo modulo;
}

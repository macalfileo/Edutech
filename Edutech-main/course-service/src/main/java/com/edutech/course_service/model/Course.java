package com.edutech.course_service.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "El titulo no puede estar vacío")
    private String titulo;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    @Column(nullable = false)
    @NotNull(message = "Debe especificar el ID del instructor")
    private Long instructorId; // ID del profesor (relación con UserService)

    @Column(nullable = false)
    @Min(value = 45, message = "La duracion debe ser de al menos de 45 minutos")
    private int duracionMinutos;

    @Column(nullable = false, length = 50)
    @Size(min = 4, message = "La categoría debe tener al menos 3 caracteres")
    @NotBlank(message = "La categoría no puede estar vacía")
    private String categoria;

    @CreationTimestamp // Fecha en la que se creo
    @Column(updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    private LocalDateTime actualizadoEn; // Fecha en la que se actualizado

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL) // Cursos tiene muchos modulos, si se borra un curso se borraran todos los modulos
    @JsonIgnore // No se incluiran los modulos cuando se hagan las consultas
    private List<Modulo> modulos; // Lista de todos los modulos que tienen este curso
}
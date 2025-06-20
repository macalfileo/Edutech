package com.edutech.course_service.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "modulos")
@NoArgsConstructor
@AllArgsConstructor

public class Modulo {
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
    @Min(value = 1, message = "El orden debe ser al menos 1")
    private int orden; // Define la posición del módulo dentro del curso

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creadoEn; // Fecha exacta en la que se creo

    @UpdateTimestamp
    private LocalDateTime actualizadoEn; // Fecha exacta en la que se actualizo

    @ManyToOne // Muchos modulos pueden tener un solo curso
    @JoinColumn(name = "curso_id") // Tabla modulo, tendra una llave foranea
    @JsonIgnoreProperties("modulos") // Previene ciclos infinitos
    private Course curso;

    @OneToMany(mappedBy = "modulo", cascade = CascadeType.ALL) // Modulos tiene muchos contenidos, si se borra un contenido se borraran todos los usuario
    @JsonIgnore // No se incluiran los contenidos cuando se hagan las consultas
    private List<Contenido> contenidos; // Lista de todos los contenidos que tienen este modulo
}

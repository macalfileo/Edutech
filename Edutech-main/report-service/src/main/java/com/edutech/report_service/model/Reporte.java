package com.edutech.report_service.model;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del reporte", example = "1")
    private Long id;

    @Schema(description = "Tipo de reporte", example = "PDF")
    private String tipo;

    @Schema(description = "Contenido del reporte", example = "Este es un reporte de ventas mensual.")
    private String contenido;

    @Schema(description = "Fecha en que se generó el reporte", example = "2025-06-24")
    private LocalDate fechaGeneracion;

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getContenido() {
        return contenido;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
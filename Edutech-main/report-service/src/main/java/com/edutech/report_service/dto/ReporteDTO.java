package com.edutech.report_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que representa un reporte")
public class ReporteDTO {

    @Schema(description = "Tipo de reporte, por ejemplo, 'PDF', 'EXCEL', etc.")
    private Object tipo;

    @Schema(description = "Contenido del reporte que puede ser un texto, imagen o cualquier dato relevante")
    private Object contenido;

    public Object getTipo() {
        return tipo;
    }

    public Object getContenido() {
        return contenido;
    }

    public void setContenido(Object contenido) {
        this.contenido = contenido;
    }

    public void setTipo(Object tipo) {
        this.tipo = tipo;
    }

    // Getters y setters
}
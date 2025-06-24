package com.edutech.report_service.dto;

public class ReporteDTO {

    private Object tipo;
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
package com.edutech.userprofile_service.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Géneros disponibles para el perfil de usuario")
public enum Genero {
    // Enum para los géneros
    MASCULINO,
    FEMENINO,
    OTRO
}

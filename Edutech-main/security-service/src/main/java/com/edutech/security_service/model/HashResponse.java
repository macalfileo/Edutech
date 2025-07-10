package com.edutech.security_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HashResponse {

    @Schema(description = "Texto encriptado con algoritmo BCrypt", example = "$2a$10$...")
    private String hashed;
}
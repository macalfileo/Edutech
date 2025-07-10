package com.edutech.security_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HashRequest {

    @NotBlank(message = "El texto no puede estar vacío")
    @Schema(description = "Texto en texto plano que se desea encriptar", example = "secreto123")
    private String plainText;
}
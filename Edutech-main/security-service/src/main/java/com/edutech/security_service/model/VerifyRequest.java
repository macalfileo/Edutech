package com.edutech.security_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyRequest {

    @NotBlank
    @Schema(description = "Texto plano a comparar", example = "secreto123")
    private String plainText;

    @NotBlank
    @Schema(description = "Texto hasheado previamente", example = "$2a$10$...")
    private String hashed;
}
package com.edutech.security_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyResponse {

    @Schema(description = "Resultado de la verificación", example = "true")
    private boolean valid;
}
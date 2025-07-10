package com.edutech.security_service.controller;

import com.edutech.security_service.model.HashRequest;
import com.edutech.security_service.model.HashResponse;
import com.edutech.security_service.model.VerifyRequest;
import com.edutech.security_service.model.VerifyResponse;
import com.edutech.security_service.service.SecurityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/security")
public class SecurityController {

    @Autowired
    private SecurityService securityService;

    @Operation(summary = "Generar hash", description = "Hashea texto plano con BCrypt.")
    @ApiResponse(responseCode = "200", description = "Texto hasheado", content = @Content(schema = @Schema(implementation = HashResponse.class)))
    @ApiResponse(responseCode = "400", description = "Texto inválido", content = @Content(schema = @Schema(implementation = String.class)))
    @PostMapping("/hash")
    public ResponseEntity<?> generarHash(@Valid @RequestBody HashRequest request) {
        try {
            HashResponse response = securityService.hashTexto(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Verificar hash", description = "Verifica si el texto plano coincide con el hash BCrypt.")
    @ApiResponse(responseCode = "200", description = "Resultado de verificación", content = @Content(schema = @Schema(implementation = VerifyResponse.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(schema = @Schema(implementation = String.class)))
    @PostMapping("/verify")
    public ResponseEntity<?> verificarHash(@Valid @RequestBody VerifyRequest request) {
        try {
            VerifyResponse response = securityService.verificarTexto(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

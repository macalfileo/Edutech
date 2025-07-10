package com.edutech.security_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.edutech.security_service.model.HashRequest;
import com.edutech.security_service.model.HashResponse;
import com.edutech.security_service.model.VerifyRequest;
import com.edutech.security_service.model.VerifyResponse;

@Service
public class SecurityService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Hashea texto plano
    public HashResponse hashTexto(HashRequest request) {
        if (request.getPlainText() == null || request.getPlainText().trim().isEmpty()) {
            throw new RuntimeException("El texto a encriptar no puede estar vacío");
        }

        String hashed = passwordEncoder.encode(request.getPlainText());
        return new HashResponse(hashed);
    }

    // Verifica si el texto plano coincide con el hash
    public VerifyResponse verificarTexto(VerifyRequest request) {
        if (request.getPlainText() == null || request.getHashed() == null) {
            throw new RuntimeException("Texto plano y hash son requeridos");
        }

        boolean valido = passwordEncoder.matches(request.getPlainText(), request.getHashed());
        return new VerifyResponse(valido);
    }
}
package com.edutech.security_service.service;

import com.edutech.security_service.model.HashRequest;
import com.edutech.security_service.model.HashResponse;
import com.edutech.security_service.model.VerifyRequest;
import com.edutech.security_service.model.VerifyResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SecurityServiceTest {

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private SecurityService securityService;

    @Test
    void hashTexto_valido() {
        HashRequest request = new HashRequest();
        request.setPlainText("clave123");

        when(passwordEncoder.encode("clave123")).thenReturn("$2a$10$algo");

        HashResponse response = securityService.hashTexto(request);

        assertEquals("$2a$10$algo", response.getHashed());
    }

    @Test
    void hashTexto_invalido_vacio() {
        HashRequest request = new HashRequest();
        request.setPlainText("   ");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            securityService.hashTexto(request);
        });

        assertEquals("El texto a encriptar no puede estar vacío", ex.getMessage());
    }

    @Test
    void verificarTexto_valido_true() {
        VerifyRequest request = new VerifyRequest();
        request.setPlainText("secreto");
        request.setHashed("$2a$10$hash");

        when(passwordEncoder.matches("secreto", "$2a$10$hash")).thenReturn(true);

        VerifyResponse response = securityService.verificarTexto(request);

        assertTrue(response.isValid());
    }

    @Test
    void verificarTexto_valido_false() {
        VerifyRequest request = new VerifyRequest();
        request.setPlainText("secreto");
        request.setHashed("$2a$10$incorrecto");

        when(passwordEncoder.matches("secreto", "$2a$10$incorrecto")).thenReturn(false);

        VerifyResponse response = securityService.verificarTexto(request);

        assertFalse(response.isValid());
    }

    @Test
    void verificarTexto_faltanCampos() {
        VerifyRequest request = new VerifyRequest();
        request.setPlainText(null);
        request.setHashed(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            securityService.verificarTexto(request);
        });

        assertEquals("Texto plano y hash son requeridos", ex.getMessage());
    }
}
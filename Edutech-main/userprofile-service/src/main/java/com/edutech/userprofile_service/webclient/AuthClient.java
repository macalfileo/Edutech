package com.edutech.userprofile_service.webclient;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.beans.factory.annotation.Value;

@Component
public class AuthClient {

    private final WebClient webClient;

    // Constructor: construye WebClient con la base URL del AuthService
    public AuthClient(@Value("${auth.service.url}") String authServiceUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(authServiceUrl)
            .build();
    }

    public void eliminarUsuario(Long userId) {
        webClient.delete()
            .uri("/users/{id}", userId)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public Map<String, Object> obtenerUsuarioPorId(Long userId) {
        return webClient.get()
            .uri("/users/{id}", userId)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .block();
    }

    public boolean existeUsuario(Long userId) {
        try {
            webClient.get()
                .uri("/users/{id}", userId)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
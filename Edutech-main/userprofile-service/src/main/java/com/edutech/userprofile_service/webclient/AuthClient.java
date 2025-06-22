package com.edutech.userprofile_service.webclient;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AuthClient {

    private final WebClient webClient;

    public AuthClient(WebClient.Builder builder) {
        this.webClient = builder
            .baseUrl("http://localhost:8081/api/v1") // Cambia según el puerto real
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
            .bodyToMono(new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {})
            .block();
    }
}
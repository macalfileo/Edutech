package com.edutech.userprofile_service.webclient;


import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AuthConfig {

    private final WebClient webClient;

    public AuthConfig(WebClient.Builder builder) {
        this.webClient = builder
            .baseUrl("http://localhost:8081/api/v1")
            .build();
    }

    public Map<String, Object> obtenerUsuarioPorId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("El userId no puede ser nulo");
        }

        return this.webClient.get()
            .uri("/users/{id}", userId)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError(),
                response -> response.bodyToMono(String.class)
                    .map(body -> new RuntimeException("Usuario no encontrado en AuthService"))
            )
            .bodyToMono(new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {})
            .block();
    }
}

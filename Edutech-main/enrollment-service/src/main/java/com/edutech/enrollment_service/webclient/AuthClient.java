package com.edutech.enrollment_service.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AuthClient {

    private final WebClient webClient;

    public AuthClient(@Value("${auth.service.url}") String authServiceUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(authServiceUrl)
            .build();
    }

    public boolean usuarioExiste(Long userId, String authHeader) {
        try {
            webClient.get()
                .uri("/users/{id}", userId)
                .header("Authorization", authHeader)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

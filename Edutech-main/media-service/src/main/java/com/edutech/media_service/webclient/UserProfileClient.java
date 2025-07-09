package com.edutech.media_service.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UserProfileClient {

    private final WebClient webClient;

    public UserProfileClient(@Value("${userprofile.service.url}") String userProfileServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(userProfileServiceUrl)
                .build();
    }

    public boolean usuarioExiste(Long userId, String authHeader) {
        try {
            webClient.get()
                .uri("/usuarios/{id}", userId)
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
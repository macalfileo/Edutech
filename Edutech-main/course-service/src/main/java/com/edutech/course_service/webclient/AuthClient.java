package com.edutech.course_service.webclient;

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

    public boolean existeInstructor(Long instructorId) {
        try {
            webClient.get()
                .uri("/users/{id}", instructorId)
                .retrieve()
                .bodyToMono(Void.class)
                .block(); 
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}


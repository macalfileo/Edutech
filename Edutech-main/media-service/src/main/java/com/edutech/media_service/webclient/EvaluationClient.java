package com.edutech.media_service.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class EvaluationClient {

    private final WebClient webClient;

    public EvaluationClient(@Value("${evaluation.service.url}") String evaluationServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(evaluationServiceUrl)
                .build();
    }

    public boolean evaluacionExiste(Long evaluationId, String authHeader) {
        try {
            webClient.get()
                .uri("/evaluaciones/{id}", evaluationId)
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

package com.edutech.report_service.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class EvaluationClient {

    private final WebClient webClient;

    public EvaluationClient(@Value("${evaluation.service.url}") String evaluationUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(evaluationUrl)
            .build();
    }

    public String obtenerResumenEvaluaciones(Long userId) {
        try {
            return webClient.get()
                .uri("/evaluations/resumen/{userId}", userId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        } catch (Exception e) {
            return "No se pudo obtener el desempeño del usuario.";
        }
    }
}
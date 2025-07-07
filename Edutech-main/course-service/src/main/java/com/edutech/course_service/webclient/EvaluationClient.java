package com.edutech.course_service.webclient;

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

    public void eliminarEvaluacionesDeCurso(Long courseId) {
        try {
            webClient.delete()
                .uri("/evaluaciones/curso/{courseId}", courseId)
                .retrieve()
                .toBodilessEntity()
                .block();
        } catch (Exception e) {
        }
    }
}

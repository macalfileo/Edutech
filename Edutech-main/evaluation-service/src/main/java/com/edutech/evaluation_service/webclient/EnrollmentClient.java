package com.edutech.evaluation_service.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class EnrollmentClient {
    private final WebClient webClient;

    public EnrollmentClient(@Value("${enrollment.service.url}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public boolean tieneInscripciones(Long courseId) {
        try {
            var lista = webClient.get()
                .uri("/inscripciones/curso/{courseId}", courseId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
            return lista != null && !lista.equals("[]");
        } catch (Exception e) {
            return false;
        }
    }
}


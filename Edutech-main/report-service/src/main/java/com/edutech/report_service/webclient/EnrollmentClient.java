package com.edutech.report_service.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class EnrollmentClient {

    private final WebClient webClient;

    public EnrollmentClient(@Value("${enrollment.service.url}") String enrollmentUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(enrollmentUrl)
            .build();
    }

    public String obtenerResumenProgreso(Long userId) {
        try {
            return webClient.get()
                .uri("/inscripciones/progreso/{userId}", userId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        } catch (Exception e) {
            return "No se pudo obtener el progreso del usuario.";
        }
    }
}
package com.edutech.course_service.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class EnrollmentClient {

    private final WebClient webClient;

    public EnrollmentClient(@Value("${enrollment.service.url}") String enrollmentServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(enrollmentServiceUrl)
                .build();
    }

    public void eliminarInscripcionesDeCurso(Long courseId) {
        try {
            webClient.delete()
                .uri("/inscripciones/curso/{courseId}", courseId)
                .retrieve()
                .toBodilessEntity()
                .block();
        } catch (Exception e) {

        }
    }
}

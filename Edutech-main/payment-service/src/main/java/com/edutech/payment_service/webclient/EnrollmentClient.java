package com.edutech.payment_service.webclient;

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

    public boolean inscripcionExiste(Long enrollmentId, String token) {
        try {
            webClient.get()
                .uri("/enrollments/{id}", enrollmentId)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

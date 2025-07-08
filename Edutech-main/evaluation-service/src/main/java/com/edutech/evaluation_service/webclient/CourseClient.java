package com.edutech.evaluation_service.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CourseClient {
    private final WebClient webClient;

    public CourseClient(@Value("${course.service.url}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public boolean cursoExiste(Long courseId, String token) {
        try {
            webClient.get()
                .uri("/cursos/{id}", courseId)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean puedeModificarCurso(Long instructorId, String token) {
        try {
            return webClient.get()
                .uri("/auth/validate-course-access/{instructorId}", instructorId)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
        } catch (Exception e) {
            return false;
        }
    }
}


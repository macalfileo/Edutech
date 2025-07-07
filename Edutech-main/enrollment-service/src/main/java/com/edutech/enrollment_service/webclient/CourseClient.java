package com.edutech.enrollment_service.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CourseClient {

    private final WebClient webClient;

    public CourseClient(@Value("${course.service.url}") String courseServiceUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(courseServiceUrl)
            .build();
    }

    public boolean cursoExiste(Long courseId, String authHeader) {
        try {
            webClient.get()
                .uri("/courses/{id}", courseId)
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


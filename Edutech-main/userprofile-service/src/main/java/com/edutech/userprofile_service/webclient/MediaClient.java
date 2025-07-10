package com.edutech.userprofile_service.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class MediaClient {

    private final WebClient webClient;

    public MediaClient(@Value("${media.service.url}") String mediaServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(mediaServiceUrl)
                .build();
    }

    public boolean avatarExiste(String url) {
        try {
            webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/archivos/validar")
                    .queryParam("url", url)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
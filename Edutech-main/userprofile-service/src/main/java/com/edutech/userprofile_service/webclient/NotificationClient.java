package com.edutech.userprofile_service.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class NotificationClient {

    private final WebClient webClient;

    public NotificationClient(@Value("${notification.service.url}") String notificationServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(notificationServiceUrl)
                .build();
    }

    public void enviarNotificacionPerfil(Long userId, String titulo, String mensaje) {
        try {
            webClient.post()
                .uri("/notificaciones")
                .bodyValue(Map.of(
                    "usuarioId", userId,
                    "titulo", titulo,
                    "mensaje", mensaje,
                    "tipo", "PERFIL"
                ))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        } catch (Exception e) {
            System.err.println("No se pudo enviar la notificación: " + e.getMessage());
        }
    }
}

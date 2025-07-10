package com.edutech.notification_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.ExternalDocumentation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI apiInfo() {
        return new OpenAPI()
            .info(new Info()
                .title("EduTech Notification Service API")
                .version("1.0.0")
                .description("Microservicio para la creación, consulta, lectura y eliminación de notificaciones asociadas a usuarios y eventos en la plataforma EduTech.")
            );
    }
}

package com.edutech.payment_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI enrollmentOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("EduTech Payment Service API")
                .version("1.0.0")
                .description("Microservicio para gestionar pagos asociados a inscripciones de cursos en EduTech.")
            );
    }
}
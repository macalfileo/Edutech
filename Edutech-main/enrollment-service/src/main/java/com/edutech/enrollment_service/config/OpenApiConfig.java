package com.edutech.enrollment_service.config;

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
                .title("EduTech Enrollment Service API")
                .version("1.0.0")
                .description("Microservicio encargado de gestionar las inscripciones de usuarios a cursos en EduTech.")
            );
    }
}


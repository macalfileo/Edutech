package com.edutech.report_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI apiInfo() {
        return new OpenAPI()
            .info(new Info()
                .title("EduTech Report Service API")
                .version("1.0.0")
                .description("Microservicio para generar y consultar reportes académicos, de desempeño y progreso en la plataforma EduTech.")
            );
    }
}


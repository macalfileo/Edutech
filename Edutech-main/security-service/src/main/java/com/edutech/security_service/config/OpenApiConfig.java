package com.edutech.security_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI securityOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("EduTech Security Service API")
                .version("1.0.0")
                .description("Microservicio utilitario para funciones de seguridad como hash y verificación de contraseñas en EduTech.")
            );
    }
}

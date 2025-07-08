package com.edutech.course_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI apiInfo(){
        return new OpenAPI()
            .info(new Info()
                    .title("EduTech Course Service API")
                    .version("1.0.0")
                    .description("Microservicio para la creación, edición y gestión de cursos, módulos y contenidos de la plataforma EduTech.")
            
        );
    }
}

package com.edutech.media_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MediaServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MediaServiceApplication.class, args);
        // http://localhost:8091/swagger-ui/index.html 
        // Consola de Swagger para probar los endpoints
    }
}
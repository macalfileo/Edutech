package com.edutech.security_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecurityServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityServiceApplication.class, args);
        // http://localhost:8090/swagger-ui/index.html 
        // Consola de Swagger para probar los endpoints

    }
}
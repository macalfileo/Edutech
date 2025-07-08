package com.edutech.enrollment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EnrollmentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnrollmentServiceApplication.class, args);

        // http://localhost:8083/swagger-ui/index.html 
        // Consola de Swagger para probar los endpoints
    }
}
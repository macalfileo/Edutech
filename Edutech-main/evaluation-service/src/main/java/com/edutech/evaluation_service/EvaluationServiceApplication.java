package com.edutech.evaluation_service;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class EvaluationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EvaluationServiceApplication.class, args);
        // http://localhost:8084/swagger-ui/index.html 
        // Consola de Swagger para probar los endpoints

    }
}
package com.edutech.course_service;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CourseServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(CourseServiceApplication.class, args);

        // http://localhost:8082/swagger-ui/index.html 
        // Consola de Swagger para probar los endpoints
    }
}


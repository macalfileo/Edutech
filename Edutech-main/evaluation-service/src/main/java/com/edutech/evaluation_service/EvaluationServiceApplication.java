package com.edutech.evaluation_service;

import com.edutech.evaluation_service.model.Evaluation;
import com.edutech.evaluation_service.service.EvaluationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class EvaluationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EvaluationServiceApplication.class, args);

        EvaluationService service = new EvaluationService();

        Evaluation evaluation = new Evaluation();
        evaluation.setId(1L);
        evaluation.setTitle("Prueba Java");
        evaluation.setDescription("Examen básico de Java");

        service.createEvaluation(evaluation);
        service.getAllEvaluations().forEach(e -> {
            System.out.println("Evaluación: " + e.getTitle() + " - " + e.getDescription());
        });
    }
}
package com.edutech.enrollment_service;

import com.edutech.enrollment_service.model.Enrollment;
import com.edutech.enrollment_service.service.EnrollmentService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class EnrollmentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnrollmentServiceApplication.class, args);

        EnrollmentService service = new EnrollmentService();

        Enrollment enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setUserId(101L);
        enrollment.setCourseId(202L);

        service.registerEnrollment(enrollment);
        service.getAllEnrollments().forEach(e -> {
            System.out.println("Inscripción -> Usuario: " + e.getUserId() + ", Curso: " + e.getCourseId());
        });
    }
}
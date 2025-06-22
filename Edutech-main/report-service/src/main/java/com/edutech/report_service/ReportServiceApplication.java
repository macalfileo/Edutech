package com.edutech.report_service;

import com.edutech.report_service.model.Report;
import com.edutech.report_service.service.ReportService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ReportServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReportServiceApplication.class, args);

        ReportService reportService = new ReportService();

        Report report = new Report();
        report.setId(1L);
        report.setStudentName("Juan Pérez");
        report.setGrade(85);

        reportService.addReport(report);
        reportService.getAllReports().forEach(r -> {
            System.out.println("Reporte: " + r.getStudentName() + " - Nota: " + r.getGrade());
        });
    }
}
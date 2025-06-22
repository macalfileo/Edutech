package com.edutech.report_service.model;

import lombok.Data;

@Data
public class Report {
    private Long id;
    private String studentName;
    private int grade;
}
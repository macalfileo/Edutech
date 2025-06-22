package com.edutech.enrollment_service.model;

import lombok.Data;

@Data
public class Enrollment {
    private Long id;
    private Long userId;
    private Long courseId;
}
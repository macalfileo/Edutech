package com.edutech.report_service.service;

import com.edutech.report_service.model.Report;
import java.util.ArrayList;
import java.util.List;

public class ReportService {
    private final List<Report> reports = new ArrayList<>();

    public void addReport(Report report) {
        reports.add(report);
    }

    public List<Report> getAllReports() {
        return reports;
    }
}
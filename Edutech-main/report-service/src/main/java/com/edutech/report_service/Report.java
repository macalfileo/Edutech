package com.edutech.report_service;

public class Report {

    private String studentName;
    private int grade;

    public void setId(long l) {
    }

    public void setStudentName(String string) {
        this.studentName = string;
    }

    public void setGrade(int i) {
        this.grade = i;
    }

    public String getStudentName() {
        return this.studentName;
    }

    public int getGrade() {
        return this.grade;
    }

}

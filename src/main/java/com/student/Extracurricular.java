package com.student;

public class Extracurricular {

    private String activityName;
    private String role;
    private String academicYear;

    // Constructor
    public Extracurricular(String activityName, String role, String academicYear) {
        this.activityName = activityName;
        this.role = role;
        this.academicYear = academicYear;
    }

    // Getters
    public String getActivityName() { return activityName; }
    public String getRole() { return role; }
    public String getAcademicYear() { return academicYear; }
}
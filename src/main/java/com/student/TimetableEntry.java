package com.student;

public class TimetableEntry {

    private String dayOfWeek;
    private String period;
    private String courseCode;
    private String facultyCode;

    // Constructor
    public TimetableEntry(String dayOfWeek, String period, String courseCode, String facultyCode) {
        this.dayOfWeek = dayOfWeek;
        this.period = period;
        this.courseCode = courseCode;
        this.facultyCode = facultyCode;
    }

    // Getters
    public String getDayOfWeek() { return dayOfWeek; }
    public String getPeriod() { return period; }
    public String getCourseCode() { return courseCode; }
    public String getFacultyCode() { return facultyCode; }
}
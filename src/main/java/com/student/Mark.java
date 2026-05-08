package com.student;

public class Mark {

    private String courseCode;
    private int marksObtained;
    private int totalMarks;
    private String grade;

    // Constructor
    public Mark(String courseCode, int marksObtained, int totalMarks, String grade) {
        this.courseCode = courseCode;
        this.marksObtained = marksObtained;
        this.totalMarks = totalMarks;
        this.grade = grade;
    }

    // Getters
    public String getCourseCode() { return courseCode; }
    public int getMarksObtained() { return marksObtained; }
    public int getTotalMarks() { return totalMarks; }
    public String getGrade() { return grade; }
}
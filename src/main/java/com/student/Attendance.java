package com.student;

import java.sql.Date;

public class Attendance {

    private int studentId; // <-- ADDED THIS
    private String courseCode;
    private Date date;
    private String status;

    // Constructor (updated)
    public Attendance(int studentId, String courseCode, Date date, String status) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.date = date;
        this.status = status;
    }

    // Simpler constructor (we'll remove this later, but it's needed for StudentDAO)
    public Attendance(String courseCode, Date date, String status) {
        this.courseCode = courseCode;
        this.date = date;
        this.status = status;
    }


    // Getters
    public int getStudentId() { return studentId; } // <-- ADDED THIS
    public String getCourseCode() { return courseCode; }
    public Date getDate() { return date; }
    public String getStatus() { return status; }

    // Setter (we need this for the new panel)
    public void setStatus(String status) { this.status = status; }
}

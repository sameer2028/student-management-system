package com.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDAO {

    /**
     * Fetches a student's profile information from the database
     * based on their user_id.
     */
    // Add this method INSIDE the StudentDAO class
   // Add this method INSIDE the StudentDAO class
    
public static java.util.List<Mark> getMarksByStudentId(int studentId) {
    java.util.List<Mark> marksList = new java.util.ArrayList<>();
    String sql = "SELECT course_code, marks_obtained, total_marks, grade FROM marks WHERE student_id = ?";

    try (Connection conn = DBManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, studentId);

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String courseCode = rs.getString("course_code");
                int marksObtained = rs.getInt("marks_obtained");
                int totalMarks = rs.getInt("total_marks");
                String grade = rs.getString("grade");
                marksList.add(new Mark(courseCode, marksObtained, totalMarks, grade));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return marksList;
}
// Add this method INSIDE the StudentDAO class
    
public static java.util.List<TimetableEntry> getTimetableBySection(String section) {
    java.util.List<TimetableEntry> timetable = new java.util.ArrayList<>();
    String sql = "SELECT day_of_week, period, course_code, faculty_code FROM timetable WHERE course_section = ?";

    try (Connection conn = DBManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, section);

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                timetable.add(new TimetableEntry(
                    rs.getString("day_of_week"),
                    rs.getString("period"),
                    rs.getString("course_code"),
                    rs.getString("faculty_code")
                ));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return timetable;
} 
// Add these two methods INSIDE the StudentDAO class

public static FeeSummary getFeeSummary(int studentId, int semester) {
    FeeSummary summary = null;
    String sql = "SELECT * FROM fee_summary WHERE student_id = ? AND semester = ?";

    try (Connection conn = DBManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, studentId);
        pstmt.setInt(2, semester);

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                summary = new FeeSummary();
                summary.setSummaryId(rs.getInt("summary_id"));
                summary.setSemester(rs.getInt("semester"));
                summary.setTotalDue(rs.getBigDecimal("total_due"));
                summary.setTotalPaid(rs.getBigDecimal("total_paid"));
                summary.setStatus(rs.getString("status"));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return summary;
}
// Add this method INSIDE the StudentDAO class
    
public static java.util.List<Extracurricular> getExtracurriculars(int studentId) {
    java.util.List<Extracurricular> activities = new java.util.ArrayList<>();
    String sql = "SELECT activity_name, role, academic_year FROM extracurriculars " +
                 "WHERE student_id = ? ORDER BY academic_year DESC";

    try (Connection conn = DBManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, studentId);

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                activities.add(new Extracurricular(
                    rs.getString("activity_name"),
                    rs.getString("role"),
                    rs.getString("academic_year")
                ));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return activities;
}
// Add this method INSIDE the StudentDAO class
    
public static java.util.List<Notice> getNotices(String section) {
    java.util.List<Notice> notices = new java.util.ArrayList<>();
    // This query selects notices for 'ALL' or for the student's specific section
    String sql = "SELECT title, content, posted_by, posted_date FROM notices " +
                 "WHERE target_section = 'ALL' OR target_section = ? " +
                 "ORDER BY posted_date DESC";

    try (Connection conn = DBManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, section); // Bind the student's section (e.g., 'D')

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                notices.add(new Notice(
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getString("posted_by"),
                    rs.getTimestamp("posted_date")
                ));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return notices;
}
public static java.util.List<FeeDetail> getFeeDetails(int summaryId) {
    java.util.List<FeeDetail> details = new java.util.ArrayList<>();
    String sql = "SELECT description, amount FROM fee_details WHERE summary_id = ?";

    try (Connection conn = DBManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, summaryId);

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                details.add(new FeeDetail(
                    rs.getString("description"),
                    rs.getBigDecimal("amount")
                ));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return details;
}
public static java.util.List<Attendance> getAttendanceByStudentId(int studentId) {
    java.util.List<Attendance> attendanceList = new java.util.ArrayList<>();
    String sql = "SELECT course_code, date, status FROM attendance WHERE student_id = ? ORDER BY date DESC";

    try (Connection conn = DBManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, studentId);

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String courseCode = rs.getString("course_code");
                java.sql.Date date = rs.getDate("date");
                String status = rs.getString("status");
                attendanceList.add(new Attendance(courseCode, date, status));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return attendanceList;
}
    public static Student getStudentByUserId(int userId) {
        Student student = null;
        String sql = "SELECT * FROM students WHERE user_id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    student = new Student();
                    student.setStudentId(rs.getInt("student_id"));
                    student.setUserId(rs.getInt("user_id"));
                    student.setRollNo(rs.getString("roll_no"));
                    student.setFirstName(rs.getString("first_name"));
                    student.setLastName(rs.getString("last_name"));
                    student.setDob(rs.getDate("dob"));
                    student.setGender(rs.getString("gender"));
                    student.setContact(rs.getString("contact"));
                    student.setAddress(rs.getString("address"));
                    student.setCourse(rs.getString("course"));
                    student.setYear(rs.getInt("year"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }
}

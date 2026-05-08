package com.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles all database operations for the Teacher dashboard.
 */
public class TeacherDAO {

    /**
     * Fetches ALL students from the database.
     */
    public static List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();
        String sql = "SELECT student_id, user_id, roll_no, first_name, last_name, course, year FROM students";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Student student = new Student();
                student.setStudentId(rs.getInt("student_id"));
                student.setUserId(rs.getInt("user_id"));
                student.setRollNo(rs.getString("roll_no"));
                student.setFirstName(rs.getString("first_name"));
                student.setLastName(rs.getString("last_name"));
                student.setCourse(rs.getString("course"));
                student.setYear(rs.getInt("year"));
                studentList.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentList;
    }

    /**
     * Deletes a student by their user_id.
     */
    public static boolean deleteStudentByUserId(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Adds a new student to the database. (Transaction)
     */
    public static boolean addNewStudent(Student student, String username, String email, String password) {
        Connection conn = null;
        try {
            conn = DBManager.getConnection();
            conn.setAutoCommit(false);

            // --- Step 1: Insert into 'users' table ---
            String hashedPassword = PasswordUtil.hashPassword(password);
            String sqlUser = "INSERT INTO users (username, email, password_hash, role) VALUES (?, ?, ?, 'STUDENT')";

            int newUserId = -1;

            try (PreparedStatement pstmtUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {
                pstmtUser.setString(1, username);
                pstmtUser.setString(2, email);
                pstmtUser.setString(3, hashedPassword);
                pstmtUser.executeUpdate();

                try (ResultSet rs = pstmtUser.getGeneratedKeys()) {
                    if (rs.next()) {
                        newUserId = rs.getInt(1);
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            }

            // --- Step 2: Insert into 'students' table ---
            student.setUserId(newUserId);

            String sqlStudent = "INSERT INTO students (user_id, roll_no, first_name, last_name, dob, gender, contact, address, course, year) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmtStudent = conn.prepareStatement(sqlStudent)) {
                pstmtStudent.setInt(1, student.getUserId());
                pstmtStudent.setString(2, student.getRollNo());
                pstmtStudent.setString(3, student.getFirstName());
                pstmtStudent.setString(4, student.getLastName());
                pstmtStudent.setDate(5, student.getDob());
                pstmtStudent.setString(6, student.getGender());
                pstmtStudent.setString(7, student.getContact());
                pstmtStudent.setString(8, student.getAddress());
                pstmtStudent.setString(9, student.getCourse());
                pstmtStudent.setInt(10, student.getYear());

                pstmtStudent.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates an existing student's profile.
     */
    public static boolean updateStudent(Student student) {
        String sql = "UPDATE students SET roll_no = ?, first_name = ?, last_name = ?, dob = ?, " +
                "gender = ?, contact = ?, address = ?, course = ?, year = ? " +
                "WHERE student_id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getRollNo());
            pstmt.setString(2, student.getFirstName());
            pstmt.setString(3, student.getLastName());
            pstmt.setDate(4, student.getDob());
            pstmt.setString(5, student.getGender());
            pstmt.setString(6, student.getContact());
            pstmt.setString(7, student.getAddress());
            pstmt.setString(8, student.getCourse());
            pstmt.setInt(9, student.getYear());
            pstmt.setInt(10, student.getStudentId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * --- NEW METHOD ---
     * Inserts a new notice into the 'notices' table.
     */
    public static boolean postNewNotice(String title, String content, String postedBy, String targetSection) {
        String sql = "INSERT INTO notices (title, content, posted_by, target_section) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setString(3, postedBy);
            pstmt.setString(4, targetSection);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Add these two methods INSIDE the TeacherDAO class

    /**
     * --- NEW METHOD ---
     * Fetches all marks for a single student, identified by student_id.
     */
    public static java.util.List<Mark> getStudentMarks(int studentId) {
        java.util.List<Mark> marksList = new java.util.ArrayList<>();
        String sql = "SELECT course_code, marks_obtained, total_marks, grade FROM marks WHERE student_id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    marksList.add(new Mark(
                            rs.getString("course_code"),
                            rs.getInt("marks_obtained"),
                            rs.getInt("total_marks"),
                            rs.getString("grade")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return marksList;
    }

    /**
     * --- NEW METHOD ---
     * Adds a new mark or updates an existing one for a student.
     * It uses "ON DUPLICATE KEY UPDATE" to simplify the logic.
     * This requires a UNIQUE constraint on (student_id, course_code) in your DB.
     */
    public static boolean updateOrInsertMark(int studentId, String courseCode, int marksObtained, int totalMarks, String grade) {
        // This query tries to insert. If a row with the same unique key (student_id, course_code)
        // already exists, it performs an UPDATE instead.
        String sql = "INSERT INTO marks (student_id, course_code, marks_obtained, total_marks, grade) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "marks_obtained = VALUES(marks_obtained), " +
                "total_marks = VALUES(total_marks), " +
                "grade = VALUES(grade)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setString(2, courseCode);
            pstmt.setInt(3, marksObtained);
            pstmt.setInt(4, totalMarks);
            pstmt.setString(5, grade);

            int rowsAffected = pstmt.executeUpdate();
            // In this specific SQL, 1 = new row inserted, 2 = existing row updated
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Add this method INSIDE the TeacherDAO class

    /**
     * --- NEW METHOD ---
     * Saves a batch of attendance records.
     * It uses "ON DUPLICATE KEY UPDATE" to either insert a new record
     * or update an existing one if it already exists.
     * This relies on the unique key (student_id, course_code, date).
     */
    public static boolean saveAttendance(java.util.List<Attendance> attendanceList, String courseCode, java.sql.Date date) {
        String sql = "INSERT INTO attendance (student_id, course_code, date, status) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE status = VALUES(status)";

        Connection conn = null;
        try {
            conn = DBManager.getConnection();
            // Start transaction
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (Attendance record : attendanceList) {
                    pstmt.setInt(1, record.getStudentId()); // Need to add student_id to Attendance model
                    pstmt.setString(2, courseCode);
                    pstmt.setDate(3, date);
                    pstmt.setString(4, record.getStatus());

                    pstmt.addBatch(); // Add this operation to the batch
                }

                pstmt.executeBatch(); // Execute all operations at once
            }

            conn.commit(); // Commit the transaction
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // Rollback if any error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * --- NEW METHOD ---
     * Resets a user's password.
     * Hashes the new password and updates the 'users' table.
     */
    public static boolean resetUserPassword(int userId, String newPassword) {
        String hashedPassword = PasswordUtil.hashPassword(newPassword);
        String sql = "UPDATE users SET password_hash = ? WHERE user_id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, hashedPassword);
            pstmt.setInt(2, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}




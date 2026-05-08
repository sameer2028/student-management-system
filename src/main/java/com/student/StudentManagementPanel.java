package com.student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentManagementPanel extends JPanel {

    // --- Define consistent colors and fonts (same as StudentDashboard) ---
    private final Color COLOR_BACKGROUND = new Color(245, 248, 250);
    private final Color COLOR_HEADER = new Color(220, 230, 240);
    private final Color COLOR_TEXT = new Color(50, 50, 50);
    private final Font FONT_HEADER = new Font("Arial", Font.BOLD, 16);
    private final Font FONT_TEXT = new Font("Arial", Font.PLAIN, 14);
    private final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 12);

    private JTable studentTable;
    private DefaultTableModel tableModel;

    public StudentManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Create the top panel with buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(COLOR_BACKGROUND);

        JButton addButton = new JButton("Add New Student");
        styleButton(addButton, new Color(0, 128, 0)); // Green

        JButton editButton = new JButton("Edit Selected");
        styleButton(editButton, new Color(0, 100, 180)); // Blue

        JButton deleteButton = new JButton("Delete Selected");
        styleButton(deleteButton, new Color(192, 0, 0)); // Red

        // --- NEW: Reset Password Button ---
        JButton resetButton = new JButton("Reset Password");
        styleButton(resetButton, new Color(108, 117, 125)); // Gray

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(resetButton); // Add the new button

        add(buttonPanel, BorderLayout.NORTH);

        // 2. Create the table to show students
        String[] columnNames = {"Student ID", "User ID", "Roll No", "First Name", "Last Name", "Course", "Year"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        studentTable = new JTable(tableModel);
        styleTable(studentTable);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
        add(scrollPane, BorderLayout.CENTER);

        // 3. Load initial data
        refreshStudentTable();

        // 4. Add Action Listeners

        // --- ADD NEW STUDENT LOGIC ---
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StudentFormDialog dialog = new StudentFormDialog((Frame) SwingUtilities.getWindowAncestor(StudentManagementPanel.this));
                dialog.setVisible(true);

                if (dialog.isSaved()) {
                    Student newStudent = dialog.getStudent();
                    String username = dialog.getUsername();
                    String email = dialog.getEmail();
                    String password = dialog.getPassword();

                    boolean success = TeacherDAO.addNewStudent(newStudent, username, email, password);
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Student added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        refreshStudentTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to add student. Check username/email uniqueness.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // --- EDIT STUDENT LOGIC ---
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a student from the table to edit.", "No Student Selected", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int userId = (int) tableModel.getValueAt(selectedRow, 1);

                // Fetch FULL student details to pre-fill the form
                Student studentToEdit = StudentDAO.getStudentByUserId(userId);

                if (studentToEdit == null) {
                    JOptionPane.showMessageDialog(null, "Error: Could not retrieve student details.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                StudentFormDialog dialog = new StudentFormDialog((Frame) SwingUtilities.getWindowAncestor(StudentManagementPanel.this), studentToEdit);
                dialog.setVisible(true);

                if (dialog.isSaved()) {
                    Student updatedStudent = dialog.getStudent();

                    boolean success = TeacherDAO.updateStudent(updatedStudent);
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Student updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        refreshStudentTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to update student.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // --- DELETE BUTTON LOGIC ---
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a student from the table to delete.", "No Student Selected", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int userId = (int) tableModel.getValueAt(selectedRow, 1);
                String studentName = tableModel.getValueAt(selectedRow, 3) + " " + tableModel.getValueAt(selectedRow, 4);

                int confirm = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete this student?\n\n" + studentName + " (User ID: " + userId + ")\n\nThis action cannot be undone.",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.ERROR_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = TeacherDAO.deleteStudentByUserId(userId);
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Student deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        refreshStudentTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to delete student.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // --- NEW: Reset Password Button Logic ---
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a student from the table.", "No Student Selected", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Get user_id and name from the table
                int userId = (int) tableModel.getValueAt(selectedRow, 1);
                String studentName = tableModel.getValueAt(selectedRow, 3) + " " + tableModel.getValueAt(selectedRow, 4);

                // Ask the teacher for a new temporary password
                String newPassword = JOptionPane.showInputDialog(
                        StudentManagementPanel.this,
                        "Enter a new temporary password for " + studentName + ":",
                        "Reset Password",
                        JOptionPane.PLAIN_MESSAGE
                );

                // Check if the teacher clicked "Cancel" or entered an empty password
                if (newPassword != null && !newPassword.trim().isEmpty()) {

                    // Call the DAO to reset the password
                    boolean success = TeacherDAO.resetUserPassword(userId, newPassword.trim());

                    if (success) {
                        JOptionPane.showMessageDialog(null, "Password for " + studentName + " has been reset successfully.", "Password Reset", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to reset password. A database error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (newPassword != null) {
                    // Teacher entered an empty string
                    JOptionPane.showMessageDialog(null, "Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                // If newPassword is null (teacher clicked Cancel), do nothing.
            }
        });
    }

    /**
     * Fetches all students from the DAO and repopulates the JTable
     */
    private void refreshStudentTable() {
        tableModel.setRowCount(0);

        List<Student> students = TeacherDAO.getAllStudents();
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                    s.getStudentId(),
                    s.getUserId(),
                    s.getRollNo(),
                    s.getFirstName(),
                    s.getLastName(),
                    s.getCourse(),
                    s.getYear()
            });
        }
    }

    // Helper method to style buttons
    private void styleButton(JButton button, Color color) {
        button.setFont(FONT_BUTTON);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Helper method to style the JTable
    private void styleTable(JTable table) {
        table.setFont(FONT_TEXT);
        table.setForeground(COLOR_TEXT);
        table.setRowHeight(28);
        table.setGridColor(COLOR_HEADER);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_HEADER);
        header.setBackground(COLOR_HEADER);
        header.setForeground(COLOR_TEXT);
        header.setReorderingAllowed(false);
    }
}


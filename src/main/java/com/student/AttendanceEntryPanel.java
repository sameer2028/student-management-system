package com.student;

import javax.swing.*;
// import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class AttendanceEntryPanel extends JPanel {

    // --- Define consistent colors and fonts ---
    private final Color COLOR_BACKGROUND = new Color(245, 248, 250);
    private final Color COLOR_HEADER = new Color(220, 230, 240);
    private final Color COLOR_TEXT = new Color(50, 50, 50);
    private final Font FONT_HEADER = new Font("Arial", Font.BOLD, 16);
    private final Font FONT_LABEL = new Font("Arial", Font.BOLD, 14);
    private final Font FONT_TEXT = new Font("Arial", Font.PLAIN, 14);

    // --- UI Components ---
    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    private JTextField txtDate;
    private JComboBox<String> cmbCourse;
    private JButton btnLoadRoster;
    private JButton btnSaveAttendance;

    private List<Student> studentRoster; // Holds the list of loaded students

    public AttendanceEntryPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- 1. TOP Panel: Selection (Date and Course) ---
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        selectionPanel.setBackground(COLOR_BACKGROUND);

        selectionPanel.add(new JLabel("Date (YYYY-MM-DD):") {{ setFont(FONT_LABEL); }});
        txtDate = new JTextField(10);
        txtDate.setFont(FONT_TEXT);
        // Set today's date as default
        txtDate.setText(new Date(System.currentTimeMillis()).toString());
        selectionPanel.add(txtDate);

        selectionPanel.add(new JLabel("Course Code:") {{ setFont(FONT_LABEL); }});
        // We hard-code these for now. A better app would load them from a 'courses' table.
        String[] courses = {"CS101", "MA101", "BCS-21", "BCS-211", "BCS-212", "BCS-213", "BCS-214", "AUC-101"};
        cmbCourse = new JComboBox<>(courses);
        cmbCourse.setFont(FONT_TEXT);
        selectionPanel.add(cmbCourse);

        btnLoadRoster = new JButton("Load Class Roster");
        styleButton(btnLoadRoster, new Color(0, 100, 180)); // Blue
        selectionPanel.add(btnLoadRoster);

        add(selectionPanel, BorderLayout.NORTH);

        // --- 2. CENTER Panel: Attendance Table ---
        String[] columnNames = {"Student ID", "Roll No", "Name", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        attendanceTable = new JTable(tableModel);
        styleTable(attendanceTable);

        // --- This is the important part: Make the 'Status' column a dropdown ---
        String[] statuses = {"PRESENT", "ABSENT", "LATE"};
        JComboBox<String> statusComboBox = new JComboBox<>(statuses);
        TableColumn statusColumn = attendanceTable.getColumnModel().getColumn(3); // Column 3 is "Status"
        statusColumn.setCellEditor(new DefaultCellEditor(statusComboBox));
        // --------------------------------------------------------------------

        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
        add(scrollPane, BorderLayout.CENTER);

        // --- 3. BOTTOM Panel: Save Button ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        bottomPanel.setBackground(COLOR_BACKGROUND);
        btnSaveAttendance = new JButton("Save Attendance");
        styleButton(btnSaveAttendance, new Color(0, 128, 0)); // Green
        btnSaveAttendance.setFont(new Font("Arial", Font.BOLD, 16));
        btnSaveAttendance.setPreferredSize(new Dimension(300, 40));
        btnSaveAttendance.setEnabled(false); // Disabled until roster is loaded
        bottomPanel.add(btnSaveAttendance);

        add(bottomPanel, BorderLayout.SOUTH);

        // --- 4. Action Listeners ---
        btnLoadRoster.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRoster();
            }
        });

        btnSaveAttendance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAttendance();
            }
        });
    }

    /**
     * Loads all students into the table.
     * (A real app would filter by section/course)
     */
    private void loadRoster() {
        tableModel.setRowCount(0); // Clear table
        studentRoster = TeacherDAO.getAllStudents(); // Get all students

        for (Student s : studentRoster) {
            tableModel.addRow(new Object[]{
                    s.getStudentId(),
                    s.getRollNo(),
                    s.getFirstName() + " " + s.getLastName(),
                    "PRESENT" // Default to PRESENT
            });
        }
        btnSaveAttendance.setEnabled(true); // Enable the save button
    }

    /**
     * Reads all data from the table and saves it to the database.
     */
    private void saveAttendance() {
        // 1. Get date and course
        Date date;
        try {
            date = Date.valueOf(txtDate.getText());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String courseCode = (String) cmbCourse.getSelectedItem();

        // 2. Stop cell editing to ensure the last change is captured
        if (attendanceTable.isEditing()) {
            attendanceTable.getCellEditor().stopCellEditing();
        }

        // 3. Create a list of Attendance objects from the table
        List<Attendance> attendanceList = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int studentId = (int) tableModel.getValueAt(i, 0); // Col 0 is student_id
            String status = (String) tableModel.getValueAt(i, 3); // Col 3 is status

            attendanceList.add(new Attendance(studentId, courseCode, date, status));
        }

        // 4. Call the DAO
        boolean success = TeacherDAO.saveAttendance(attendanceList, courseCode, date);

        if (success) {
            JOptionPane.showMessageDialog(this, "Attendance saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save attendance. A database error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper method to style buttons
    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD, 12));
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

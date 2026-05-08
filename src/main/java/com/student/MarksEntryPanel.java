package com.student;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MarksEntryPanel extends JPanel {

    // --- Define consistent colors and fonts ---
    private final Color COLOR_BACKGROUND = new Color(245, 248, 250);
    private final Color COLOR_HEADER = new Color(220, 230, 240);
    private final Color COLOR_TEXT = new Color(50, 50, 50);
    private final Font FONT_HEADER = new Font("Arial", Font.BOLD, 16);
    private final Font FONT_LABEL = new Font("Arial", Font.BOLD, 14);
    private final Font FONT_TEXT = new Font("Arial", Font.PLAIN, 14);

    // --- UI Components ---
    private JList<Student> studentList;
    private DefaultListModel<Student> studentListModel;
    private JTable marksTable;
    private DefaultTableModel marksTableModel;
    private JLabel lblSelectedStudent;

    // --- Form Fields ---
    private JTextField txtCourseCode;
    private JTextField txtMarksObtained;
    private JTextField txtTotalMarks;
    private JTextField txtGrade;
    private JButton btnSaveMark;

    // --- State ---
    private Student selectedStudent = null;

    public MarksEntryPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- 1. WEST Panel: Student List ---
        studentListModel = new DefaultListModel<>();
        studentList = new JList<>(studentListModel);
        studentList.setCellRenderer(new StudentListRenderer()); // Custom renderer
        studentList.setFont(FONT_TEXT);
        loadStudentList(); // Load data from DAO

        JScrollPane listScrollPane = new JScrollPane(studentList);
        listScrollPane.setPreferredSize(new Dimension(250, 0));
        listScrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Select a Student",
                TitledBorder.LEFT, TitledBorder.TOP, FONT_HEADER, COLOR_TEXT
        ));
        add(listScrollPane, BorderLayout.WEST);

        // --- 2. CENTER Panel: Marks Editor ---
        JPanel editorPanel = new JPanel(new BorderLayout(10, 10));
        editorPanel.setBackground(COLOR_BACKGROUND);

        // Label for the selected student
        lblSelectedStudent = new JLabel("Please select a student from the list.");
        lblSelectedStudent.setFont(FONT_HEADER);
        lblSelectedStudent.setHorizontalAlignment(SwingConstants.CENTER);
        editorPanel.add(lblSelectedStudent, BorderLayout.NORTH);

        // Table for marks
        String[] columnNames = {"Course Code", "Marks Obtained", "Total Marks", "Grade"};
        marksTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        marksTable = new JTable(marksTableModel);
        styleTable(marksTable);

        JScrollPane tableScrollPane = new JScrollPane(marksTable);
        tableScrollPane.setBorder(BorderFactory.createEtchedBorder());
        editorPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Form for adding/updating marks
        editorPanel.add(createMarksEntryForm(), BorderLayout.SOUTH);

        add(editorPanel, BorderLayout.CENTER);

        // --- 3. Event Listener for Student List ---
        studentList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    selectedStudent = studentList.getSelectedValue();
                    if (selectedStudent != null) {
                        lblSelectedStudent.setText("Showing Marks for: " + selectedStudent.getFirstName() + " " + selectedStudent.getLastName());
                        refreshMarksTable();
                    }
                }
            }
        });

        // --- 4. Event Listener for Save Button ---
        btnSaveMark.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveMark();
            }
        });

        // --- 5. Event Listener for Table Row Click (to fill form) ---
        marksTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && marksTable.getSelectedRow() != -1) {
                    fillFormFromTable(marksTable.getSelectedRow());
                }
            }
        });
    }

    /**
     * Creates the small form panel at the bottom for entry.
     */
    private JPanel createMarksEntryForm() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(COLOR_BACKGROUND);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Add / Update Mark",
                TitledBorder.LEFT, TitledBorder.TOP, FONT_HEADER, COLOR_TEXT
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(new JLabel("Course Code:") {{ setFont(FONT_LABEL); }}, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        txtCourseCode = new JTextField(10);
        formPanel.add(txtCourseCode, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(new JLabel("Marks Obtained:") {{ setFont(FONT_LABEL); }}, gbc);

        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1;
        txtMarksObtained = new JTextField(5);
        formPanel.add(txtMarksObtained, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Total Marks:") {{ setFont(FONT_LABEL); }}, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        txtTotalMarks = new JTextField(5);
        formPanel.add(txtTotalMarks, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Grade:") {{ setFont(FONT_LABEL); }}, gbc);

        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1;
        txtGrade = new JTextField(3);
        formPanel.add(txtGrade, gbc);

        // Row 3 - Button
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        btnSaveMark = new JButton("Save / Update Mark");
        btnSaveMark.setBackground(new Color(0, 128, 0));
        btnSaveMark.setForeground(Color.WHITE);
        btnSaveMark.setFont(FONT_LABEL);
        formPanel.add(btnSaveMark, gbc);

        return formPanel;
    }

    /**
     * Loads the list of students from the database into the JList.
     */
    private void loadStudentList() {
        studentListModel.clear();
        List<Student> students = TeacherDAO.getAllStudents();
        for (Student s : students) {
            studentListModel.addElement(s);
        }
    }

    /**
     * Refreshes the marks table for the currently selected student.
     */
    private void refreshMarksTable() {
        marksTableModel.setRowCount(0); // Clear table

        if (selectedStudent == null) return;

        List<Mark> marks = TeacherDAO.getStudentMarks(selectedStudent.getStudentId());
        for (Mark m : marks) {
            marksTableModel.addRow(new Object[]{
                    m.getCourseCode(),
                    m.getMarksObtained(),
                    m.getTotalMarks(),
                    m.getGrade()
            });
        }
    }

    /**
     * Fills the entry form when a user clicks a row in the marks table.
     */
    private void fillFormFromTable(int row) {
        txtCourseCode.setText((String) marksTableModel.getValueAt(row, 0));
        txtMarksObtained.setText(String.valueOf(marksTableModel.getValueAt(row, 1)));
        txtTotalMarks.setText(String.valueOf(marksTableModel.getValueAt(row, 2)));
        txtGrade.setText((String) marksTableModel.getValueAt(row, 3));
    }

    /**
     * Handles the logic for saving or updating a mark.
     */
    private void saveMark() {
        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(this, "Please select a student first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 1. Get data from form
        String courseCode = txtCourseCode.getText();
        String marksObtStr = txtMarksObtained.getText();
        String totalMarksStr = txtTotalMarks.getText();
        String grade = txtGrade.getText();

        // 2. Validate input
        if (courseCode.isEmpty() || marksObtStr.isEmpty() || totalMarksStr.isEmpty() || grade.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int marksObtained = Integer.parseInt(marksObtStr);
            int totalMarks = Integer.parseInt(totalMarksStr);
            int studentId = selectedStudent.getStudentId();

            // 3. Call the DAO
            boolean success = TeacherDAO.updateOrInsertMark(studentId, courseCode, marksObtained, totalMarks, grade);

            // 4. Show feedback
            if (success) {
                JOptionPane.showMessageDialog(this, "Mark saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshMarksTable(); // Refresh the table
                // Clear the form
                txtCourseCode.setText("");
                txtMarksObtained.setText("");
                txtTotalMarks.setText("");
                txtGrade.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save mark.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Marks Obtained and Total Marks must be valid numbers.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Custom renderer to show student names nicely in the JList.
     */
    private class StudentListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            // Get the default component
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Student) {
                Student s = (Student) value;
                // Display as "FirstName LastName (RollNo)"
                setText(s.getFirstName() + " " + s.getLastName() + " (" + s.getRollNo() + ")");
            }

            if (isSelected) {
                setBackground(new Color(0, 120, 215)); // Selection blue
                setForeground(Color.WHITE);
            } else {
                setBackground(Color.WHITE);
            }
            return c;
        }
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

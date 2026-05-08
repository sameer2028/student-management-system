package com.student;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.TitledBorder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent; // Added for logout
import java.awt.event.ActionListener; // Added for logout
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;
// import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.net.URL;

public class StudentDashboardUI {

    private Student student;

    // --- Define consistent colors and fonts for an attractive UI ---
    private final Color COLOR_BACKGROUND = new Color(245, 248, 250); // Very light blue-gray
    private final Color COLOR_HEADER = new Color(220, 230, 240); // Light blue
    private final Color COLOR_TEXT = new Color(50, 50, 50); // Dark gray
    private final Color COLOR_PROFILE_LABEL = new Color(0, 100, 180); // Blue for profile labels
    private final Color COLOR_LOGOUT_BUTTON = new Color(192, 0, 0); // Red for logout

    private final Font FONT_TITLE = new Font("Arial", Font.BOLD, 22);
    private final Font FONT_HEADER = new Font("Arial", Font.BOLD, 16);
    private final Font FONT_LABEL = new Font("Arial", Font.BOLD, 14);
    private final Font FONT_TEXT = new Font("Arial", Font.PLAIN, 14);
    private final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 12);

    public StudentDashboardUI(int userId) {
        // 1. Fetch the student's data
        this.student = StudentDAO.getStudentByUserId(userId);

        if (this.student == null) {
            JOptionPane.showMessageDialog(null, "Could not load student data.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Create the main frame
        JFrame frame = new JFrame("Student Dashboard - Welcome " + student.getFirstName());
        frame.setSize(900, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the window
        frame.getContentPane().setBackground(COLOR_BACKGROUND);
        frame.setLayout(new BorderLayout()); // Use BorderLayout

        // 3. Create the tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.setBackground(COLOR_HEADER);
        tabbedPane.setForeground(COLOR_TEXT);

        // 4. Create all the tabs
        tabbedPane.addTab("  My Profile  ", createProfilePanel());
        tabbedPane.addTab("  Attendance  ", createAttendancePanel());
        tabbedPane.addTab(" Marks & CGPA ", createMarksPanel());
        tabbedPane.addTab("  Timetable  ", createTimetablePanel());
        tabbedPane.addTab("    Fees    ", createFeesPanel());
        tabbedPane.addTab("   Notices   ", createNoticesPanel());
        tabbedPane.addTab("Extracurriculars", createExtracurricularsPanel());

        // --- 5. NEW: Create Header Panel with Logout Button ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_HEADER);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel welcomeLabel = new JLabel("Welcome, " + student.getFirstName());
        welcomeLabel.setFont(FONT_HEADER);
        welcomeLabel.setForeground(COLOR_TEXT);

        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton, COLOR_LOGOUT_BUTTON);

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        // --- 6. Add components to the frame ---
        frame.add(headerPanel, BorderLayout.NORTH); // Header at the top
        frame.add(tabbedPane, BorderLayout.CENTER); // Tabs in the center
        frame.setVisible(true);

        // --- 7. NEW: Logout Button Action ---
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        frame,
                        "Are you sure you want to logout?",
                        "Logout Confirmation",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    frame.dispose(); // Close this dashboard
                    // Open a new Login window
                    SwingUtilities.invokeLater(() -> new LoginUI());
                }
            }
        });
    }

    // Helper method to style buttons (created for logout)
    private void styleButton(JButton button, Color color) {
        button.setFont(FONT_BUTTON);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Helper method to style a JTable
     */
    private JTable styleTable(JTable table) {
        table.setFont(FONT_TEXT);
        table.setForeground(COLOR_TEXT);
        table.setRowHeight(28);
        table.setGridColor(COLOR_HEADER);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_HEADER);
        header.setBackground(COLOR_HEADER);
        header.setForeground(COLOR_TEXT);
        header.setReorderingAllowed(false); // Don't let user drag columns

        return table;
    }

    /**
     * Helper method to create a standard title label for each tab
     */
    private JLabel createTabTitle(String title) {
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(COLOR_TEXT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        return titleLabel;
    }

    /**
     * Creates the panel for the 'My Profile' tab
     */
    private JPanel createProfilePanel() {
        // Main panel with a title at the top
        JPanel mainPanel = new JPanel(new BorderLayout(20, 0)); // Added horizontal gap for photo
        mainPanel.setBackground(COLOR_BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        mainPanel.add(createTabTitle("Student Profile"), BorderLayout.NORTH);

        // Panel to hold the profile details (left/center)
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(COLOR_BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Add labels and data fields
        addField(detailsPanel, gbc, 0, "Full Name:", student.getFirstName() + " " + student.getLastName());
        addField(detailsPanel, gbc, 1, "Roll Number:", student.getRollNo());
        addField(detailsPanel, gbc, 2, "Course:", student.getCourse());
        addField(detailsPanel, gbc, 3, "Year:", String.valueOf(student.getYear()));
        addField(detailsPanel, gbc, 4, "Date of Birth:", (student.getDob() != null) ? student.getDob().toString() : "N/A");
        addField(detailsPanel, gbc, 5, "Gender:", (student.getGender() != null) ? student.getGender() : "N/A");
        addField(detailsPanel, gbc, 6, "Contact:", (student.getContact() != null) ? student.getContact() : "N/A");

        // Special handling for Address to allow wrapping
        gbc.gridx = 0;
        gbc.gridy = 7;
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(FONT_LABEL);
        addressLabel.setForeground(COLOR_PROFILE_LABEL);
        detailsPanel.add(addressLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        // Use HTML in a JLabel for clean, read-only wrapping
        String addressText = "<html><div style='width: 300px;'>" +
                ((student.getAddress() != null) ? student.getAddress() : "N/A") +
                "</div></html>";
        JLabel addressValue = new JLabel(addressText);
        addressValue.setFont(FONT_TEXT);
        addressValue.setForeground(COLOR_TEXT);
        detailsPanel.add(addressValue, gbc);

        // Add a "filler" panel to push everything to the top
        gbc.gridy = 8;
        gbc.weighty = 1.0;
        detailsPanel.add(new JPanel() {{ setBackground(COLOR_BACKGROUND); }}, gbc);

        mainPanel.add(detailsPanel, BorderLayout.CENTER); // Profile details in the center

        // --- Add Photo Panel to the EAST (right) ---
        mainPanel.add(createPhotoPanel(), BorderLayout.EAST);

        return mainPanel;
    }

    // Updated helper to use JLabels for a cleaner, read-only look
    private void addField(JPanel panel, GridBagConstraints gbc, int y, String labelText, String valueText) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel label = new JLabel(labelText);
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_PROFILE_LABEL); // Use distinct label color
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JLabel value = new JLabel(valueText);
        value.setFont(FONT_TEXT);
        value.setForeground(COLOR_TEXT);
        panel.add(value, gbc);
    }

    /**
     * Creates a panel to display a dummy photo at the top right.
     */
    private JPanel createPhotoPanel() {
        JPanel photoPanel = new JPanel(new BorderLayout());
        photoPanel.setBackground(COLOR_BACKGROUND);
        photoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // Padding on right

        JLabel photoLabel = new JLabel();
        photoLabel.setPreferredSize(new Dimension(150, 150)); // Fixed size for the photo
        photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        photoLabel.setVerticalAlignment(SwingConstants.CENTER);
        photoLabel.setBorder(BorderFactory.createLineBorder(COLOR_HEADER, 2)); // Simple border

        // Load image from resources
        URL imageUrl = getClass().getClassLoader().getResource("images/profile_placeholder.png");

        if (imageUrl != null) {
            ImageIcon originalIcon = new ImageIcon(imageUrl);
            Image image = originalIcon.getImage();
            Image resizedImage = image.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
            photoLabel.setIcon(new ImageIcon(resizedImage));
        } else {
            photoLabel.setText("No Photo");
            photoLabel.setFont(FONT_TEXT);
            photoLabel.setForeground(COLOR_TEXT);
        }

        photoPanel.add(photoLabel, BorderLayout.NORTH);
        photoPanel.add(new JPanel() {{ setBackground(COLOR_BACKGROUND); }}, BorderLayout.CENTER);
        return photoPanel;
    }

    /**
     * Creates the panel for the 'Attendance' tab
     */
    private JPanel createAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(createTabTitle("Attendance Record"), BorderLayout.NORTH);

        List<Attendance> attendanceList = StudentDAO.getAttendanceByStudentId(student.getStudentId());
        String[] columnNames = {"Course Code", "Date", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Attendance record : attendanceList) {
            model.addRow(new Object[]{
                    record.getCourseCode(),
                    record.getDate().toString(),
                    record.getStatus()
            });
        }

        JTable table = styleTable(new JTable(model));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Creates the panel for the 'Marks & CGPA' tab
     */
    private JPanel createMarksPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(createTabTitle("Marks & CGPA"), BorderLayout.NORTH);

        List<Mark> marksList = StudentDAO.getMarksByStudentId(student.getStudentId());
        String[] columnNames = {"Course Code", "Marks Obtained", "Total Marks", "Grade"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        double totalMarksObtained = 0;
        double totalMaxMarks = 0;

        for (Mark record : marksList) {
            model.addRow(new Object[]{
                    record.getCourseCode(),
                    record.getMarksObtained(),
                    record.getTotalMarks(),
                    record.getGrade()
            });
            totalMarksObtained += record.getMarksObtained();
            totalMaxMarks += record.getTotalMarks();
        }

        JTable table = styleTable(new JTable(model));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        double overallPercentage = 0;
        if (totalMaxMarks > 0) {
            overallPercentage = (totalMarksObtained / totalMaxMarks) * 100;
        }
        double cgpa = (overallPercentage / 9.5);

        String cgpaText = String.format(
                "<html><div style='padding: 10px;'>" +
                        "<b>Total Marks Obtained:</b> %.0f / %.0f<br>" +
                        "<b>Overall Percentage:</b> %.2f%%<br>" +
                        "<hr><b style='font-size:14px;'>Calculated CGPA:</b> <b style='font-size:16px; color:blue;'>%.2f</b>" +
                        "</div></html>",
                totalMarksObtained, totalMaxMarks, overallPercentage, cgpa
        );
        JLabel cgpaLabel = new JLabel(cgpaText);
        cgpaLabel.setFont(FONT_TEXT);
        cgpaLabel.setForeground(COLOR_TEXT);
        cgpaLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cgpaLabel.setBorder(BorderFactory.createEtchedBorder());

        panel.add(cgpaLabel, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Creates the panel for the 'Timetable' tab
     */
    private JPanel createTimetablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(createTabTitle("Class Timetable (Section D)"), BorderLayout.NORTH);

        String[] columnNames = {
                "Day/Period", "I (9.30-10.20)", "II (10.20-11.10)", "III (11.10-12.00)",
                "IV (12.00-12.50)", "LUNCH", "V (2.20-3.10)",
                "VI (3.10-4.00)", "VII (4.00-4.50)"
        };
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        Map<String, Integer> periodToCol = new HashMap<>();
        for (int i = 1; i < columnNames.length; i++) {
            periodToCol.put(columnNames[i], i);
        }
        Map<String, Integer> dayToRow = new HashMap<>();
        for (int i = 0; i < days.length; i++) {
            dayToRow.put(days[i], i);
        }

        Object[][] data = new Object[days.length][columnNames.length];
        for (int r = 0; r < days.length; r++) {
            data[r][0] = days[r];
            for (int c = 1; c < columnNames.length; c++) {
                data[r][c] = "---";
            }
            data[r][periodToCol.get("LUNCH")] = "LUNCH";
        }

        List<TimetableEntry> entries = StudentDAO.getTimetableBySection("D");

        for (TimetableEntry entry : entries) {
            Integer row = dayToRow.get(entry.getDayOfWeek());
            Integer col = periodToCol.get(entry.getPeriod());
            if (row != null && col != null) {
                data[row][col] = "<html><center>" + entry.getCourseCode() + "<br>(" + entry.getFacultyCode() + ")</center></html>";
            }
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = styleTable(new JTable(model));
        table.setRowHeight(40);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the panel for the 'Fees' tab
     */
    private JPanel createFeesPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(createTabTitle("Fee Payment Status"), BorderLayout.NORTH);

        int currentSemester = 3;
        FeeSummary summary = StudentDAO.getFeeSummary(student.getStudentId(), currentSemester);

        if (summary == null) {
            panel.add(new JLabel("No fee information found for Semester " + currentSemester), BorderLayout.CENTER);
            return panel;
        }

        List<FeeDetail> details = StudentDAO.getFeeDetails(summary.getSummaryId());

        String[] columnNames = {"Fee Description", "Amount (INR)"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (FeeDetail detail : details) {
            model.addRow(new Object[]{detail.getDescription(), detail.getAmount()});
        }
        JTable table = styleTable(new JTable(model));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        summaryPanel.setBackground(COLOR_BACKGROUND);
        summaryPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Semester " + currentSemester + " Payment Summary",
                TitledBorder.LEFT, TitledBorder.TOP, FONT_HEADER, COLOR_TEXT
        ));
        summaryPanel.setFont(FONT_TEXT);

        BigDecimal balance = summary.getTotalDue().subtract(summary.getTotalPaid());

        summaryPanel.add(new JLabel("Total Due:") {{ setFont(FONT_LABEL); }});
        summaryPanel.add(new JLabel("₹ " + summary.getTotalDue().toString()) {{ setFont(FONT_TEXT); }});

        summaryPanel.add(new JLabel("Total Paid:") {{ setFont(FONT_LABEL); }});
        summaryPanel.add(new JLabel("₹ " + summary.getTotalPaid().toString()) {{ setFont(FONT_TEXT); }});

        summaryPanel.add(new JLabel("Status:") {{ setFont(FONT_LABEL); }});
        JLabel statusLabel = new JLabel(summary.getStatus());
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        if ("PAID".equals(summary.getStatus())) {
            statusLabel.setForeground(new Color(0, 128, 0));
        } else {
            statusLabel.setForeground(Color.RED);
        }
        summaryPanel.add(statusLabel);

        summaryPanel.add(new JLabel("Balance Due:") {{ setFont(FONT_LABEL); }});
        JLabel balanceLabel = new JLabel("₹ " + balance.toString());
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        balanceLabel.setForeground(Color.RED);
        summaryPanel.add(balanceLabel);

        panel.add(summaryPanel, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Creates the panel for the 'Notices' tab
     */
    private JPanel createNoticesPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(createTabTitle("College Notices"), BorderLayout.NORTH);

        JPanel noticesListPanel = new JPanel();
        noticesListPanel.setLayout(new BoxLayout(noticesListPanel, BoxLayout.Y_AXIS));
        noticesListPanel.setBackground(COLOR_BACKGROUND);

        List<Notice> notices = StudentDAO.getNotices("D");

        if (notices.isEmpty()) {
            panel.add(new JLabel("No notices found."), BorderLayout.CENTER);
            return panel;
        }

        for (Notice notice : notices) {
            JPanel noticePanel = new JPanel(new BorderLayout(5, 5));
            noticePanel.setBackground(Color.WHITE);
            noticePanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(0, 0, 10, 0),
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createEtchedBorder(),
                            BorderFactory.createEmptyBorder(10, 15, 10, 15)
                    )
            ));

            JLabel titleLabel = new JLabel(notice.getTitle());
            titleLabel.setFont(FONT_HEADER);
            titleLabel.setForeground(COLOR_PROFILE_LABEL);
            noticePanel.add(titleLabel, BorderLayout.NORTH);

            JTextArea contentArea = new JTextArea(notice.getContent());
            contentArea.setWrapStyleWord(true);
            contentArea.setLineWrap(true);
            contentArea.setEditable(false);
            contentArea.setBackground(Color.WHITE);
            contentArea.setFont(FONT_TEXT);
            contentArea.setForeground(COLOR_TEXT);
            noticePanel.add(contentArea, BorderLayout.CENTER);

            String footerText = String.format("Posted by: %s on %s",
                    notice.getPostedBy(),
                    new SimpleDateFormat("dd-MMM-yyyy 'at' h:mm a").format(notice.getPostedDate())
            );
            JLabel footerLabel = new JLabel(footerText);
            footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            footerLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
            noticePanel.add(footerLabel, BorderLayout.SOUTH);

            noticesListPanel.add(noticePanel);
        }

        noticesListPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(noticesListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(COLOR_BACKGROUND);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Creates the panel for the 'Extracurriculars' tab
     */
    private JPanel createExtracurricularsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(createTabTitle("Extracurricular Activities"), BorderLayout.NORTH);

        List<Extracurricular> activities = StudentDAO.getExtracurriculars(student.getStudentId());

        String[] columnNames = {"Academic Year", "Activity Name", "Role / Position"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Extracurricular activity : activities) {
            model.addRow(new Object[]{
                    activity.getAcademicYear(),
                    activity.getActivityName(),
                    activity.getRole()
            });
        }

        JTable table = styleTable(new JTable(model));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
}


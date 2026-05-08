package com.student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeacherDashboardUI {

    // --- Define consistent colors and fonts ---
    private final Color COLOR_BACKGROUND = new Color(245, 248, 250);
    private final Color COLOR_HEADER = new Color(220, 230, 240);
    private final Color COLOR_TEXT = new Color(50, 50, 50);
    private final Color COLOR_LOGOUT_BUTTON = new Color(192, 0, 0); // Red for logout
    private final Font FONT_HEADER = new Font("Arial", Font.BOLD, 16);
    private final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 12);

    public TeacherDashboardUI() {
        // 1. Create the main frame
        JFrame frame = new JFrame("Teacher Dashboard");
        frame.setSize(900, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(COLOR_BACKGROUND);
        frame.setLayout(new BorderLayout()); // Use BorderLayout

        // 2. Create the tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.setBackground(COLOR_HEADER);
        tabbedPane.setForeground(COLOR_TEXT);

        // 3. Add tabs
        tabbedPane.addTab("  Manage Students  ", new StudentManagementPanel());
        tabbedPane.addTab("   Enter Marks   ", new MarksEntryPanel());
        tabbedPane.addTab(" Post Attendance ", new AttendanceEntryPanel());
        tabbedPane.addTab("   Post Notice   ", new NoticePostingPanel());
        // --- "Manage Fees" tab has been removed ---

        // --- 4. Create Header Panel with Logout Button ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_HEADER);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel welcomeLabel = new JLabel("Teacher Control Panel");
        welcomeLabel.setFont(FONT_HEADER);
        welcomeLabel.setForeground(COLOR_TEXT);

        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton, COLOR_LOGOUT_BUTTON);

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        // --- 5. Add components to the frame ---
        frame.add(headerPanel, BorderLayout.NORTH); // Header at the top
        frame.add(tabbedPane, BorderLayout.CENTER); // Tabs in the center
        frame.setVisible(true);

        // --- 6. Logout Button Action ---
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
}


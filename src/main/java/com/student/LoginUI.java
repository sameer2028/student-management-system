package com.student;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class LoginUI {

    public LoginUI() {
        // --- Setup Colors and Fonts ---
        Color pageBackground = new Color(240, 245, 250); // Light blue-gray for the window
        Color cardBackground = Color.WHITE;              // White for the "card"
        Color buttonColor = new Color(0, 120, 215);     // Strong blue
        Color resetButtonColor = new Color(108, 117, 125); // Gray
        Color labelColor = new Color(50, 50, 50);       // Dark gray text

        Font titleFont = new Font("Arial", Font.BOLD, 26);
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        Font linkFont = new Font("Arial", Font.PLAIN, 12); // Font for the link

        // --- Create the main frame ---
        JFrame frame = new JFrame("Login");
        frame.setSize(500, 520); // Made the window slightly taller
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the window
        frame.setResizable(false);

        frame.getContentPane().setBackground(pageBackground);
        frame.getContentPane().setLayout(new GridBagLayout()); // This will center the cardPanel

        // --- Create the "Card" Panel (holds all content) ---
        JPanel cardPanel = new JPanel(new BorderLayout(10, 10));
        cardPanel.setBackground(cardBackground);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220)), // Soft gray line border
                BorderFactory.createEmptyBorder(30, 40, 30, 40)  // 30-40px padding
        ));

        // --- 1. Title Label (NORTH) ---
        JLabel titleLabel = new JLabel("Student Information System");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(labelColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0)); // Bottom padding
        cardPanel.add(titleLabel, BorderLayout.NORTH);

        // --- 2. Form Panel (CENTER) ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(cardBackground);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // Padding between components

        // --- Row 0: Role ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel roleLabel = new JLabel("Select Role:");
        roleLabel.setFont(labelFont);
        roleLabel.setForeground(labelColor);
        formPanel.add(roleLabel, gbc);

        // Radio Buttons
        JRadioButton studentRadio = new JRadioButton("Student");
        studentRadio.setFont(fieldFont);
        studentRadio.setBackground(cardBackground);
        studentRadio.setSelected(true);

        JRadioButton teacherRadio = new JRadioButton("Teacher");
        teacherRadio.setFont(fieldFont);
        teacherRadio.setBackground(cardBackground);

        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(studentRadio);
        roleGroup.add(teacherRadio);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        radioPanel.setBackground(cardBackground);
        radioPanel.add(studentRadio);
        radioPanel.add(teacherRadio);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(radioPanel, gbc);

        // --- Row 1: Username Row ---
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(labelFont);
        userLabel.setForeground(labelColor);
        formPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField userText = new JTextField(20);
        userText.setFont(fieldFont);
        formPanel.add(userText, gbc);

        // --- Row 2: Password Row ---
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(labelFont);
        passLabel.setForeground(labelColor);
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JPasswordField passText = new JPasswordField(20);
        passText.setFont(fieldFont);
        formPanel.add(passText, gbc);

        // --- Row 3: Login & Reset Buttons ---
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(15, 8, 8, 8); // More top padding for buttons
        gbc.ipady = 10; // Make buttons taller

        JButton loginButton = new JButton("Login");
        loginButton.setFont(buttonFont);
        loginButton.setBackground(buttonColor);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton resetButton = new JButton("Reset");
        resetButton.setFont(buttonFont);
        resetButton.setBackground(resetButtonColor);
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusPainted(false);
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(cardBackground);
        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);

        formPanel.add(buttonPanel, gbc);

        // --- Row 4: Forgot Password Link ---
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 8, 0, 8); // Top padding
        gbc.ipady = 0; // Reset padding

        JButton forgotPasswordButton = new JButton("Forgot your password?");
        forgotPasswordButton.setFont(linkFont);
        forgotPasswordButton.setForeground(buttonColor);
        forgotPasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Style as a link (no border, no background)
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setContentAreaFilled(false);
        forgotPasswordButton.setFocusPainted(false);

        formPanel.add(forgotPasswordButton, gbc);

        // --- Add Form to Card ---
        cardPanel.add(formPanel, BorderLayout.CENTER);

        // --- Add Card to Frame ---
        frame.add(cardPanel, new GridBagConstraints()); // This centers the cardPanel

        // --- Action Listeners ---

        // Login Button Listener
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // GET ROLE FROM RADIO BUTTONS
                String selectedRole;
                if (teacherRadio.isSelected()) {
                    selectedRole = "TEACHER";
                } else {
                    selectedRole = "STUDENT";
                }

                String username = userText.getText();
                String password = new String(passText.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Username and password cannot be empty.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (Connection conn = DBManager.getConnection()) {
                    String sql = "SELECT password_hash, role, user_id FROM users WHERE username = ? AND role = ?";

                    try (java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, username);
                        pstmt.setString(2, selectedRole);

                        try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                            if (rs.next()) {
                                String storedHash = rs.getString("password_hash");

                                if (PasswordUtil.checkPassword(password, storedHash)) {
                                    String role = rs.getString("role");
                                    int userId = rs.getInt("user_id");

                                    if (role.equals("STUDENT")) {
                                        SwingUtilities.invokeLater(() -> new StudentDashboardUI(userId));
                                        frame.dispose();
                                    } else if (role.equals("TEACHER")) {
                                        SwingUtilities.invokeLater(() -> new TeacherDashboardUI());
                                        frame.dispose();
                                    } else {
                                        JOptionPane.showMessageDialog(frame, "Login Successful! Unknown Role: " + role, "Success", JOptionPane.INFORMATION_MESSAGE);
                                    }

                                } else {
                                    JOptionPane.showMessageDialog(frame, "Invalid username, password, or role.", "Login Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(frame, "Invalid username, password, or role.", "Login Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Database error. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // --- Reset Button Listener ---
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear text fields
                userText.setText("");
                passText.setText("");
                // Reset role selection to default (Student)
                studentRadio.setSelected(true);
            }
        });

        // --- Forgot Password Listener ---
        forgotPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Password reset cannot be done automatically in this system.\n\n" +
                                "Please contact your teacher to have your password reset.",
                        "Offline Password Reset",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }
}


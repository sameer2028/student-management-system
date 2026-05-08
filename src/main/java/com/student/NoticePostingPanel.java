package com.student;

import javax.swing.*;
// import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NoticePostingPanel extends JPanel {

    // --- Define consistent colors and fonts ---
    private final Color COLOR_BACKGROUND = new Color(245, 248, 250);
    private final Color COLOR_TEXT = new Color(50, 50, 50);
    // private final Color COLOR_HEADER = new Color(220, 230, 240);
    private final Color COLOR_BUTTON = new Color(0, 128, 0); // Green for "Post"

    private final Font FONT_TITLE = new Font("Arial", Font.BOLD, 22);
    private final Font FONT_LABEL = new Font("Arial", Font.BOLD, 14);
    private final Font FONT_FIELD = new Font("Arial", Font.PLAIN, 14);

    // --- Form Components ---
    private JTextField txtTitle;
    private JTextArea txtContent;
    private JComboBox<String> cmbTargetSection;
    private JButton btnPostNotice;

    public NoticePostingPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // --- 1. Title Label ---
        JLabel titleLabel = new JLabel("Post a New Notice");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(COLOR_TEXT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        // --- 2. Form Panel ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(COLOR_BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Row 1: Title ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel lblTitle = new JLabel("Notice Title:");
        lblTitle.setFont(FONT_LABEL);
        formPanel.add(lblTitle, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        txtTitle = new JTextField();
        txtTitle.setFont(FONT_FIELD);
        formPanel.add(txtTitle, gbc);

        // --- Row 2: Target Section ---
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel lblTarget = new JLabel("Target Section:");
        lblTarget.setFont(FONT_LABEL);
        formPanel.add(lblTarget, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        // In a real app, you might fetch these from the DB
        String[] sections = {"ALL", "A", "B", "C", "D"};
        cmbTargetSection = new JComboBox<>(sections);
        cmbTargetSection.setFont(FONT_FIELD);
        cmbTargetSection.setSelectedItem("D"); // Default to Section D
        formPanel.add(cmbTargetSection, gbc);

        // --- Row 3: Content ---
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST; // Align label to top
        JLabel lblContent = new JLabel("Content:");
        lblContent.setFont(FONT_LABEL);
        formPanel.add(lblContent, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // Give vertical space to content
        gbc.fill = GridBagConstraints.BOTH; // Fill both horizontally and vertically
        txtContent = new JTextArea();
        txtContent.setFont(FONT_FIELD);
        txtContent.setLineWrap(true);
        txtContent.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtContent);
        scrollPane.setPreferredSize(new Dimension(0, 200)); // Set preferred height
        formPanel.add(scrollPane, gbc);

        add(formPanel, BorderLayout.CENTER);

        // --- 3. Post Button ---
        btnPostNotice = new JButton("Post Notice");
        btnPostNotice.setFont(new Font("Arial", Font.BOLD, 16));
        btnPostNotice.setBackground(COLOR_BUTTON);
        btnPostNotice.setForeground(Color.WHITE);
        btnPostNotice.setFocusPainted(false);
        btnPostNotice.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPostNotice.setPreferredSize(new Dimension(0, 50)); // Make button taller

        add(btnPostNotice, BorderLayout.SOUTH);

        // --- 4. Action Listener ---
        btnPostNotice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                postNotice();
            }
        });
    }

    /**
     * Called when the "Post Notice" button is clicked.
     */
    private void postNotice() {
        // 1. Get data from form
        String title = txtTitle.getText();
        String content = txtContent.getText();
        String targetSection = (String) cmbTargetSection.getSelectedItem();
        // We'll hard-code the poster as "Teacher" for now
        String postedBy = "Teacher";

        // 2. Validate input
        if (title.isEmpty() || content.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Title and Content cannot be empty.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Call the DAO
        boolean success = TeacherDAO.postNewNotice(title, content, postedBy, targetSection);

        // 4. Show feedback
        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Notice posted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            // Clear the form
            txtTitle.setText("");
            txtContent.setText("");
            cmbTargetSection.setSelectedItem("ALL");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to post notice.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

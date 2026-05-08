package com.student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

/**
 * A pop-up dialog (JDialog) for adding a new student or editing an existing one.
 */
public class StudentFormDialog extends JDialog {

    // --- Fonts and Colors (consistent with dashboards) ---
    private final Color COLOR_BACKGROUND = new Color(245, 248, 250);
    // private final Color COLOR_TEXT = new Color(50, 50, 50);
    private final Font FONT_LABEL = new Font("Arial", Font.BOLD, 14);
    private final Font FONT_FIELD = new Font("Arial", Font.PLAIN, 14);

    // --- Form Fields ---
    // User Account fields (for new student)
    private JTextField txtUsername;
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    // Student Profile fields
    private JTextField txtRollNo;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtDob; // We use a simple text field for date for simplicity
    private JComboBox<String> cmbGender;
    private JTextField txtContact;
    private JTextArea txtAddress;
    private JTextField txtCourse;
    private JTextField txtYear;

    // --- Control Buttons ---
    private JButton btnSave;
    private JButton btnCancel;

    // --- State ---
    private boolean isSaved = false;
    private Student student; // Holds the student data being edited, or null for new

    /**
     * Constructor for "Add New Student" mode.
     * @param owner The frame that owns this dialog.
     */
    public StudentFormDialog(Frame owner) {
        super(owner, "Add New Student", true); // 'true' for modal
        this.student = null; // No existing student
        initComponents();
        setTitle("Add New Student");
    }

    /**
     * Constructor for "Edit Student" mode.
     * @param owner The frame that owns this dialog.
     * @param studentToEdit The student object to pre-fill the form with.
     */
    public StudentFormDialog(Frame owner, Student studentToEdit) {
        super(owner, "Edit Student", true);
        this.student = studentToEdit; // The student we are editing
        initComponents();
        setTitle("Edit Student - " + student.getFirstName());

        // Fill the form with the student's data
        fillForm();

        // For "Edit" mode, we lock the user account fields
        txtUsername.setEditable(false);
        txtEmail.setEditable(false);
        txtPassword.setEditable(false);
    }

    /**
     * Creates and lays out all the Swing components.
     */
    private void initComponents() {
        setSize(500, 700);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BACKGROUND);

        // --- Create the Form Panel ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(COLOR_BACKGROUND);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- User Account Section (for new students) ---
        addSectionTitle(formPanel, gbc, "User Account (for Login)");
        txtUsername = addFormField(formPanel, gbc, "Username:");
        txtEmail = addFormField(formPanel, gbc, "Email:");
        txtPassword = new JPasswordField();
        addField(formPanel, gbc, new JLabel("Password:") {{ setFont(FONT_LABEL); }}, txtPassword);

        // --- Student Profile Section ---
        addSectionTitle(formPanel, gbc, "Student Profile Details");
        txtRollNo = addFormField(formPanel, gbc, "Roll No:");
        txtFirstName = addFormField(formPanel, gbc, "First Name:");
        txtLastName = addFormField(formPanel, gbc, "Last Name:");
        txtDob = addFormField(formPanel, gbc, "Date of Birth (YYYY-MM-DD):");

        cmbGender = new JComboBox<>(new String[]{"M", "F", "O"});
        addField(formPanel, gbc, new JLabel("Gender:") {{ setFont(FONT_LABEL); }}, cmbGender);

        txtContact = addFormField(formPanel, gbc, "Contact:");
        txtCourse = addFormField(formPanel, gbc, "Course (e.g., B.Tech):");
        txtYear = addFormField(formPanel, gbc, "Year (e.g., 2024-2028):");

        // Address (JTextArea needs special handling)
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setFont(FONT_LABEL);
        formPanel.add(lblAddress, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 40; // Make it taller
        txtAddress = new JTextArea();
        txtAddress.setFont(FONT_FIELD);
        txtAddress.setLineWrap(true);
        txtAddress.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtAddress);
        formPanel.add(scrollPane, gbc);

        // --- Create the Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(COLOR_BACKGROUND);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        btnSave = new JButton("Save");
        btnSave.setBackground(new Color(0, 128, 0));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(FONT_LABEL);

        btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(192, 0, 0));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(FONT_LABEL);

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        // --- Add Panels to Dialog ---
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Add Action Listeners ---
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
    }

    /** Helper to add a label and text field to the form */
    private JTextField addFormField(JPanel panel, GridBagConstraints gbc, String label) {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.ipady = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(FONT_LABEL);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField txtField = new JTextField();
        txtField.setFont(FONT_FIELD);
        panel.add(txtField, gbc);
        return txtField;
    }

    /** Helper to add a non-field component */
    private void addField(JPanel panel, GridBagConstraints gbc, JLabel label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.ipady = 0;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        field.setFont(FONT_FIELD);
        panel.add(field, gbc);
    }

    /** Helper to add a section title to the form */
    private void addSectionTitle(JPanel panel, GridBagConstraints gbc, String title) {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 0, 5, 0); // Top padding
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(new Color(0, 100, 180));
        lblTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        panel.add(lblTitle, gbc);

        // Reset GBC
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    }

    /**
     * Fills the form fields with data from the student object (for "Edit" mode).
     */
    private void fillForm() {
        if (student == null) return;

        // We can't get username/email/password from the Student object,
        // so we'll leave them blank and locked for "Edit" mode.
        txtUsername.setText("N/A (Locked for Edit)");
        txtEmail.setText("N/A (Locked for Edit)");
        txtPassword.setText("********");

        txtRollNo.setText(student.getRollNo());
        txtFirstName.setText(student.getFirstName());
        txtLastName.setText(student.getLastName());
        txtDob.setText(student.getDob() != null ? student.getDob().toString() : "");
        cmbGender.setSelectedItem(student.getGender());
        txtContact.setText(student.getContact());
        txtAddress.setText(student.getAddress());
        txtCourse.setText(student.getCourse());
        txtYear.setText(String.valueOf(student.getYear()));
    }

    /**
     * Called when the "Save" button is clicked.
     * Validates and saves the data.
     */
    private void onSave() {
        // TODO: Add input validation (e.g., check for empty fields)

        if (student == null) {
            // "Add New" mode: create a new student object
            this.student = new Student();
        }

        // Update the student object with data from the form
        student.setRollNo(txtRollNo.getText());
        student.setFirstName(txtFirstName.getText());
        student.setLastName(txtLastName.getText());
        student.setDob(Date.valueOf(txtDob.getText())); // Note: This will fail if format is wrong!
        student.setGender((String) cmbGender.getSelectedItem());
        student.setContact(txtContact.getText());
        student.setAddress(txtAddress.getText());
        student.setCourse(txtCourse.getText());
        student.setYear(Integer.parseInt(txtYear.getText())); // Note: This will fail if not a number!

        this.isSaved = true;
        dispose(); // Close the dialog
    }

    /**
     * Called when the "Cancel" button is clicked.
     */
    private void onCancel() {
        this.isSaved = false;
        dispose(); // Close the dialog
    }

    // --- Public methods to get the data after closing ---

    /**
     * @return true if the "Save" button was clicked, false otherwise.
     */
    public boolean isSaved() {
        return isSaved;
    }

    /**
     * @return The Student object with the new/updated data.
     */
    public Student getStudent() {
        return student;
    }

    // These are needed only for "Add New" mode
    public String getUsername() { return txtUsername.getText(); }
    public String getEmail() { return txtEmail.getText(); }
    public String getPassword() { return new String(txtPassword.getPassword()); }

}
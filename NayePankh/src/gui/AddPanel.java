package gui;

import service.VolunteerService;
import util.InputValidator;
import util.UITheme;
import model.Volunteer;

import javax.swing.*;
import java.awt.*;

/**
 * AddPanel — Form for registering a new volunteer.
 */
public class AddPanel extends JPanel {

    private final VolunteerService service;
    private final DashboardFrame   parent;

    // Form fields
    private JTextField tfName, tfEmail, tfPhone, tfCity, tfSkills;
    private JLabel     lblStatus;

    public AddPanel(VolunteerService service, DashboardFrame parent) {
        this.service = service;
        this.parent  = parent;
        initUI();
    }

    private void initUI() {
        setBackground(UITheme.BG_MAIN);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        // Header
        JLabel heading = new JLabel("Add New Volunteer");
        heading.setFont(UITheme.FONT_TITLE);
        heading.setForeground(UITheme.PRIMARY_DARK);
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 22, 0));
        add(heading, BorderLayout.NORTH);

        // Form card
        JPanel card = UITheme.createCard();
        card.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(8, 8, 8, 8);

        // Create fields
        tfName   = UITheme.createTextField();
        tfEmail  = UITheme.createTextField();
        tfPhone  = UITheme.createTextField();
        tfCity   = UITheme.createTextField();
        tfSkills = UITheme.createTextField();
        tfSkills.setToolTipText("e.g. Teaching, Design, Social Media");

        // Row 0 — Name
        addRow(card, gc, 0, "Full Name *", tfName);
        // Row 1 — Email
        addRow(card, gc, 1, "Email Address *", tfEmail);
        // Row 2 — Phone
        addRow(card, gc, 2, "Phone Number *", tfPhone);
        // Row 3 — City
        addRow(card, gc, 3, "City *", tfCity);
        // Row 4 — Skills
        addRow(card, gc, 4, "Skills", tfSkills);

        // Row 5 — Buttons
        gc.gridx = 0; gc.gridy = 5; gc.gridwidth = 1;
        JButton btnSubmit = UITheme.createButton("✔  Register Volunteer", UITheme.SUCCESS, Color.WHITE);
        btnSubmit.setPreferredSize(new Dimension(200, 40));
        btnSubmit.addActionListener(e -> handleSubmit());
        card.add(btnSubmit, gc);

        gc.gridx = 1;
        JButton btnClear = UITheme.createButton("✖  Clear Form", UITheme.DANGER, Color.WHITE);
        btnClear.setPreferredSize(new Dimension(150, 40));
        btnClear.addActionListener(e -> clearForm());
        card.add(btnClear, gc);

        // Row 6 — Status message
        gc.gridx = 0; gc.gridy = 6; gc.gridwidth = 2;
        lblStatus = new JLabel(" ");
        lblStatus.setFont(UITheme.FONT_BODY);
        card.add(lblStatus, gc);

        // Center the card
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(card);
        add(wrapper, BorderLayout.CENTER);

        // Note about required fields
        JLabel note = new JLabel("* Required fields  |  Phone must be 10 digits (India)");
        note.setFont(UITheme.FONT_SMALL);
        note.setForeground(UITheme.TEXT_MUTED);
        note.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        add(note, BorderLayout.SOUTH);
    }

    /** Adds a label + text field row to the form. */
    private void addRow(JPanel card, GridBagConstraints gc,
                        int row, String labelText, JTextField field) {
        gc.gridwidth = 1;
        gc.weightx   = 0;
        gc.gridx = 0; gc.gridy = row;
        JLabel lbl = UITheme.createLabel(labelText);
        lbl.setPreferredSize(new Dimension(160, 36));
        card.add(lbl, gc);

        gc.gridx = 1; gc.weightx = 1;
        field.setPreferredSize(new Dimension(320, 36));
        card.add(field, gc);
    }

    /** Validates and submits the form. */
    private void handleSubmit() {
        String name   = tfName.getText().trim();
        String email  = tfEmail.getText().trim();
        String phone  = tfPhone.getText().trim();
        String city   = tfCity.getText().trim();
        String skills = tfSkills.getText().trim();

        // Validation
        if (!InputValidator.isNotEmpty(name)) {
            showError("Full Name is required.");
            tfName.requestFocus(); return;
        }
        if (!InputValidator.isValidEmail(email)) {
            showError("Please enter a valid email address.");
            tfEmail.requestFocus(); return;
        }
        if (!InputValidator.isValidPhone(phone)) {
            showError("Phone must be a 10-digit Indian number (starts with 6-9).");
            tfPhone.requestFocus(); return;
        }
        if (!InputValidator.isNotEmpty(city)) {
            showError("City is required.");
            tfCity.requestFocus(); return;
        }

        // Add volunteer
        Volunteer v = service.addVolunteer(name, email, phone, city,
                skills.isEmpty() ? "—" : skills);
        showSuccess("Volunteer registered successfully! ID: " + v.getVolunteerId());
        clearForm();
    }

    private void clearForm() {
        tfName.setText("");  tfEmail.setText("");
        tfPhone.setText(""); tfCity.setText(""); tfSkills.setText("");
        lblStatus.setText(" ");
        tfName.requestFocus();
    }

    private void showError(String msg) {
        lblStatus.setForeground(UITheme.DANGER);
        lblStatus.setText("⚠ " + msg);
    }

    private void showSuccess(String msg) {
        lblStatus.setForeground(UITheme.SUCCESS);
        lblStatus.setText("✔ " + msg);
    }
}

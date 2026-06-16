package gui;

import model.Volunteer;
import service.VolunteerService;
import util.InputValidator;
import util.UITheme;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * ListPanel — Displays all volunteers in a table with
 * Edit and Delete functionality per row.
 */
public class ListPanel extends JPanel {

    private final VolunteerService service;
    private final DashboardFrame   parent;

    private JTable       table;
    private DefaultTableModel tableModel;
    private JLabel       lblCount;

    // Column names for the table
    private static final String[] COLUMNS = {
        "Volunteer ID", "Full Name", "Email", "Phone", "City", "Skills", "Registered On"
    };

    public ListPanel(VolunteerService service, DashboardFrame parent) {
        this.service = service;
        this.parent  = parent;
        initUI();
    }

    private void initUI() {
        setBackground(UITheme.BG_MAIN);
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        // Header row
        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setOpaque(false);
        headerRow.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        JLabel heading = new JLabel("All Volunteers");
        heading.setFont(UITheme.FONT_TITLE);
        heading.setForeground(UITheme.PRIMARY_DARK);
        headerRow.add(heading, BorderLayout.WEST);

        lblCount = new JLabel();
        lblCount.setFont(UITheme.FONT_BODY);
        lblCount.setForeground(UITheme.TEXT_MUTED);
        headerRow.add(lblCount, BorderLayout.EAST);
        add(headerRow, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        styleTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xE0E0E0), 1, true));
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);

        // Bottom toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolbar.setOpaque(false);
        toolbar.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        JButton btnEdit   = UITheme.createButton("✏  Edit Selected",   UITheme.PRIMARY, Color.WHITE);
        JButton btnDelete = UITheme.createButton("🗑  Delete Selected", UITheme.DANGER,  Color.WHITE);
        JButton btnRefresh= UITheme.createButton("🔄  Refresh",        UITheme.SUCCESS, Color.WHITE);

        btnEdit.setPreferredSize(new Dimension(160, 36));
        btnDelete.setPreferredSize(new Dimension(160, 36));
        btnRefresh.setPreferredSize(new Dimension(120, 36));

        btnEdit.addActionListener(e -> handleEdit());
        btnDelete.addActionListener(e -> handleDelete());
        btnRefresh.addActionListener(e -> refreshTable());

        toolbar.add(btnEdit);
        toolbar.add(btnDelete);
        toolbar.add(btnRefresh);
        add(toolbar, BorderLayout.SOUTH);

        refreshTable();
    }

    /** Re-loads table data from the service. */
    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Volunteer> list = service.getAllVolunteers();
        for (Volunteer v : list) {
            tableModel.addRow(new Object[]{
                v.getVolunteerId(), v.getFullName(), v.getEmail(),
                v.getPhoneNumber(), v.getCity(), v.getSkills(),
                v.getDateOfRegistration()
            });
        }
        lblCount.setText("Total: " + list.size() + " volunteer(s)");
    }

    /** Opens an edit dialog for the selected row. */
    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a volunteer to edit.", "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        Volunteer v = service.findById(id);
        if (v == null) return;

        // Build edit dialog
        JDialog dialog = new JDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            "Edit Volunteer — " + id, true);
        dialog.setSize(460, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(20, 24, 12, 24));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(6, 6, 6, 6);

        JTextField tfName   = UITheme.createTextField();
        JTextField tfEmail  = UITheme.createTextField();
        JTextField tfPhone  = UITheme.createTextField();
        JTextField tfCity   = UITheme.createTextField();
        JTextField tfSkills = UITheme.createTextField();

        tfName.setText(v.getFullName());   tfEmail.setText(v.getEmail());
        tfPhone.setText(v.getPhoneNumber()); tfCity.setText(v.getCity());
        tfSkills.setText(v.getSkills());

        String[][] fields = {
            {"Full Name *", null}, {"Email *", null},
            {"Phone *", null}, {"City *", null}, {"Skills", null}
        };
        JTextField[] tfs = {tfName, tfEmail, tfPhone, tfCity, tfSkills};

        for (int i = 0; i < tfs.length; i++) {
            gc.gridx = 0; gc.gridy = i; gc.weightx = 0;
            JLabel lbl = UITheme.createLabel(fields[i][0]);
            lbl.setPreferredSize(new Dimension(120, 34));
            form.add(lbl, gc);
            gc.gridx = 1; gc.weightx = 1;
            tfs[i].setPreferredSize(new Dimension(250, 34));
            form.add(tfs[i], gc);
        }

        JLabel lblMsg = new JLabel(" ");
        lblMsg.setFont(UITheme.FONT_SMALL);
        gc.gridx = 0; gc.gridy = 5; gc.gridwidth = 2;
        form.add(lblMsg, gc);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = UITheme.createButton("Save Changes", UITheme.SUCCESS, Color.WHITE);
        JButton btnCancel = UITheme.createButton("Cancel", UITheme.DANGER, Color.WHITE);
        btnSave.setPreferredSize(new Dimension(140, 36));
        btnCancel.setPreferredSize(new Dimension(100, 36));
        btnRow.add(btnSave); btnRow.add(btnCancel);

        btnCancel.addActionListener(e -> dialog.dispose());
        btnSave.addActionListener(e -> {
            String name  = tfName.getText().trim();
            String email = tfEmail.getText().trim();
            String phone = tfPhone.getText().trim();
            String city  = tfCity.getText().trim();
            String skills= tfSkills.getText().trim();

            if (!InputValidator.isNotEmpty(name)) {
                lblMsg.setForeground(UITheme.DANGER); lblMsg.setText("⚠ Name is required."); return; }
            if (!InputValidator.isValidEmail(email)) {
                lblMsg.setForeground(UITheme.DANGER); lblMsg.setText("⚠ Invalid email."); return; }
            if (!InputValidator.isValidPhone(phone)) {
                lblMsg.setForeground(UITheme.DANGER); lblMsg.setText("⚠ Invalid phone."); return; }
            if (!InputValidator.isNotEmpty(city)) {
                lblMsg.setForeground(UITheme.DANGER); lblMsg.setText("⚠ City is required."); return; }

            service.updateVolunteer(id, name, email, phone, city,
                    skills.isEmpty() ? "—" : skills);
            dialog.dispose();
            refreshTable();
            JOptionPane.showMessageDialog(this, "Volunteer updated successfully!",
                    "Updated", JOptionPane.INFORMATION_MESSAGE);
        });

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnRow, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /** Deletes the selected volunteer after confirmation. */
    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a volunteer to delete.", "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id   = (String) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete volunteer " + name + " (" + id + ")?\nThis cannot be undone.",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            service.deleteVolunteer(id);
            refreshTable();
            JOptionPane.showMessageDialog(this,
                "Volunteer deleted successfully.", "Deleted",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void styleTable() {
        table.setRowHeight(32);
        table.setFont(UITheme.FONT_BODY);
        table.setGridColor(new Color(0xEEEEEE));
        table.setSelectionBackground(new Color(0xBBDEFB));
        table.setSelectionForeground(UITheme.TEXT_DARK);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 1));

        // Header style
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(UITheme.PRIMARY);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 36));
        header.setReorderingAllowed(false);

        // Column widths
        int[] widths = {90, 160, 200, 110, 100, 150, 110};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Alternate row colors via renderer
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                if (!sel) {
                    setBackground(r % 2 == 0 ? Color.WHITE : new Color(0xF5F9FF));
                    setForeground(UITheme.TEXT_DARK);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        });
    }
}

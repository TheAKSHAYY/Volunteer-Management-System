package gui;

import model.Volunteer;
import service.VolunteerService;
import util.UITheme;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * SearchPanel — Search volunteers by ID or by Name.
 */
public class SearchPanel extends JPanel {

    private final VolunteerService service;

    private JTextField tfSearch;
    private JRadioButton rbId, rbName;
    private DefaultTableModel tableModel;
    private JLabel lblResult;

    private static final String[] COLUMNS = {
        "Volunteer ID", "Full Name", "Email", "Phone", "City", "Skills", "Registered On"
    };

    public SearchPanel(VolunteerService service) {
        this.service = service;
        initUI();
    }

    private void initUI() {
        setBackground(UITheme.BG_MAIN);
        setLayout(new BorderLayout(0, 16));
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        // Header
        JLabel heading = new JLabel("Search Volunteers");
        heading.setFont(UITheme.FONT_TITLE);
        heading.setForeground(UITheme.PRIMARY_DARK);
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        add(heading, BorderLayout.NORTH);

        // Search card
        JPanel searchCard = UITheme.createCard();
        searchCard.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 8));

        // Search type radio buttons
        rbId   = new JRadioButton("Search by ID");
        rbName = new JRadioButton("Search by Name");
        rbId.setFont(UITheme.FONT_BODY);
        rbName.setFont(UITheme.FONT_BODY);
        rbId.setOpaque(false);
        rbName.setOpaque(false);
        rbId.setSelected(true);

        ButtonGroup bg = new ButtonGroup();
        bg.add(rbId); bg.add(rbName);

        tfSearch = UITheme.createTextField();
        tfSearch.setPreferredSize(new Dimension(280, 36));
        tfSearch.addActionListener(e -> performSearch()); // Enter key search

        JButton btnSearch = UITheme.createButton("🔍 Search", UITheme.PRIMARY, Color.WHITE);
        btnSearch.setPreferredSize(new Dimension(110, 36));
        btnSearch.addActionListener(e -> performSearch());

        JButton btnClear = UITheme.createButton("✖ Clear", UITheme.DANGER, Color.WHITE);
        btnClear.setPreferredSize(new Dimension(90, 36));
        btnClear.addActionListener(e -> {
            tfSearch.setText(""); tableModel.setRowCount(0);
            lblResult.setText("Enter a search term above.");
        });

        searchCard.add(new JLabel("Search:"));
        searchCard.add(tfSearch);
        searchCard.add(rbId);
        searchCard.add(rbName);
        searchCard.add(btnSearch);
        searchCard.add(btnClear);

        // Result status
        lblResult = new JLabel("Enter a search term above.");
        lblResult.setFont(UITheme.FONT_BODY);
        lblResult.setForeground(UITheme.TEXT_MUTED);
        searchCard.add(lblResult);

        add(searchCard, BorderLayout.NORTH);

        // Results table
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xE0E0E0), 1, true));
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);
    }

    private void performSearch() {
        String query = tfSearch.getText().trim();
        if (query.isEmpty()) {
            lblResult.setForeground(UITheme.WARNING);
            lblResult.setText("⚠ Please enter a search term.");
            return;
        }

        tableModel.setRowCount(0);

        if (rbId.isSelected()) {
            Volunteer v = service.findById(query);
            if (v != null) {
                addRow(v);
                lblResult.setForeground(UITheme.SUCCESS);
                lblResult.setText("✔ Volunteer found.");
            } else {
                lblResult.setForeground(UITheme.DANGER);
                lblResult.setText("✘ No volunteer found with ID: " + query);
            }
        } else {
            List<Volunteer> results = service.findByName(query);
            for (Volunteer v : results) addRow(v);
            if (results.isEmpty()) {
                lblResult.setForeground(UITheme.DANGER);
                lblResult.setText("✘ No volunteers found matching: " + query);
            } else {
                lblResult.setForeground(UITheme.SUCCESS);
                lblResult.setText("✔ Found " + results.size() + " result(s).");
            }
        }
    }

    private void addRow(Volunteer v) {
        tableModel.addRow(new Object[]{
            v.getVolunteerId(), v.getFullName(), v.getEmail(),
            v.getPhoneNumber(), v.getCity(), v.getSkills(),
            v.getDateOfRegistration()
        });
    }

    private void styleTable(JTable table) {
        table.setRowHeight(32);
        table.setFont(UITheme.FONT_BODY);
        table.setGridColor(new Color(0xEEEEEE));
        table.setSelectionBackground(new Color(0xBBDEFB));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(UITheme.PRIMARY);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 36));
        header.setReorderingAllowed(false);

        int[] widths = {90, 160, 200, 110, 100, 150, 110};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
    }
}

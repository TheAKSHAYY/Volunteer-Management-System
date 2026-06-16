package gui;

import service.VolunteerService;
import util.FileHandler;
import util.UITheme;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * ReportPanel — Displays summary statistics and allows exporting a full report.
 */
public class ReportPanel extends JPanel {

    private final VolunteerService service;
    private JTextArea  taReport;
    private JLabel     lblStatus;

    public ReportPanel(VolunteerService service) {
        this.service = service;
        initUI();
    }

    private void initUI() {
        setBackground(UITheme.BG_MAIN);
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        // Header
        JLabel heading = new JLabel("Reports & Analytics");
        heading.setFont(UITheme.FONT_TITLE);
        heading.setForeground(UITheme.PRIMARY_DARK);
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
        add(heading, BorderLayout.NORTH);

        // Split: left = stats cards, right = text report
        JPanel main = new JPanel(new BorderLayout(20, 0));
        main.setOpaque(false);

        // ── Left: stat cards ──────────────────────────────
        JPanel leftCol = new JPanel();
        leftCol.setLayout(new BoxLayout(leftCol, BoxLayout.Y_AXIS));
        leftCol.setOpaque(false);
        leftCol.setPreferredSize(new Dimension(240, 0));

        leftCol.add(buildActionCard());
        leftCol.add(Box.createRigidArea(new Dimension(0, 14)));
        leftCol.add(buildCityCard());

        main.add(leftCol, BorderLayout.WEST);

        // ── Right: text report area ───────────────────────
        taReport = new JTextArea();
        taReport.setFont(UITheme.FONT_MONO);
        taReport.setEditable(false);
        taReport.setBackground(new Color(0xFAFAFA));
        taReport.setForeground(UITheme.TEXT_DARK);
        taReport.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JScrollPane scroll = new JScrollPane(taReport);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xE0E0E0), 1, true));
        main.add(scroll, BorderLayout.CENTER);

        add(main, BorderLayout.CENTER);

        // Bottom toolbar
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        JButton btnGenerate = UITheme.createButton("🔄 Generate Report", UITheme.PRIMARY, Color.WHITE);
        JButton btnExport   = UITheme.createButton("💾 Export to File",  UITheme.SUCCESS, Color.WHITE);

        btnGenerate.setPreferredSize(new Dimension(175, 36));
        btnExport.setPreferredSize(new Dimension(165, 36));

        btnGenerate.addActionListener(e -> generateReport());
        btnExport.addActionListener(e -> exportReport());

        lblStatus = new JLabel(" ");
        lblStatus.setFont(UITheme.FONT_BODY);

        bottom.add(btnGenerate);
        bottom.add(btnExport);
        bottom.add(lblStatus);
        add(bottom, BorderLayout.SOUTH);

        generateReport(); // auto-generate on open
    }

    private JPanel buildActionCard() {
        JPanel card = UITheme.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("📊 Quick Stats");
        title.setFont(UITheme.FONT_SUBTITLE);
        title.setForeground(UITheme.PRIMARY_DARK);
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        int total = service.getTotalCount();
        int cities = service.getVolunteersByCity().size();

        card.add(statRow("Total Volunteers", String.valueOf(total), UITheme.PRIMARY));
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(statRow("Cities Covered", String.valueOf(cities), UITheme.SUCCESS));

        return card;
    }

    private JPanel buildCityCard() {
        JPanel card = UITheme.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("🏙️  By City");
        title.setFont(UITheme.FONT_SUBTITLE);
        title.setForeground(UITheme.PRIMARY_DARK);
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        Map<String, Long> cityMap = service.getVolunteersByCity();
        if (cityMap.isEmpty()) {
            JLabel empty = new JLabel("No data yet.");
            empty.setFont(UITheme.FONT_BODY);
            empty.setForeground(UITheme.TEXT_MUTED);
            card.add(empty);
        } else {
            for (Map.Entry<String, Long> e : cityMap.entrySet()) {
                card.add(statRow(e.getKey(), e.getValue() + " volunteer(s)",
                        UITheme.TEXT_DARK));
                card.add(Box.createRigidArea(new Dimension(0, 4)));
            }
        }

        return card;
    }

    private JPanel statRow(String label, String value, Color valueColor) {
        JPanel row = new JPanel(new BorderLayout(6, 0));
        row.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.FONT_BODY);
        lbl.setForeground(UITheme.TEXT_MUTED);
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 13));
        val.setForeground(valueColor);
        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.EAST);
        return row;
    }

    private void generateReport() {
        taReport.setText(service.generateFullReport());
        taReport.setCaretPosition(0);
        lblStatus.setForeground(UITheme.SUCCESS);
        lblStatus.setText("✔ Report generated.");
    }

    private void exportReport() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName  = "NayePankh_Report_" + timestamp + ".txt";
        boolean ok = FileHandler.exportReport(service.generateFullReport(), fileName);
        if (ok) {
            lblStatus.setForeground(UITheme.SUCCESS);
            lblStatus.setText("✔ Exported to: " + fileName);
            JOptionPane.showMessageDialog(this,
                "Report saved as:\n" + fileName, "Export Successful",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            lblStatus.setForeground(UITheme.DANGER);
            lblStatus.setText("✘ Export failed. Check permissions.");
        }
    }
}

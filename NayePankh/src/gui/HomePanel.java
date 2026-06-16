package gui;

import model.Volunteer;
import service.VolunteerService;
import util.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * HomePanel — Dashboard overview with stat cards and recent volunteers.
 */
public class HomePanel extends JPanel {

    private final VolunteerService service;
    private final DashboardFrame   parent;

    // Stat labels we need to refresh
    private JLabel totalLabel;
    private JLabel cityLabel;
    private JLabel recentLabel;

    // Recent table
    private JPanel recentContainer;

    public HomePanel(VolunteerService service, DashboardFrame parent) {
        this.service = service;
        this.parent  = parent;
        initUI();
    }

    private void initUI() {
        setBackground(UITheme.BG_MAIN);
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        // ── Page header ──────────────────────────────────
        JLabel heading = new JLabel("Dashboard");
        heading.setFont(UITheme.FONT_TITLE);
        heading.setForeground(UITheme.PRIMARY_DARK);
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(heading, BorderLayout.NORTH);

        // ── Center content ───────────────────────────────
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);

        // Stat cards row
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 16, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        totalLabel  = new JLabel("0", SwingConstants.CENTER);
        cityLabel   = new JLabel("0", SwingConstants.CENTER);
        recentLabel = new JLabel("0", SwingConstants.CENTER);

        statsRow.add(buildStatCard("👥 Total Volunteers", totalLabel, UITheme.PRIMARY));
        statsRow.add(buildStatCard("🏙️  Cities Covered",  cityLabel,  UITheme.SUCCESS));
        statsRow.add(buildStatCard("🕐 Added This Session", recentLabel, UITheme.ACCENT));

        center.add(statsRow);
        center.add(Box.createRigidArea(new Dimension(0, 24)));

        // Quick-action buttons
        JPanel actionsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actionsRow.setOpaque(false);
        JButton btnAdd    = UITheme.createButton("➕  Add Volunteer",   UITheme.PRIMARY, Color.WHITE);
        JButton btnList   = UITheme.createButton("📋  View All",        UITheme.SUCCESS, Color.WHITE);
        JButton btnReport = UITheme.createButton("📊  View Reports",    UITheme.ACCENT,  Color.WHITE);
        btnAdd.setPreferredSize(new Dimension(160, 38));
        btnList.setPreferredSize(new Dimension(140, 38));
        btnReport.setPreferredSize(new Dimension(160, 38));
        btnAdd.addActionListener(e    -> parent.showPanel("ADD"));
        btnList.addActionListener(e   -> parent.showPanel("LIST"));
        btnReport.addActionListener(e -> parent.showPanel("REPORTS"));
        actionsRow.add(btnAdd);
        actionsRow.add(btnList);
        actionsRow.add(btnReport);
        center.add(actionsRow);
        center.add(Box.createRigidArea(new Dimension(0, 24)));

        // Recent volunteers section
        JLabel recHeading = new JLabel("Recently Registered Volunteers");
        recHeading.setFont(UITheme.FONT_SUBTITLE);
        recHeading.setForeground(UITheme.TEXT_DARK);
        recHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(recHeading);
        center.add(Box.createRigidArea(new Dimension(0, 10)));

        recentContainer = new JPanel();
        recentContainer.setLayout(new BoxLayout(recentContainer, BoxLayout.Y_AXIS));
        recentContainer.setOpaque(false);
        recentContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(recentContainer);

        JScrollPane scroll = new JScrollPane(center);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        add(scroll, BorderLayout.CENTER);

        refreshStats();
    }

    /** Rebuild stats and recent list. Called when switching to this panel. */
    public void refreshStats() {
        int total = service.getTotalCount();
        int cities = service.getVolunteersByCity().size();
        List<Volunteer> recent = service.getRecentVolunteers(5);

        totalLabel.setText(String.valueOf(total));
        cityLabel.setText(String.valueOf(cities));
        recentLabel.setText(String.valueOf(Math.min(recent.size(), 5)));

        // Rebuild recent rows
        recentContainer.removeAll();
        if (recent.isEmpty()) {
            JLabel empty = new JLabel("  No volunteers registered yet.");
            empty.setFont(UITheme.FONT_BODY);
            empty.setForeground(UITheme.TEXT_MUTED);
            recentContainer.add(empty);
        } else {
            for (Volunteer v : recent) {
                recentContainer.add(buildRecentRow(v));
                recentContainer.add(Box.createRigidArea(new Dimension(0, 6)));
            }
        }
        recentContainer.revalidate();
        recentContainer.repaint();
    }

    private JPanel buildStatCard(String title, JLabel valueLabel, Color accent) {
        JPanel card = UITheme.createCard();
        card.setLayout(new BorderLayout(8, 8));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(UITheme.FONT_BODY);
        titleLbl.setForeground(UITheme.TEXT_MUTED);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(accent);

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildRecentRow(Volunteer v) {
        JPanel row = UITheme.createCard();
        row.setLayout(new GridLayout(1, 4, 8, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        row.add(makeCell(v.getVolunteerId(), UITheme.PRIMARY));
        row.add(makeCell(v.getFullName(),    UITheme.TEXT_DARK));
        row.add(makeCell(v.getCity(),        UITheme.TEXT_MUTED));
        row.add(makeCell(v.getDateOfRegistration(), UITheme.TEXT_MUTED));
        return row;
    }

    private JLabel makeCell(String text, Color fg) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UITheme.FONT_BODY);
        lbl.setForeground(fg);
        return lbl;
    }
}

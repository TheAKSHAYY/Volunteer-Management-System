package gui;

import service.VolunteerService;
import util.UITheme;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import model.Volunteer;

/**
 * DashboardFrame — Main application window.
 * Hosts a sidebar navigation and a central content area that
 * swaps between panels (Dashboard, Add, Search, Reports).
 */
public class DashboardFrame extends JFrame {

    private final VolunteerService service;

    // Content panels
    private JPanel contentArea;
    private CardLayout cardLayout;

    // Sub-panels
    private HomePanel    homePanel;
    private AddPanel     addPanel;
    private ListPanel    listPanel;
    private SearchPanel  searchPanel;
    private ReportPanel  reportPanel;

    public DashboardFrame(VolunteerService service) {
        this.service = service;
        initUI();
    }

    private void initUI() {
        setTitle("NayePankh Foundation — Volunteer Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // ── Sidebar ──────────────────────────────────────
        JPanel sidebar = buildSidebar();
        add(sidebar, BorderLayout.WEST);

        // ── Content area (CardLayout) ─────────────────────
        cardLayout  = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(UITheme.BG_MAIN);

        homePanel   = new HomePanel(service, this);
        addPanel    = new AddPanel(service, this);
        listPanel   = new ListPanel(service, this);
        searchPanel = new SearchPanel(service);
        reportPanel = new ReportPanel(service);

        contentArea.add(homePanel,   "HOME");
        contentArea.add(addPanel,    "ADD");
        contentArea.add(listPanel,   "LIST");
        contentArea.add(searchPanel, "SEARCH");
        contentArea.add(reportPanel, "REPORTS");

        add(contentArea, BorderLayout.CENTER);

        cardLayout.show(contentArea, "HOME");
        setVisible(true);
    }

    // ─────────────────────────────────────────────────────
    //  Build sidebar
    // ─────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(UITheme.BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Logo / org name
        JPanel logoBox = new JPanel(new BorderLayout());
        logoBox.setBackground(UITheme.PRIMARY_DARK);
        logoBox.setBorder(BorderFactory.createEmptyBorder(24, 16, 24, 16));

        JLabel orgName = new JLabel("<html><center>🌿 NayePankh<br>Foundation</center></html>");
        orgName.setFont(new Font("Segoe UI", Font.BOLD, 16));
        orgName.setForeground(Color.WHITE);
        orgName.setHorizontalAlignment(SwingConstants.CENTER);
        logoBox.add(orgName, BorderLayout.CENTER);
        logoBox.setMaximumSize(new Dimension(210, 90));
        sidebar.add(logoBox);

        sidebar.add(Box.createRigidArea(new Dimension(0, 12)));

        // Nav buttons
        sidebar.add(navBtn("🏠  Dashboard",  "HOME"));
        sidebar.add(navBtn("➕  Add Volunteer","ADD"));
        sidebar.add(navBtn("📋  All Volunteers","LIST"));
        sidebar.add(navBtn("🔍  Search",      "SEARCH"));
        sidebar.add(navBtn("📊  Reports",     "REPORTS"));

        sidebar.add(Box.createVerticalGlue());

        // Footer
        JLabel footer = new JLabel("  v1.0 — Java Project");
        footer.setFont(UITheme.FONT_SMALL);
        footer.setForeground(new Color(0x90A4AE));
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
        sidebar.add(footer);

        return sidebar;
    }

    private JButton navBtn(String label, String card) {
        JButton btn = new JButton(label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover()
                        ? UITheme.BG_SIDEBAR_BTN : UITheme.BG_SIDEBAR;
                g2.setColor(bg);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(UITheme.BG_SIDEBAR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 0));
        btn.setMaximumSize(new Dimension(210, 48));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> showPanel(card));
        return btn;
    }

    // ─────────────────────────────────────────────────────
    //  Public navigation method
    // ─────────────────────────────────────────────────────
    public void showPanel(String name) {
        if ("LIST".equals(name)) listPanel.refreshTable();
        if ("HOME".equals(name)) homePanel.refreshStats();
        cardLayout.show(contentArea, name);
    }
}

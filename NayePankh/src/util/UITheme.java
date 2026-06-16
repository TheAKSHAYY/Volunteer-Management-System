package util;

import java.awt.*;

/**
 * UITheme — central color and font constants for the NayePankh VMS GUI.
 * Change values here to restyle the whole application.
 */
public class UITheme {

    // Brand colours
    public static final Color PRIMARY        = new Color(0x1565C0); // deep blue
    public static final Color PRIMARY_DARK   = new Color(0x003C8F);
    public static final Color PRIMARY_LIGHT  = new Color(0x5E92F3);
    public static final Color ACCENT         = new Color(0xFF6F00); // amber
    public static final Color SUCCESS        = new Color(0x2E7D32);
    public static final Color DANGER         = new Color(0xC62828);
    public static final Color WARNING        = new Color(0xF57F17);

    // Backgrounds
    public static final Color BG_MAIN        = new Color(0xF0F4F8);
    public static final Color BG_CARD        = Color.WHITE;
    public static final Color BG_SIDEBAR     = new Color(0x1A237E);
    public static final Color BG_SIDEBAR_BTN = new Color(0x283593);

    // Text
    public static final Color TEXT_DARK      = new Color(0x212121);
    public static final Color TEXT_MUTED     = new Color(0x757575);
    public static final Color TEXT_WHITE     = Color.WHITE;

    // Fonts
    public static final Font FONT_TITLE      = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE   = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BODY       = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL      = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BTN        = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_MONO       = new Font("Courier New", Font.PLAIN, 12);

    /** Creates a styled rounded button. */
    public static javax.swing.JButton createButton(String text, Color bg, Color fg) {
        javax.swing.JButton btn = new javax.swing.JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed()  ? bg.darker()
                           : getModel().isRollover() ? bg.brighter()
                           : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BTN);
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /** Creates a styled text field. */
    public static javax.swing.JTextField createTextField() {
        javax.swing.JTextField tf = new javax.swing.JTextField();
        tf.setFont(FONT_BODY);
        tf.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new Color(0xBDBDBD), 1, true),
                javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        tf.setPreferredSize(new Dimension(0, 36));
        return tf;
    }

    /** Creates a label. */
    public static javax.swing.JLabel createLabel(String text) {
        javax.swing.JLabel lbl = new javax.swing.JLabel(text);
        lbl.setFont(FONT_BODY);
        lbl.setForeground(TEXT_DARK);
        return lbl;
    }

    /** White card panel with subtle shadow border. */
    public static javax.swing.JPanel createCard() {
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setBackground(BG_CARD);
        panel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new Color(0xE0E0E0), 1, true),
                javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20)));
        return panel;
    }
}

import gui.DashboardFrame;
import service.VolunteerService;

import javax.swing.*;

/**
 * Main — Entry point for the NayePankh Volunteer Management System.
 *
 * Launch this class to start the application.
 * The application:
 *   1. Loads existing volunteer data from volunteers.txt (if present)
 *   2. Opens the main Dashboard window
 *   3. Auto-saves to file on every add / update / delete
 */
public class Main {

    public static void main(String[] args) {

        // Set Nimbus look-and-feel for a modern Swing appearance
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Falls back to default look-and-feel — safe to ignore
        }

        // Run GUI on the Event Dispatch Thread (EDT) — Swing best practice
        SwingUtilities.invokeLater(() -> {
            VolunteerService service = new VolunteerService();
            new DashboardFrame(service);
        });
    }
}

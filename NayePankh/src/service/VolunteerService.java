package service;

import model.Volunteer;
import util.FileHandler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * VolunteerService Class
 * Contains all business logic for managing volunteers.
 * Acts as the bridge between the GUI and data storage.
 */
public class VolunteerService {

    // In-memory list of all volunteers
    private List<Volunteer> volunteerList;

    // Counter to auto-generate unique IDs
    private int idCounter;

    /**
     * Constructor: loads existing data from file on startup.
     */
    public VolunteerService() {
        volunteerList = FileHandler.loadVolunteers();
        idCounter = computeNextId();
    }

    /**
     * Computes the next ID based on existing records.
     */
    private int computeNextId() {
        int max = 1000; // IDs start from NPF-1001
        for (Volunteer v : volunteerList) {
            try {
                int num = Integer.parseInt(v.getVolunteerId().replace("NPF-", ""));
                if (num > max) max = num;
            } catch (NumberFormatException ignored) {}
        }
        return max + 1;
    }

    /**
     * Generates a unique volunteer ID.
     */
    private String generateId() {
        return "NPF-" + idCounter++;
    }

    // ─────────────────────────────────────────────
    //  CRUD OPERATIONS
    // ─────────────────────────────────────────────

    /**
     * Adds a new volunteer to the system.
     *
     * @return The newly created Volunteer object
     */
    public Volunteer addVolunteer(String name, String email, String phone,
                                  String city, String skills) {
        String id = generateId();
        Volunteer v = new Volunteer(id, name, email, phone, city, skills);
        volunteerList.add(v);
        FileHandler.saveVolunteers(volunteerList); // persist immediately
        return v;
    }

    /**
     * Returns all volunteers.
     */
    public List<Volunteer> getAllVolunteers() {
        return new ArrayList<>(volunteerList);
    }

    /**
     * Searches for a volunteer by exact ID.
     *
     * @return Volunteer if found, null otherwise
     */
    public Volunteer findById(String id) {
        for (Volunteer v : volunteerList) {
            if (v.getVolunteerId().equalsIgnoreCase(id.trim())) return v;
        }
        return null;
    }

    /**
     * Searches for volunteers whose name contains the query (case-insensitive).
     */
    public List<Volunteer> findByName(String name) {
        List<Volunteer> results = new ArrayList<>();
        String query = name.trim().toLowerCase();
        for (Volunteer v : volunteerList) {
            if (v.getFullName().toLowerCase().contains(query)) {
                results.add(v);
            }
        }
        return results;
    }

    /**
     * Updates an existing volunteer's details.
     *
     * @return true if the volunteer was found and updated
     */
    public boolean updateVolunteer(String id, String name, String email,
                                    String phone, String city, String skills) {
        Volunteer v = findById(id);
        if (v == null) return false;

        v.setFullName(name);
        v.setEmail(email);
        v.setPhoneNumber(phone);
        v.setCity(city);
        v.setSkills(skills);

        FileHandler.saveVolunteers(volunteerList);
        return true;
    }

    /**
     * Deletes a volunteer by ID.
     *
     * @return true if found and removed
     */
    public boolean deleteVolunteer(String id) {
        Volunteer v = findById(id);
        if (v == null) return false;
        volunteerList.remove(v);
        FileHandler.saveVolunteers(volunteerList);
        return true;
    }

    // ─────────────────────────────────────────────
    //  REPORTS
    // ─────────────────────────────────────────────

    /**
     * Returns total count of volunteers.
     */
    public int getTotalCount() {
        return volunteerList.size();
    }

    /**
     * Returns a map of City → count of volunteers.
     */
    public Map<String, Long> getVolunteersByCity() {
        Map<String, Long> cityMap = new LinkedHashMap<>();
        for (Volunteer v : volunteerList) {
            String city = v.getCity().trim();
            cityMap.put(city, cityMap.getOrDefault(city, 0L) + 1);
        }
        return cityMap;
    }

    /**
     * Returns the last N registered volunteers (most recently added).
     */
    public List<Volunteer> getRecentVolunteers(int n) {
        int size = volunteerList.size();
        int from = Math.max(0, size - n);
        List<Volunteer> recent = new ArrayList<>(volunteerList.subList(from, size));
        // Reverse so newest appears first
        java.util.Collections.reverse(recent);
        return recent;
    }

    /**
     * Generates a full text-based report.
     */
    public String generateFullReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════\n");
        sb.append("        NAYEPANKH FOUNDATION — VOLUNTEER REPORT        \n");
        sb.append("═══════════════════════════════════════════════════════\n\n");

        sb.append("📊 SUMMARY\n");
        sb.append("───────────────────────────────────────────────────────\n");
        sb.append("  Total Registered Volunteers : ").append(getTotalCount()).append("\n\n");

        sb.append("🏙️  VOLUNTEERS BY CITY\n");
        sb.append("───────────────────────────────────────────────────────\n");
        Map<String, Long> cityMap = getVolunteersByCity();
        if (cityMap.isEmpty()) {
            sb.append("  No data available.\n");
        } else {
            for (Map.Entry<String, Long> entry : cityMap.entrySet()) {
                sb.append(String.format("  %-20s : %d\n", entry.getKey(), entry.getValue()));
            }
        }

        sb.append("\n🕐 RECENTLY ADDED VOLUNTEERS (Last 5)\n");
        sb.append("───────────────────────────────────────────────────────\n");
        List<Volunteer> recent = getRecentVolunteers(5);
        if (recent.isEmpty()) {
            sb.append("  No volunteers registered yet.\n");
        } else {
            for (Volunteer v : recent) {
                sb.append(String.format("  %-10s  %-20s  %-15s  %s\n",
                        v.getVolunteerId(), v.getFullName(),
                        v.getCity(), v.getDateOfRegistration()));
            }
        }

        sb.append("\n📋 FULL VOLUNTEER DIRECTORY\n");
        sb.append("───────────────────────────────────────────────────────\n");
        if (volunteerList.isEmpty()) {
            sb.append("  No volunteers registered yet.\n");
        } else {
            sb.append(String.format("  %-10s  %-20s  %-25s  %-15s  %-15s  %s\n",
                    "ID", "Name", "Email", "Phone", "City", "Date"));
            sb.append("  " + "─".repeat(100) + "\n");
            for (Volunteer v : volunteerList) {
                sb.append(String.format("  %-10s  %-20s  %-25s  %-15s  %-15s  %s\n",
                        v.getVolunteerId(), v.getFullName(), v.getEmail(),
                        v.getPhoneNumber(), v.getCity(), v.getDateOfRegistration()));
            }
        }

        sb.append("\n═══════════════════════════════════════════════════════\n");
        sb.append("         Report generated by NayePankh VMS v1.0        \n");
        sb.append("═══════════════════════════════════════════════════════\n");
        return sb.toString();
    }
}

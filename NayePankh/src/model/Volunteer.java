package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Volunteer Model Class
 * Represents a single volunteer in the NayePankh system.
 * Implements Serializable for file-based persistence.
 */
public class Volunteer implements Serializable {

    private static final long serialVersionUID = 1L;

    // --- Fields ---
    private String volunteerId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String city;
    private String skills;
    private String dateOfRegistration;

    // --- Constructor (used when adding a new volunteer) ---
    public Volunteer(String volunteerId, String fullName, String email,
                     String phoneNumber, String city, String skills) {
        this.volunteerId = volunteerId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.skills = skills;
        // Auto-set registration date to today
        this.dateOfRegistration = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    // --- Full Constructor (used when loading from file) ---
    public Volunteer(String volunteerId, String fullName, String email,
                     String phoneNumber, String city, String skills,
                     String dateOfRegistration) {
        this.volunteerId = volunteerId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.skills = skills;
        this.dateOfRegistration = dateOfRegistration;
    }

    // --- Getters ---
    public String getVolunteerId()        { return volunteerId; }
    public String getFullName()           { return fullName; }
    public String getEmail()              { return email; }
    public String getPhoneNumber()        { return phoneNumber; }
    public String getCity()               { return city; }
    public String getSkills()             { return skills; }
    public String getDateOfRegistration() { return dateOfRegistration; }

    // --- Setters ---
    public void setFullName(String fullName)         { this.fullName = fullName; }
    public void setEmail(String email)               { this.email = email; }
    public void setPhoneNumber(String phoneNumber)   { this.phoneNumber = phoneNumber; }
    public void setCity(String city)                 { this.city = city; }
    public void setSkills(String skills)             { this.skills = skills; }

    /**
     * Converts volunteer data to a CSV line for file storage.
     * Format: ID|Name|Email|Phone|City|Skills|Date
     */
    public String toFileString() {
        return volunteerId + "|" + fullName + "|" + email + "|"
                + phoneNumber + "|" + city + "|" + skills + "|" + dateOfRegistration;
    }

    /**
     * Parses a CSV line back into a Volunteer object.
     */
    public static Volunteer fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length != 7) return null;
        return new Volunteer(parts[0], parts[1], parts[2],
                             parts[3], parts[4], parts[5], parts[6]);
    }

    @Override
    public String toString() {
        return "[" + volunteerId + "] " + fullName + " | " + city + " | " + email;
    }
}

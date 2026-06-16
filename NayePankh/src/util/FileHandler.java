package util;

import model.Volunteer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FileHandler Utility Class
 * Handles all file I/O operations for volunteer data persistence.
 * Data is stored in a plain text file (pipe-separated values).
 */
public class FileHandler {

    // Path to the data file (created in the project root)
    private static final String FILE_PATH = "volunteers.txt";

    /**
     * Saves the entire list of volunteers to the file.
     * Overwrites existing content.
     *
     * @param volunteers List of Volunteer objects to save
     */
    public static void saveVolunteers(List<Volunteer> volunteers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Volunteer v : volunteers) {
                writer.write(v.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving volunteers: " + e.getMessage());
        }
    }

    /**
     * Loads volunteer records from the data file.
     * Returns an empty list if file doesn't exist or is empty.
     *
     * @return List of Volunteer objects loaded from file
     */
    public static List<Volunteer> loadVolunteers() {
        List<Volunteer> volunteers = new ArrayList<>();
        File file = new File(FILE_PATH);

        // If file doesn't exist yet, return empty list
        if (!file.exists()) return volunteers;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    Volunteer v = Volunteer.fromFileString(line);
                    if (v != null) volunteers.add(v);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading volunteers: " + e.getMessage());
        }

        return volunteers;
    }

    /**
     * Exports a formatted text report to a separate file.
     *
     * @param content The report text to write
     * @param fileName The output file name (e.g. "report.txt")
     * @return true if export was successful
     */
    public static boolean exportReport(String content, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting report: " + e.getMessage());
            return false;
        }
    }
}

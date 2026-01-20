package com.userstream.web.service;

import com.userstream.user.UserData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Service for writing user data to CSV file.
 * Creates the file with headers if it doesn't exist, otherwise appends to it.
 */
public class CsvUserWriter {

    private static final String DEFAULT_CSV_PATH = "users.csv";
    private static final String CSV_HEADER = "id,name,surname,email,createdAt,active";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final String csvFilePath;

    public CsvUserWriter() {
        this(DEFAULT_CSV_PATH);
    }

    public CsvUserWriter(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    /**
     * Writes a user to the CSV file.
     * Creates the file with headers if it doesn't exist.
     *
     * @param user The user data to write
     * @throws IOException if writing fails
     */
    public void writeUser(UserData user) throws IOException {
        File file = new File(csvFilePath);
        boolean fileExists = file.exists();

        try (FileWriter fw = new FileWriter(file, true);
             PrintWriter pw = new PrintWriter(fw)) {

            // Write header if file is new
            if (!fileExists) {
                pw.println(CSV_HEADER);
            }

            // Write user data
            pw.println(formatUserAsCsv(user));
        }
    }

    /**
     * Formats a UserData object as a CSV row.
     */
    private String formatUserAsCsv(UserData user) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(escapeCsvField(user.getId())).append(",");
        sb.append(escapeCsvField(user.getName())).append(",");
        sb.append(escapeCsvField(user.getSurname())).append(",");
        sb.append(escapeCsvField(user.getEmail())).append(",");
        sb.append(formatDateTime(user.getCreatedAt())).append(",");
        sb.append(user.isActive());
        
        return sb.toString();
    }

    /**
     * Escapes a field for CSV format (handles commas, quotes, newlines).
     */
    private String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        
        // If field contains comma, quote, or newline, wrap in quotes and escape internal quotes
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        
        return field;
    }

    /**
     * Formats LocalDateTime for CSV.
     */
    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DATE_FORMATTER);
    }

    /**
     * Gets the path to the CSV file.
     */
    public String getCsvFilePath() {
        return csvFilePath;
    }
}

package com.library.infrastructure.persistence;

import com.library.domain.model.Book;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for file storage operations.
 * Handles CSV export and file reading operations.
 * Pure OOP implementation without any framework dependencies.
 */
public class FileStorageUtil {

    private static final String BOOK_FILE = "books.csv";

    /**
     * Saves books to a CSV file.
     * @param books The list of books to save
     */
    public static void saveBooksToCSV(List<Book> books) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOK_FILE))) {
            writer.println("ISBN,Title,Authors,Categories,Available,Copies");
            for (Book b : books) {
                // Get first author name or "Unknown"
                String authors = b.getAuthors().stream()
                    .map(a -> a.getName())
                    .collect(Collectors.joining("; "));
                if (authors.isEmpty()) {
                    authors = "Unknown";
                }

                // Get category names or "Uncategorized"
                String categories = b.getCategories().stream()
                    .map(c -> c.getName())
                    .collect(Collectors.joining("; "));
                if (categories.isEmpty()) {
                    categories = "Uncategorized";
                }

                writer.printf("%s,%s,%s,%s,%s,%s%n",
                        b.getIsbn().getValue(),
                        escapeCSV(b.getTitle()),
                        escapeCSV(authors),
                        escapeCSV(categories),
                        b.isAvailable(),
                        b.getAvailableCopies() != null ? b.getAvailableCopies() : 0);
            }
            System.out.println("✓ Books saved to " + BOOK_FILE);
        } catch (IOException e) {
            System.err.println("✗ Error saving to CSV: " + e.getMessage());
        }
    }

    /**
     * Reads raw lines from a file.
     * @param filePath The path to the file
     * @return A list of lines from the file (excluding header)
     */
    public static List<String> readRawLines(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
            System.out.println("✓ Read " + lines.size() + " lines from " + filePath);
        } catch (FileNotFoundException e) {
            System.err.println("✗ File not found: " + filePath);
        } catch (IOException e) {
            System.err.println("✗ Error reading file: " + e.getMessage());
        }
        return lines;
    }

    /**
     * Escapes special characters in CSV values.
     * @param value The value to escape
     * @return The escaped value
     */
    private static String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    /**
     * Writes content to a file.
     * @param filePath The path to the file
     * @param content The content to write
     */
    public static void writeToFile(String filePath, String content) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.print(content);
            System.out.println("✓ Content written to " + filePath);
        } catch (IOException e) {
            System.err.println("✗ Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Reads entire file content as a string.
     * @param filePath The path to the file
     * @return The file content
     */
    public static String readFileAsString(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            System.err.println("✗ File not found: " + filePath);
        } catch (IOException e) {
            System.err.println("✗ Error reading file: " + e.getMessage());
        }
        return content.toString();
    }
}

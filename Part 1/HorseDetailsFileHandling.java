import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles storage and retrieval of horse details in CSV format.
 * Provides methods for saving, counting, retrieving, and displaying horse data
 * with comprehensive error handling and data validation.
 * @author Peter Bojthe
 * @version 16/04/25
 */
public class HorseDetailsFileHandling {
    private static final String FILE_NAME = "SavingHorseDetails.csv";
    private static final int EXPECTED_COLUMNS = 6;
    
    /**
     * Saves horse details to the CSV file in append mode after validating parameters
     * and ensuring the horse name doesn't already exist in the file. If the horse
     * name exists, the method returns without making any changes.
     *
     * @param name The name of the horse (cannot be null or empty)
     * @param confidence The confidence score (0.0-1.0 scale)
     * @param character The character symbol representing the horse
     * @param win Number of wins (must be >= 0)
     * @param total Total races (must be >= wins)
     * @param winRate Win percentage (0.0-1.0 scale)
     * @throws IOException If an I/O error occurs
     * @throws IllegalArgumentException If any parameter is invalid
     */
    public static void saveHorseDetails(String name, double confidence, char character, int win, int total, double winRate) throws IOException {
        // Parameter validation
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Horse name cannot be null or empty");
        }
        if (confidence < 0.0 || confidence > 1.0) {
            throw new IllegalArgumentException("Confidence must be between 0.0 and 1.0");
        }
        if (win < 0 || total < win) {
            throw new IllegalArgumentException("Invalid win/total race counts");
        }
        if (winRate < 0.0 || winRate > 1.0) {
            throw new IllegalArgumentException("Win rate must be between 0.0 and 1.0");
        }

        // Check if horse name already exists
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 0 && parts[0].equalsIgnoreCase(name)) {
                        return; // Horse already exists - silent return
                    }
                }
            }
        }

        // Save the new horse
        try (PrintWriter horseFile = new PrintWriter(new FileWriter(file, true))) {
            horseFile.printf("%s,%.2f,%c,%d,%d,%.2f%n",name, confidence, character, win, total, winRate);
        }
    }

    /**
     * Update the saved horse details after every race
     * Finds horse based on name (name is unique) and updates all of its details
     * @param horse The horse which will be updated in the file... IF it is in the file already.
     */
    public static void updateHorseInFile(Horse horse) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            // Read header first
            String header = reader.readLine();
            if (header != null) {
                lines.add(header);
            }
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[0].equalsIgnoreCase(horse.getName())) {
                    // Found the horse - replace with updated details
                    String updatedLine = String.format("%s,%.2f,%s,%d,%d,%.2f",
                        horse.getName(),
                        horse.getConfidence(),
                        horse.getSymbol(),
                        horse.getTotalWins(),
                        horse.getTotalRaces(),
                        horse.getWinRate());
                    lines.add(updatedLine);
                } else {
                    lines.add(line);
                }
            }
        }
        
        // Write all lines back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String fileLine : lines) {
                writer.write(fileLine);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Counts the number of records in the horse details file.
     *
     * @return Number of records (0 if file doesn't exist or is empty, or no horses saved)
     * @throws IOException If an I/O error occurs while reading
     */
    public static int countFileLines() throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists() || file.length() == 0) {
            return 0;
        }

        int lineNumber = -1; // Start at -1 to ignore initiall line. 
        try (BufferedReader horseFile = new BufferedReader(new FileReader(file))) {
            while (horseFile.readLine() != null) {
                lineNumber++;
            }
        }
        return lineNumber;
    }

    /**
     * Retrieves horse details from a specific line in the file.
     *
     * @param lineNumber The 1-based line number to retrieve
     * @return String array containing horse details, or null if line doesn't exist
     * validation happens before method call.
     * (invalid line number will not be passed to this method)
     * @throws IOException If an I/O error occurs
     * @throws IllegalArgumentException If lineNumber is invalid
     */
    public static String[] getHorseDetails(int lineNumber) throws IOException {
        if (lineNumber < 1) {
            throw new IllegalArgumentException("Line number must be positive");
        }

        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return null;
        }

        try (BufferedReader horseFile = new BufferedReader(new FileReader(file))) {
            String line;
            int currentLine = 0;
            while ((line = horseFile.readLine()) != null) {
                if (currentLine++ == lineNumber) {
                    return validateDataRow(line.split(",", -1));
                }
            }
        }
        return null;
    }

    /**
     * Reads all horse details from the CSV file.
     *
     * @return List of String arrays where each array represents a horse record
     * @throws IOException If an I/O error occurs while reading
     */
    public static List<String[]> readCSV() throws IOException {
        List<String[]> data = new ArrayList<>();
        File file = new File(FILE_NAME);
        
        if (!file.exists() || file.length() == 0) {
            return data;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",", -1); // Keep empty values
                data.add(validateDataRow(row));
            }
        }
        return data;
    }

    /**
     * Displays all horse details in a formatted table with borders.
     * provides clear error messages if error happens
     */
    public static void printFormattedTable() {
        try {
            List<String[]> data = readCSV();
            
            if (data.isEmpty()) {
                System.out.println("No horse data available.");
                return;
            }

            // Verify all rows have expected columns
            for (String[] row : data) {
                if (row.length != EXPECTED_COLUMNS) {
                    System.err.println("Error: Malformed data found. Expected " + 
                                     EXPECTED_COLUMNS + " columns but found " + row.length);
                    return;
                }
            }

            // Calculate column widths
            int[] colWidths = new int[EXPECTED_COLUMNS];
            for (String[] row : data) {
                for (int i = 0; i < EXPECTED_COLUMNS; i++) {
                    colWidths[i] = Math.max(colWidths[i], row[i].length());
                }
            }
            
            // Print table
            printSeparatorLine(colWidths);
            printTableRow(data.get(0), colWidths); // Header row
            printSeparatorLine(colWidths);
            
            for (int i = 1; i < data.size(); i++) {
                printTableRow(data.get(i), colWidths);
                printSeparatorLine(colWidths);
            }
        } catch (IOException e) {
            System.err.println("Error reading horse data: " + e.getMessage());
        }
    }

    /**
     * Validates a data row to ensure it has the correct number of columns.
     *
     * @param row The data row to validate
     * @return The validated row array
     * @throws IllegalArgumentException If row has incorrect number of columns
     */
    private static String[] validateDataRow(String[] row) {
        if (row.length != EXPECTED_COLUMNS) {
            throw new IllegalArgumentException(
                String.format("Invalid data format. Expected %d columns, found %d", 
                EXPECTED_COLUMNS, row.length));
        }
        return row;
    }

    /**
     * Prints a formatted table row with proper alignment.
     *
     * @param row The data row to print
     * @param colWidths Array of column widths
     */
    private static void printTableRow(String[] row, int[] colWidths) {
        System.out.print("|");
        for (int i = 0; i < row.length; i++) {
            String value = row[i].trim();
            if (value.matches("[A-Za-z0-9]+")) { // Alphanumeric value
                System.out.printf(" %" + colWidths[i] + "s |", value);
            } else { // String value
                System.out.printf(" %-" + colWidths[i] + "s |", value);
            }
        }
        System.out.println();
    }

    /**
     * Prints a separator line for the table.
     *
     * @param colWidths Array of column widths
     */
    private static void printSeparatorLine(int[] colWidths) {
        System.out.print("+");
        for (int width : colWidths) {
            System.out.print("-".repeat(width + 2) + "+");
        }
        System.out.println();
    }

    /**
     * Returns the filename used for storing horse details.
     *
     * @return The CSV filename
     */
    public static String getFileName() {
        return FILE_NAME;
    }
}
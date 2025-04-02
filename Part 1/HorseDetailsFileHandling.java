import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for handling horse details storage and retrieval in CSV format.
 * Provides methods for saving, counting, retrieving, and displaying horse data.
 */
public class HorseDetailsFileHandling {
    /** The name of the CSV file used for storing horse details */
    private static final String FILE_NAME = "SavingHorseDetails.csv";
    
    /**
     * Saves horse details to the CSV file in append mode.
     *
     * @param name The name of the horse
     * @param confidence The confidence score of the horse (0-1 scale)
     * @param character The character symbol representing the horse
     * @param win Number of wins
     * @param total Total number of races
     * @param winRate Win rate (wins/total)
     * @throws IOException If an I/O error occurs while writing to the file
     */
    public static void saveHorseDetails(String name, double confidence, char character, 
                                      int win, int total, double winRate) throws IOException {
        try (PrintWriter horseFile = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            horseFile.printf("%s,%.2f,%c,%d,%d,%.2f%n", 
                          name, confidence, character, win, total, winRate);
        }
    }

    /**
     * Counts the number of lines (records) in the horse details file.
     *
     * @return The number of lines in the file, 0 if file doesn't exist
     * @throws IOException If an I/O error occurs while reading the file
     */
    public static int countFileLines() throws IOException {
        int lineNumber = 0;
        File file = new File(FILE_NAME);
        
        if (!file.exists()) {
            return 0;
        }

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
     * @param givenLineNumber The line number to retrieve (1-based index)
     * @return String array containing the horse details, or null if line doesn't exist
     * @throws IOException If an I/O error occurs while reading the file
     */
    public static String[] getHorseDetails(int givenLineNumber) throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return null;
        }

        try (BufferedReader horseFile = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 1;
            while ((line = horseFile.readLine()) != null) {
                if (lineNumber == givenLineNumber) {
                    return line.split(",");
                }
                lineNumber++;
            }
        }
        return null;
    }

    /**
     * Reads all horse details from the CSV file into memory.
     *
     * @return List of String arrays where each array represents a horse record
     */
    public static List<String[]> readCSV() {
        List<String[]> data = new ArrayList<>();
        File file = new File(FILE_NAME);
        
        if (!file.exists() || file.length() == 0) {
            return data;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split on commas but ignore commas inside quotes
                String[] row = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                // Remove surrounding quotes if present
                for (int i = 0; i < row.length; i++) {
                    row[i] = row[i].replaceAll("^\"|\"$", "").trim();
                }
                data.add(row);
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
        
        return data;
    }

    /**
     * Displays all horse details in a formatted table with borders.
     * Automatically detects column widths and aligns numbers to the right,
     * strings to the left. Includes header row and separator lines.
     */
    public static void printFormattedTable() {
        List<String[]> data = readCSV();
        
        if (data.isEmpty()) {
            System.out.println("No horse data available.");
            return;
        }

        // Determine maximum width for each column
        int[] colWidths = new int[data.get(0).length];
        for (String[] row : data) {
            for (int i = 0; i < row.length; i++) {
                colWidths[i] = Math.max(colWidths[i], row[i].length());
            }
        }
        
        // Print top border
        printSeparatorLine(colWidths);
        
        // Print header row (first row in data)
        System.out.print("|");
        for (int i = 0; i < data.get(0).length; i++) {
            System.out.printf(" %-" + colWidths[i] + "s |", data.get(0)[i]);
        }
        System.out.println();
        printSeparatorLine(colWidths);
        
        // Print data rows (skip header if it exists)
        for (int rowNum = 1; rowNum < data.size(); rowNum++) {
            String[] row = data.get(rowNum);
            System.out.print("|");
            for (int i = 0; i < row.length; i++) {
                if (row[i].matches("-?\\d+(\\.\\d+)?")) { // Number
                    System.out.printf(" %" + colWidths[i] + "s |", row[i]);
                } else { // String
                    System.out.printf(" %-" + colWidths[i] + "s |", row[i]);
                }
            }
            System.out.println();
            printSeparatorLine(colWidths);
        }
    }
    
    /**
     * Prints a separator line for the table based on column widths.
     *
     * @param colWidths Array of integers representing each column's width
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
     * @return The CSV filename as a String
     */
    public static String getFileName() {
        return FILE_NAME;
    }
}
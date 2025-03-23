import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class HorseDetailsFileHandling {

    static final String FILE_NAME = "SavingHorseDetails.csv";

    @SuppressWarnings(FILE_NAME)
    public static void saveHorseDetails(String name, double confidence, char character) throws IOException {
        try (PrintWriter horseFile = new PrintWriter(FILE_NAME)) {
            horseFile.println(name + "," + confidence + "," + character);
        }
    }

    public String[] getHorseDetails(int givenLineNumber) throws IOException {
        int lineNumber = 1;
        try (BufferedReader horseFile = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = horseFile.readLine()) != null) {
                if (lineNumber == givenLineNumber) {
                    return line.split(",");
                }
                lineNumber++;
            }
        }
        return null;
    }

    public static String getFileName() {
        return FILE_NAME;
    }
}
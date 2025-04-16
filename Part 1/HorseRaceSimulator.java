import java.io.IOException;

/**
 * The main class and main method to run the program.
 * Start a race with given distance passed to Race class
 * 
 * @author Peter Bojthe
 * @version 16/04/25
 */
public class HorseRaceSimulator {
    public static void main(String[] args) throws IOException {
        Race race = new Race();
        race.startRace();
    }
}
/**
 * The main class and main method to run the program.
 * Start a race with given distance passed to Race class
 * 
 * 
 * @author Peter Bojthe 
 * @version 18/03/25
 */

public class HorseRaceSimulator {
    public static void main(String[] args) {
        Race race = new Race(25);
        race.startRace();
    }
}

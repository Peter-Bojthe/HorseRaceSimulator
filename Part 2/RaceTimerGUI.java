/**
 * The class to time each race
 * 
 * @author Peter Bojthe
 * @version 1.0.0
 */
public class RaceTimerGUI {
    private long raceStartTime;
    private long raceEndTime;
    private boolean running = false;

    /** @return elapsed time in milliseconds */
    public long getElapsedMillis() { return raceEndTime - raceStartTime; }

    /** @return elapsed time in seconds */
    public double getElapsedSeconds() { return getElapsedMillis() / 1000.0; }

    /** Starts the race timer */
    public void start() {
        raceStartTime = System.currentTimeMillis();
        running = true;
    }

    /** Stops the race timer */
    public void stop() {
        if (running) {
            raceEndTime = System.currentTimeMillis();
            running = false;
        }
    }

    /** @return formatted string of the elapsed time */
    public String getFormattedTime() {
        long millis = getElapsedMillis();
        long seconds = millis / 1000;
        long ms = millis % 1000;
        return String.format("%d.%03d seconds", seconds, ms);
    }
}

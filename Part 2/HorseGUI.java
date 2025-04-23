/**
 * Represents a horse participating in the race.
 * 
 * @author Peter Bojthe
 * @version 1.0.2
 */
public class HorseGUI {
    private final String symbol;             // Symbol representing the horse in the race display
    private final String name;             // Name of the horse (immutable after creation)
    private final double confidence;       // Confidence level of the horse (0.0 to 1.0)
    private int distance;                  // Distance the horse has traveled in the race
    private boolean fallen;                // Flag indicating if the horse has fallen
    private final int lane;                // Lane number where the horse is racing    
    private int lapsCompleted;             // Laps required for Oval Track

    /**
     * Constructs a new horse.
     * 
     * @param name horse's name
     * @param symbol character symbol for the horse
     * @param confidence how likely the horse is to move
     * @param lane track lane the horse is in
     */
    public HorseGUI(String name, String symbol, double confidence, int lane) {
        this.name = name;
        this.symbol = symbol;
        this.confidence = confidence;
        this.lane = lane;
        this.distance = 0;
        this.fallen = false;
        this.lapsCompleted = 0;
    }

    /** Advances the horse forward by 1 unit. */
    public void moveForward() { distance++; }

    /** Marks a lap completed (for oval tracks). */
    public void completeLap() { lapsCompleted++; }

    /** Marks the horse as fallen. */
    public void fall() { fallen = true; }

    /** @return the horse's name */
    public String getName() { return name; }

    /** @return the symbol representing the horse */
    public String getSymbol() { return symbol; }

    /** @return the confidence level (0.0–1.0) */
    public double getConfidence() { return confidence; }

    /** @return the assigned track lane */
    public int getLane() { return lane; }

    /** @return how far the horse has traveled */
    public int getDistance() { return distance; }

    /** @return true if the horse has fallen */
    public boolean hasFallen() { return fallen; }

    /** @return number of laps completed (for oval tracks) */
    public int getLapsCompleted() { return lapsCompleted; }
}
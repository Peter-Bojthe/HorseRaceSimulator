/**
 * Represents a horse participating in the race.
 * 
 * @author Peter Bojthe
 * @version 1.0.3
 */
public class HorseGUI {
    private final String symbol;           // Symbol representing the horse in the race display
    private final String name;             // Name of the horse (immutable after creation)
    private final double confidence;       // Confidence level of the horse (0.0 to 1.0)
    private int distance;                  // Distance the horse has traveled in the race
    private boolean fallen;                // Flag indicating if the horse has fallen
    private final int lane;                // Lane number where the horse is racing    
    private int lapsCompleted;             // Laps required for Oval Track

    // Attributes affecting confidence
    private String breed;                  // Breed of the horse
    private String coatColour;             // Fur colour of the horse
    private String saddle;                 // Saddle on the horse
    private String shoes;                  // Shoes the horse has

    /**
     * Constructs a new horse.
     * 
     * @param name horse's name
     * @param symbol character symbol for the horse
     * @param confidence how likely the horse is to move
     * @param lane track lane the horse is in
     */
    public HorseGUI(String name, String symbol, double confidence, int lane, String breed, String coatColour, String saddle, String shoes) {
        this.name = name;
        this.symbol = symbol;
        this.confidence = confidence;
        this.lane = lane;
        this.breed = breed;
        this.coatColour = coatColour;
        this.saddle = saddle;
        this.shoes = shoes;

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

    /** @return breed of the horse */
    public String getBreed() { return breed; }

    /** Sets the breed of the horse */
    public void setBreed(String breed) { this.breed = breed; }

    /** @return the coat colour of the horse */
    public String getCoatColour() { return coatColour; }

    /** sets the coat colour of the horse */
    public void setCoatColour(String coatColour) { this.coatColour = coatColour; }

    /** @return the type of saddle on the horse */
    public String getSaddle() { return saddle; }

    /** sets the type of saddle on the horse */
    public void setSaddle(String saddle) { this.saddle = saddle; }

    /** @return the type of shoes on the horse */
    public String getShoes() { return shoes; }

    /** sets the type of shoes on the horse */
    public void setShoes(String shoes) { this.shoes = shoes; }
}
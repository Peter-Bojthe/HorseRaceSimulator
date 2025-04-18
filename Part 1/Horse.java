/**
 * Represents a horse in a race simulation.
 * Each horse has a name, a character symbol, a confidence rating, and a distance traveled.
 * The confidence rating affects the horse's speed and likelihood of falling.
 * Winning increases confidence, while falling decreases it.
 * 
 * @author Peter Bojthe
 * @version 16/04/25
 */

public class Horse {
    // Fields of Horse
    private char horseSymbol;              // The character symbol representing the horse
    private final String horseName;        // The name of the horse
    private double horseConfidence;        // The confidence rating of the horse (0.0 to 1.0)
    private int horseDistance;             // The distance the horse has traveled in the race
    private boolean horseFallen;           // Flag indicating whether the horse has fallen
    private int laneNumber;                // The lane number assigned to the horse
    private int totalRaces;                // Counter for the total number of races
    private int totalWins;                 // Counter for the number of wins
    private double winRate;                // total wins divided by the total races
    private double winnings = 0.0;         // the money won by the user if the horse wins (initially 0)
    private boolean betPlacedOn = false;   // if the user has put a bet on a horse then true

    // Fields of Class Horse
    static int horseCounter;           // Static counter to track the number of horses created

    /**
     * Constructor for objects of class Horse.
     * Initializes the horse with a symbol, name, confidence rating, and lane number.
     * The distance traveled is set to 0, and the horse starts as not fallen.
     * Confidence is clamped between 0.0 and 1.0.
     *
     * @param horseSymbol    the character symbol representing the horse.
     * @param horseName      the name of the horse.
     * @param horseConfidence the confidence rating of the horse (0.0 to 1.0).
     * @param laneNumber     the lane number assigned to the horse.
     */
    public Horse(String horseName, double horseConfidence, char horseSymbol, int totalWins, int totalRaces, double winRate, int laneNumber) {
        this.horseName = horseName;
        // Ensure confidence is within the valid range [0.0, 1.0]
        if (horseConfidence > 1.0) {
            this.horseConfidence = 1.0;
        } else if (horseConfidence < 0.0) {
            this.horseConfidence = 0.0;
        } else {
            this.horseConfidence = horseConfidence;
        }
        this.horseSymbol = horseSymbol;
        this.totalWins = totalWins;
        this.totalRaces = totalRaces;
        if (this.totalRaces == 0) {
            this.winRate = 0;
        } else {
            this.winRate = totalWins / totalRaces;
        }
        this.laneNumber = laneNumber;

        this.horseDistance = 0;
        this.horseFallen = false;
        horseCounter++;
    }

    /**
     * Constructs a Horse object from file-derived string data.
     * This constructor is specifically designed for instantiating horses
     * from persisted file storage, where all attributes are stored as strings.
     * Performs data validation and type conversion while initializing all horse properties.
     *
     * @param horseName String representation of the horse's unique identifier
     * @param horseConfidence String representation of confidence value (0.0-1.0)
     * @param horseSymbol String containing single character visual representation
     * @param totalWins String representation of lifetime victory count
     * @param totalRaces String representation of lifetime race participation
     * @param winRate String representation of win percentage (0.0-1.0)
     */
    public Horse(String horseName, String horseConfidence, String horseSymbol, String totalWins, String totalRaces, String winRate, int lane) {
        this.horseName = horseName;
        // Ensure confidence is within the valid range [0.0, 1.0]
        if (Double.parseDouble(horseConfidence) > 1.0) {
            this.horseConfidence = 1.0;
        } else if (Double.parseDouble(horseConfidence) < 0.0) {
            this.horseConfidence = 0.0;
        } else {
            this.horseConfidence = Double.parseDouble(horseConfidence);
        }
        this.horseSymbol = horseSymbol.charAt(0);
        this.totalWins = Integer.parseInt(totalWins);
        this.totalRaces = Integer.parseInt(totalRaces);
        if (this.totalRaces == 0) {
            this.winRate = 0;
        } else {
            this.winRate = Double.parseDouble(winRate);
        }
        this.laneNumber = lane;

        this.horseDistance = 0;
        this.horseFallen = false;
        horseCounter++; 
    }

    /**
     * Marks the horse as fallen.
     */
    public void fall() {
        this.horseFallen = true;
    }

    /**
     * Returns the lane number assigned to the horse.
     *
     * @return the lane number of the horse.
     */
    public int getLaneNumber() {
        return this.laneNumber;
    }

    /**
     * Sets the lane number for the horse.
     *
     * @param newLane the new lane number to assign to the horse.
     */
    public void setLaneNumber(int newLane) {
        this.laneNumber = newLane;
    }

    /**
     * Returns the confidence rating of the horse.
     *
     * @return the confidence rating of the horse (0.0 to 1.0).
     */
    public double getConfidence() {
        return this.horseConfidence;
    }

    /**
     * Returns the distance the horse has traveled.
     *
     * @return the distance traveled by the horse.
     */
    public int getDistanceTravelled() {
        return this.horseDistance;
    }

    /**
     * Returns the name of the horse.
     *
     * @return the name of the horse.
     */
    public String getName() {
        return this.horseName;
    }

    /**
     * Returns the character symbol representing the horse.
     *
     * @return the symbol of the horse.
     */
    public char getSymbol() {
        return this.horseSymbol;
    }

    /**
     * Resets the horse's position to the start of the race.
     * The distance traveled is set to 0, and the horse is marked as not fallen.
     */
    public void goBackToStart() {
        this.horseDistance = 0;
        this.horseFallen = false;
    }

    /**
     * Determines whether the horse has fallen during the race.
     * This state affects the horse's ability to continue moving forward.
     * 
     * @return true if the horse is in a fallen state, false if the horse remains upright
     */
    public boolean hasFallen() {
        return this.horseFallen;
    }

    /**
     * Advances the horse's position by one unit along the race track.
     * This method unconditionally increments the distance regardless of the horse's state.
     * For race simulation integrity, callers should typically check hasFallen() first.
     */
    public void moveForward() {
        this.horseDistance += 1;  // Using compound assignment for clarity
    }

    /**
     * Updates the horse's confidence rating with value
     * Confidence values are constrained to the range [0.0, 1.0] and rounded to 2 decimal places
     * for consistent simulation behavior, out-of-range values are automatically clamped
     * to the nearest valid value
     * 
     * @param newConfidence The new confidence level
     */
    public void setConfidence(double newConfidence) {   
        // Clamp value to valid range with informative messaging
        if (newConfidence > 1.0) {
            this.horseConfidence = 1.0;
        } else if (newConfidence < 0.0) {
            this.horseConfidence = 0.0;
        } else {
            // Round to 2 decimal places for consistency in simulation calculations
            this.horseConfidence = Math.round(newConfidence * 100.0) / 100.0;
        }
    }

    /**
     * Updates the visual representation symbol for the horse.
     * The symbol should be a single Unicode character that will be displayed
     * 
     * @param newSymbol The character to use for race display purposes
     */
    public void setSymbol(char newSymbol) {
        this.horseSymbol = newSymbol;
    }

    /**
     * Retrieves the cumulative count of races won by this horse.
     * 
     * @return Total wins as a non-negative integer
     */
    public int getTotalWins() {
        return totalWins;
    }

    /**
     * Updates the horse's lifetime win count.
     * 
     * @param totalWins New win count (must be non-negative)
     * @throws IllegalArgumentException if negative value is provided
     */
    public void setTotalWins(int totalWins) {
        if (totalWins < 0) {
            throw new IllegalArgumentException("Win count cannot be negative");
        }
        this.totalWins = totalWins;
    }

    /**
     * Retrieves the cumulative count of races participated in by this horse.
     * 
     * @return Total races as a non-negative integer
     */
    public int getTotalRaces() {
        return totalRaces;
    }

    /**
     * Updates the horse's lifetime race participation count.
     * 
     * @param totalRaces New race count (must be non-negative and ≥ totalWins)
     * @throws IllegalArgumentException if invalid count is provided
     */
    public void setTotalRaces(int totalRaces) {
        if (totalRaces < 0) {
            throw new IllegalArgumentException("Race count cannot be negative");
        }
        if (totalRaces < this.totalWins) {
            throw new IllegalArgumentException("Race count cannot be less than win count");
        }
        this.totalRaces = totalRaces;
    }

    /**
     * Calculates and updates the horse's win rate based on current statistics.
     * Win rate is computed as wins divided by total races, with protection against
     * division by zero (returns 0.0 when no races completed).
     * 
     * @param wins Number of wins (must be <= total races and non-negative)
     * @param total Total races (must be >= wins and non-negative)
     */
    public void setWinRate(double wins, double total) {
        if (wins < 0 || total < 0) {
            throw new IllegalArgumentException("Values cannot be negative");
        }
        if (wins > total) {
            throw new IllegalArgumentException("Wins cannot exceed total races");
        }
        this.winRate = (total == 0) ? 0.0 : wins / total;
    }

    /**
     * Retrieves the current win rate percentage.
     * 
     * @return Win rate as a decimal between 0.0 (0%) and 1.0 (100%)
     */
    public double getWinRate() {
        return this.winRate;
    }

    public double getWinnings() {
        return winnings;
    }

    public void setWinnings(double winnings) {
        this.winnings = Math.round(winnings * 100.0) / 100.0;
    }

    public boolean isBetPlacedOn() {
        return betPlacedOn;
    }

    public void setBetPlacedOn(boolean betPlacedOn) {
        this.betPlacedOn = betPlacedOn;
    }
}
/**
 * Represents a horse in a race simulation.
 * Each horse has a name, a character symbol, a confidence rating, and a distance traveled.
 * The confidence rating affects the horse's speed and likelihood of falling.
 * Winning increases confidence, while falling decreases it.
 * 
 * @author Peter Bojthe
 * @version 23/03/25
 */

class Horse {
    // Fields of class Horse
    private char horseSymbol;          // The character symbol representing the horse
    private final String horseName;    // The name of the horse
    private double horseConfidence;    // The confidence rating of the horse (0.0 to 1.0)
    private int horseDistance;         // The distance the horse has traveled in the race
    private boolean horseFallen;       // Flag indicating whether the horse has fallen
    private int laneNumber;            // The lane number assigned to the horse
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
    public Horse(char horseSymbol, String horseName, double horseConfidence, int laneNumber) {
        this.horseSymbol = horseSymbol;
        this.horseName = horseName;
        this.horseDistance = 0;
        this.horseFallen = false;
        this.laneNumber = laneNumber;

        // Ensure confidence is within the valid range [0.0, 1.0]
        if (horseConfidence > 1) {
            this.horseConfidence = 1;
        } else if (horseConfidence < 0) {
            this.horseConfidence = 0;
        } else {
            this.horseConfidence = horseConfidence;
        }

        horseCounter++; // Increment the static horse counter
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
     * Checks if the horse has fallen.
     *
     * @return true if the horse has fallen, false otherwise.
     */
    public boolean hasFallen() {
        return this.horseFallen;
    }

    /**
     * Moves the horse forward by one unit.
     */
    public void moveForward() {
        this.horseDistance = this.horseDistance + 1;
    }

    /**
     * Sets the confidence rating of the horse.
     * The confidence is clamped between 0.0 and 1.0.
     *
     * @param newConfidence the new confidence rating to set (0.0 to 1.0).
     */
    public void setConfidence(double newConfidence) {
        if (newConfidence > 1) {
            System.out.println("Horse Confidence Must Be Between 0-1.");
            this.horseConfidence = 1;
            return;
        } else if (newConfidence < 0) {
            System.out.println("Horse Confidence Must Be Between 0-1.");
            this.horseConfidence = 0;
            return;
        }
        this.horseConfidence = Math.round(newConfidence * Math.pow(10, 3)) / Math.pow(10, 3);
    }

    /**
     * Sets the character symbol representing the horse.
     *
     * @param newSymbol the new character symbol to set.
     */
    public void setSymbol(char newSymbol) {
        this.horseSymbol = newSymbol;
    }
}
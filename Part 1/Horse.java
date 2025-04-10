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
    // Fields of Horse
    private char horseSymbol;          // The character symbol representing the horse
    private final String horseName;    // The name of the horse
    private double horseConfidence;    // The confidence rating of the horse (0.0 to 1.0)
    private int horseDistance;         // The distance the horse has traveled in the race
    private boolean horseFallen;       // Flag indicating whether the horse has fallen
    private int laneNumber;            // The lane number assigned to the horse
    private int totalRaces;            // Counter for the total number of races
    private int totalWins;             // Counter for the number of wins
    private double winRate;            // total wins divided by the total races

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
        this.totalWins = 0;
        this.totalRaces = 0;
        this.winRate = 0.0; //calculate
        this.laneNumber = laneNumber;

        this.horseDistance = 0;
        this.horseFallen = false;
        horseCounter++; // Increment the static horse counter
    }

    /**
     * 
     * 
     *  Horse Details read in from file.
     * 
     * */

    public Horse(String horseName, String horseConfidence, String horseSymbol, String totalWins, String totalRaces, String winRate) {


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
        this.totalWins = 0;
        this.totalRaces = 0;
        this.winRate = 0; //calculate
        this.laneNumber = 1;

        this.horseDistance = 0;
        this.horseFallen = false;
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
        this.horseConfidence = Math.round(newConfidence * Math.pow(10, 2)) / Math.pow(10, 2);
    }

    /**
     * Sets the character symbol representing the horse.
     *
     * @param newSymbol the new character symbol to set.
     */
    public void setSymbol(char newSymbol) {
        this.horseSymbol = newSymbol;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public int getTotalRaces() {
        return totalRaces;
    }

    public void setTotalRaces(int totalRaces) {
        this.totalRaces = totalRaces;
    }

    public double getWinRate() {
        return winRate;
    }

    public void setWinRate(double wins, double total) {
        this.winRate = wins / total;
    }
}
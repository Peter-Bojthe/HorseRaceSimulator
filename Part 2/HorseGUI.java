/**
 * Represents a horse participating in the race.
 * 
 * @author Peter Bojthe
 * @version 1.0.5
 */
public class HorseGUI {
    // final variables
    private final String symbol;           // Symbol representing the horse in the race display
    private final String name;             // Name of the horse (immutable after creation)
    private final int lane;                // Lane number where the horse is racing 

    // Race variables
    private int distance;                  // Distance the horse has traveled in the race
    private boolean fallen;                // Flag indicating if the horse has fallen  
    private int lapsCompleted;             // Laps required for Oval Track
    private double confidence;             // Confidence level of the horse (0.0 to 1.0)

    // Horse Success Attributes
    private double winRate;                // the win-rate of the horse
    private int wins;                      // total number of wins
    private int races;                     // total number of races
    private double averageSpeed;           // average speed of the horse from all races

    // Betting Attributes
    private boolean betPlaced;             // flag to check if user has placeda bet on this horse
    private double winnings;               // amount of money the horse could win

    // Attributes affecting confidence
    private String breed;                  // Breed of the horse
    private String coatColour;             // Fur/ coat colour of the horse
    private String saddle;                 // Saddle on the horse
    private String shoes;                  // Shoes the horse has

    /**
     * Constructor to make a horse
     * 
     * @param name           // The name of the horse   
     * @param symbol         // The symbol for the horse
     * @param confidence     // The confidence rating of the horse 0-1
     * @param lane           // The lane of the horse 
     * @param breed          // ThE breed of the horse
     * @param coatColour     // The coat/ fur colour of the horse
     * @param saddle         // The type of saddle on the horse
     * @param shoes          // The type of shoes
     * @param wins           // Total wins of the horse
     * @param races          // Total races the horse has been in
     * @param averageSpeed   // The average speed of the horse from all races
     */
    public HorseGUI(String name, String symbol, double confidence, int lane, String breed, String coatColour, String saddle, String shoes, int wins, int races, double averageSpeed) {
        this.name = name;
        this.symbol = symbol;
        this.confidence = confidence;
        this.lane = lane;
        this.breed = breed;
        this.coatColour = coatColour;
        this.saddle = saddle;
        this.shoes = shoes;
        this.winRate = calculateWinRate(wins, races);
        this.wins = wins;
        this.races = races;
        this.averageSpeed = averageSpeed;

        this.betPlaced = false;
        this.winnings = 0.0;
        this.distance = 0;
        this.fallen = false;
        this.lapsCompleted = 0;
    }

    /**
     * calculates the win rate
     * @param wins total wins by the horse
     * @param races total races by the horse
     * @return the win rate of the horse
     */
    public static double calculateWinRate(int wins, int races) {
        if (races == 0) { return 0.0; }
        return Math.round(wins*100.0) / (100.0*races);
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

    /** set the confidence of the horse @param newConfidence is the new confidence */
    public void setConfidence(double newConfidence) { this.confidence = newConfidence; }

    /** @return the assigned track lane */
    public int getLane() { return lane; }

    /** @return how far the horse has traveled */
    public int getDistance() { return distance; }

    /** reset horse distance */
    public void resetDistance() { this.distance = 0; }

    /** reset number of laps */
    public void resetLaps() { this.lapsCompleted = 0; }

    /** reset if the horse has fell */
    public void resetFall() { this.fallen = false; }

    /** @return true if the horse has fallen */
    public boolean hasFallen() { return fallen; }

    /** @return number of laps completed (for oval tracks) */
    public int getLapsCompleted() { return lapsCompleted; }

    /** @return breed of the horse */
    public String getBreed() { return breed; }

    /** Sets the breed of the horse @param breed of the horse */
    public void setBreed(String breed) { this.breed = breed; }

    /** @return the coat colour of the horse */
    public String getCoatColour() { return coatColour; }

    /** sets the coat colour of the horse @param coatColour colour of the horse */
    public void setCoatColour(String coatColour) { this.coatColour = coatColour; }

    /** @return the type of saddle on the horse */
    public String getSaddle() { return saddle; }

    /** sets the type of saddle on the horse @param saddle saddle on the horse */
    public void setSaddle(String saddle) { this.saddle = saddle; }

    /** @return the type of shoes on the horse */
    public String getShoes() { return shoes; }

    /** sets the type of shoes on the horse @param shoes shoes accessory */
    public void setShoes(String shoes) { this.shoes = shoes; }

    /** @return the win rate of the horse */
    public double getWinRate() { return winRate; }

    /** sets the win rate of the horse @param wins total wins @param races total races */
    public void setWinRate(int wins, int races) { this.winRate = Math.round(wins*100.0)/(100.0*races); }

    /** @return the number of wins the horse has */
    public int getWins() { return wins; }

    /** sets the number of wins the horse has @param wins total wins the horse has */
    public void setWins(int wins) { this.wins = wins; }

    /** @return the number of races the horse has had */
    public int getRaces() { return races; }

    /** sets the number of races the horse has had @param races total races the horse has */
    public void setRaces(int races) { this.races = races; }

    /** @return true if this horse has had a bet placed on it */
    public boolean isBetPlaced() { return betPlaced; }

    /** set to true if this horse has been bet on @param betPlaced flag which checks which horse has been bet on */
    public void setBetPlaced(boolean betPlaced) { this.betPlaced = betPlaced; }

    /** @return winnings the horse has earned for winning when it has been bet on */
    public double getWinnings() { return winnings; }

    /** sets the winnings of the horse @param winnings amount the horse can win */
    public void setWinnings(double winnings) { this.winnings = winnings; }

    /** @return the average speed of the horse */
    public double getAverageSpeed() { return this.averageSpeed; }

    /** set the average speed of the horse */
    public void setAverageSpeed(double averageSpeed) { this.averageSpeed = calculateAverageSpeed(averageSpeed); }

    /** @return the average speed based of previous average and a new recording and @param newSpeed is the new recording */
    private double calculateAverageSpeed(double newSpeed) { return ((this.averageSpeed*this.races)+newSpeed)/(this.races+1); }
}
/**
 * Represents a horse
 * 
 * @author Peter Bojthe
 * @version 1.0.0
 */
public class HorseGUI {
    private char horseSymbol;              // Symbol representing the horse in the race display
    private final String horseName;        // Name of the horse (immutable after creation)
    private double horseConfidence;        // Confidence level of the horse (0.0 to 1.0)
    private int horseDistance;             // Distance the horse has traveled in the race
    private boolean horseFallen;           // Flag indicating if the horse has fallen
    private int laneNumber;                // Lane number where the horse is racing

    /**
     * Constructs a new HorseGUI instance with specified attributes.
     * 
     * @param name The name of the horse
     * @param symbol The character symbol representing the horse
     * @param confidence The initial confidence level (0.0 to 1.0)
     * @param lane The lane number where the horse will race
     */
    public HorseGUI(String name, char symbol, double confidence, int lane) {
        this.horseName = name;
        this.horseSymbol = symbol;
        this.horseConfidence = confidence;
        this.laneNumber = lane;

        this.horseDistance = 0;
        this.horseFallen = false;
    }

    /**
     * Gets the lane number where the horse is racing.
     * 
     * @return The lane number of this horse
     */
    public int getLaneNumber() {
        return laneNumber;
    }

    /**
     * Sets the lane number for this horse.
     * 
     * @param laneNumber The new lane number to set
     */
    public void setLaneNumber(int laneNumber) {
        this.laneNumber = laneNumber;
    }

    /**
     * Checks if the horse has fallen during the race.
     * 
     * @return true if the horse has fallen, false otherwise
     */
    public boolean isHorseFallen() {
        return horseFallen;
    }

    /**
     * Sets the fallen status of the horse.
     * 
     * @param horseFallen true to mark the horse as fallen, false otherwise
     */
    public void setHorseFallen(boolean horseFallen) {
        this.horseFallen = horseFallen;
    }

    /**
     * Gets the distance the horse has traveled in the race.
     * 
     * @return The current distance traveled by the horse
     */
    public int getHorseDistance() {
        return horseDistance;
    }

    /**
     * Sets the distance the horse has traveled in the race.
     * 
     * @param horseDistance The new distance value to set
     */
    public void setHorseDistance(int horseDistance) {
        this.horseDistance = horseDistance;
    }

    /**
     * Gets the current confidence level of the horse.
     * 
     * @return The horse's confidence level (0.0 to 1.0)
     */
    public double getHorseConfidence() {
        return horseConfidence;
    }

    /**
     * Sets the confidence level of the horse.
     * 
     * @param horseConfidence The new confidence level (0.0 to 1.0)
     */
    public void setHorseConfidence(double horseConfidence) {
        this.horseConfidence = horseConfidence;
    }

    /**
     * Gets the name of the horse.
     * 
     * @return The horse's name
     */
    public String getHorseName() {
        return horseName;
    }

    /**
     * Gets the symbol representing the horse.
     * 
     * @return The horse's display symbol
     */
    public char getHorseSymbol() {
        return horseSymbol;
    }

    /**
     * Sets the symbol representing the horse.
     * 
     * @param horseSymbol The new character symbol for the horse
     */
    public void setHorseSymbol(char horseSymbol) {
        this.horseSymbol = horseSymbol;
    }
}
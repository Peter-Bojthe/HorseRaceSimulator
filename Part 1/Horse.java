/**
 * Write a description of class Horse here.
 * 
 * Every horse must have a name and a character.
 * They must also have a conficence rating 0-1.
 * Higher the confidence rating the faster the horse, but more likely to fall
 * They need to have a boolean flag to check if they have fell
 * Horse distance in whole numbers
 * Winning increases confidence, falling decreases it.
 * 
 * @author Peter Bojthe 
 * @version 17/03/25
 */

 class Horse {
    //Fields of class Horse
    private char horseSymbol;
    private final String horseName;
    private double horseConfidence;
    private int horseDistance;
    private boolean horseFallen;
    private int laneNumber;

    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     * Give Symbol
     * Give Name
     * Give Confidence
     * Set Distance and Fallen to deafult
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence, int laneNumber) {
       this.horseSymbol = horseSymbol;
       this.horseName = horseName;
       this.horseDistance = 0;
       this.horseFallen = false;
       this.laneNumber = laneNumber;
       if (horseConfidence > 1) {
        this.horseConfidence = 1;
       } else if (horseConfidence < 0) {
        this.horseConfidence = 0;
       } else {
           this.horseConfidence = horseConfidence;
       }
    }

    //Horse has fell
    public void fall() {
        this.horseFallen = true;
    }

    // Get the lane number of horse
    public int getLaneNumber() {
        return this.laneNumber;
    }

    // Set lane number of horse
    public void setLaneNumber(int newLane) {
        this.laneNumber = newLane;
    }

    // Get the confidence of horse
    public double getConfidence() {
        return this.horseConfidence;
    }

    // Get the distance of horse
    public int getDistanceTravelled() {
        return this.horseDistance;
    }

    // Get the name of horse
    public String getName() {
        return this.horseName;
    }

    // Get the symbol of horse
    public char getSymbol() {
        return this.horseSymbol;
    }

    // Set the position to default
    public void goBackToStart() {
        this.horseDistance = 0;
        this.horseFallen = false;
    }

    // Set the horse to fallen
    public boolean hasFallen() {
        return this.horseFallen;
    }

    // Set position of horse + 1
    public void moveForward() {
        this.horseDistance = this.horseDistance+1;
    }

    // Set confidence of horse to new value 0-1
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
        this.horseConfidence = newConfidence;
    }

    // Set symbol of the horse to new character
    public void setSymbol(char newSymbol) {
        this.horseSymbol = newSymbol;
    }
}
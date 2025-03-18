import java.util.concurrent.TimeUnit;
/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author McRaceface
 * @version 1.1
 */
class Race {
    private final int raceLength;
    private Horse lane1Horse;
    private Horse lane2Horse;
    private Horse lane3Horse;

    /**
     * Constructor for objects of class Race
     * Initially, there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in meters/units...)
     */
    public Race(int distance) {
        // Initialize instance variables
        raceLength = distance;
        lane1Horse = null;
        lane2Horse = null;
        lane3Horse = null;
    }

    /**
     * Adds a horse to the race in a given lane
     * 
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse, int laneNumber) {
        switch (laneNumber) {
            case 1 -> lane1Horse = theHorse;
            case 2 -> lane2Horse = theHorse;
            case 3 -> lane3Horse = theHorse;
            default -> System.out.println("Cannot add horse to lane " + laneNumber + " because there is no such lane");
        }
    }

    /**
     * Start the race
     * The horses are brought to the start and
     * then repeatedly moved forward until the 
     * race is finished
     */
    public void startRace() {
        // Declare a local variable to tell us when the race is finished
        boolean finished = false;

        // Directly initialize the class-level variables
        lane1Horse = new Horse('1', "Horse 1", 0.1);
        lane2Horse = new Horse('2', "Horse 2", 0.10);
        lane3Horse = new Horse('3', "Horse 3", 0.15);;;;;;;;;;;;;;;;;;;;;;;;;;;;;

        // Add horse to lanes
        addHorse(lane1Horse, 1);
        addHorse(lane2Horse, 2);
        addHorse(lane3Horse, 3);

        // Reset all the lanes (all horses not fallen and back to 0). 
        lane1Horse.goBackToStart();
        lane2Horse.goBackToStart();
        lane3Horse.goBackToStart();

        // Race until one of the horses finishes
        while (!finished) {
            // Move each horse
            moveHorse(lane1Horse);
            moveHorse(lane2Horse);
            moveHorse(lane3Horse);

            // Print the race positions
            printRace();

            // If any of the three horses has won, the race is finished
            if (raceWonBy(lane1Horse) || raceWonBy(lane2Horse) || raceWonBy(lane3Horse) || (lane1Horse.hasFallen()&&lane2Horse.hasFallen()&&lane3Horse.hasFallen())) {
                finished = true;
            }

            // Wait for 100 milliseconds
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {}
        }
        
        if (raceWonBy(lane1Horse)) {
            System.out.println(lane1Horse.getName()+", has won the Race");
            // change Confidence after the race
            lane1Horse.setConfidence(lane1Horse.getConfidence()*1.1);
            lane2Horse.setConfidence(lane2Horse.getConfidence()*0.9);
            lane3Horse.setConfidence(lane3Horse.getConfidence()*0.9);
        } else if (raceWonBy(lane2Horse)) {
            System.out.println(lane2Horse.getName()+", has won the Race");
            // change Confidence after the race
            lane1Horse.setConfidence(lane1Horse.getConfidence()*0.9);
            lane2Horse.setConfidence(lane2Horse.getConfidence()*1.1);
            lane3Horse.setConfidence(lane3Horse.getConfidence()*0.9);
        } else if (raceWonBy(lane3Horse)) {
            System.out.println(lane3Horse.getName()+", has won the Race");
            // change Confidence after the race
            lane1Horse.setConfidence(lane1Horse.getConfidence()*0.9);
            lane2Horse.setConfidence(lane2Horse.getConfidence()*0.9);
            lane3Horse.setConfidence(lane3Horse.getConfidence()*1.1);
        }
    }

    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     * 
     * @param theHorse the horse to be moved
     */
    private void moveHorse(Horse theHorse) {
        if (!theHorse.hasFallen()) {
            // Probability that the horse will move forward depends on its confidence
            if (Math.random() < theHorse.getConfidence()) {
                theHorse.moveForward();
            }

            // Probability that the horse will fall (depends exponentially on confidence)
            if (Math.random() < (0.1 * theHorse.getConfidence() * theHorse.getConfidence())) {
                theHorse.fall();
            }
        }
    }

    /** 
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */
    private boolean raceWonBy(Horse theHorse) {
        return theHorse.getDistanceTravelled() == raceLength;
    }

    /***
     * Print the race on the terminal
     */
    private void printRace() {
        System.out.print('\u000C');  // Clear the terminal window

        multiplePrint('=', raceLength + 3); // Top edge of track
        System.out.println();

        printLane(lane1Horse);
        System.out.println();

        printLane(lane2Horse);
        System.out.println();

        printLane(lane3Horse);
        System.out.println();

        multiplePrint('=', raceLength + 3); // Bottom edge of track
        System.out.println();    
    }

    /**
     * Print a horse's lane during the race
     * For example:
     * |           X                      |
     * to show how far the horse has run
     */
    private void printLane(Horse theHorse) {
        // Calculate how many spaces are needed before and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();

        System.out.print('|');

        // Print spaces before the horse
        multiplePrint(' ', spacesBefore);

        // If the horse has fallen, print a fallen symbol; else print the horse's symbol
        if (theHorse.hasFallen()) {
            System.out.print('\u2322');  // Unicode for fallen symbol
        } else {
            System.out.print(theHorse.getSymbol());
        }

        // Print spaces after the horse
        multiplePrint(' ', spacesAfter);

        System.out.print('|');
    }

    /***
     * Print a character a given number of times
     * E.g. printmany('x', 5) will print: xxxxx
     * 
     * @param aChar the character to print
     * @param times the number of times to print it
     */
    private void multiplePrint(char aChar, int times) {
        for (int i = 0; i < times; i++) {
            System.out.print(aChar);
        }
    }
}
import java.util.ArrayList;
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
    ArrayList<Horse> horses = new ArrayList<>();

    /**
     * Constructor for objects of class Race
     * Initially, there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in meters/units...)
     */
    public Race(int distance) {
        // Initialize instance variables
        raceLength = distance;
    }

    /**
     * Adds a horse to the race in a given lane
     * 
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */

    // public void addHorse(Horse theHorse, int laneNumber) {
    //     switch (laneNumber) {
    //         case 1 -> horses.add(1, theHorse);
    //         case 2 -> horses.add(2, theHorse);
    //         case 3 -> horses.add(3, theHorse);
    //         default -> System.out.println("Cannot add horse to lane " + laneNumber + " because there is no such lane");
    //     }
    // }

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
        horses.add(new Horse('1', "Horse 1", 0.1));
        horses.add(new Horse('2', "Horse 2", 0.10));
        horses.add(new Horse('3', "Horse 3", 0.15));

        // Reset all the lanes (all horses not fallen and back to 0). 
        for (Horse horse : horses) {
            horse.goBackToStart();
        }

        // Race until one of the horses finishes
        while (!finished) {
            // Move each horse
            for (Horse horse : horses) {
                moveHorse(horse);
            }

            // Print the race positions
            printRace();

            // If any of the three horses has won, the race is finished
            // If all horses fell, race is over.
            finished = raceFinished(horses);

            // Wait for 100 milliseconds
            try {
                TimeUnit.MILLISECONDS.sleep(250);
            } catch (InterruptedException e) {}
        }
        showWinner(horses);
    }

    private void showWinner(ArrayList<Horse> horses) {
        for (Horse horse : horses) {
            if (raceWonBy(horse)) {
                System.out.println(horse.getName()+" has won the Race");
                horse.setConfidence(horse.getConfidence()*1.1);
            } else {
                horse.setConfidence(horse.getConfidence()*0.9);
            }
        }
    }

    /**
     * Check if the race is Over
     * All horse fell
     * A horse has finished the race.
     * 
     */
    private boolean raceFinished(ArrayList<Horse> horses) {
        boolean finished = false;
        boolean allFell = true;
        for (Horse horse : horses) {
            if (raceWonBy(horse)) {
                return true;
            }
            if (!horse.hasFallen()) {
                allFell = false;
            }
        }
        if (allFell) {
            finished = true;
        }
        return finished;
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

        // printLane(lane1Horse);
        // System.out.println();

        // printLane(lane2Horse);
        // System.out.println();

        // printLane(lane3Horse);
        // System.out.println();

        for (Horse horse : horses) {
            printLane(horse);
            System.out.println();
        }

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
            System.out.print('\u274C');  // Unicode for fallen symbol '\u2322'
        } else {
            System.out.print(theHorse.getSymbol());
        }

        // Print spaces after the horse
        multiplePrint(' ', spacesAfter);

        System.out.print('|');
        System.out.print(" "+theHorse.getName()+" (Current Confidence "+theHorse.getConfidence()+")");
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
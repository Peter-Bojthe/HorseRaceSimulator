import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author McRaceface
 * @version 1.2
 */
class Race {
    private int raceLength;
    ArrayList<Horse> horses = new ArrayList<>();

    /**
     * Constructor for objects of class Race
     * Initially, there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in meters/units...)
     */
    public Race() {
        // Initialize instance variables
        //raceLength = distance;
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
        // Declare local variable to tell us when simulation is finished
        boolean finishedSimulation = false;
        // Declare a local variable to tell us when the race is finished
        boolean finishedRace = false;
        // Let user decide the length of the race
        raceLength = UserInput.trackLength();
        // Create Horses to start simulation
        createHorses();
        // Reset all the lanes (all horses not fallen and back to 0). 
        resetHorsesPosition();

        while (!finishedSimulation) {
            while (!finishedRace) {
                // Move each horse
                moveAllHorses();
                // Print the race positions
                printRace();
                // If any of the three horses has won, the race is finished
                // If all horses fell, race is over.
                finishedRace = raceFinished();
                // Wait for 100 milliseconds
                try {
                    TimeUnit.MILLISECONDS.sleep(125);
                } catch (InterruptedException e) {}
            }
            showWinner();
            finishedSimulation = UserInput.playAgain();
            if (!finishedSimulation) {
                changeRaceDetails();
            }
        }
    }

    private void showRaceDetails() {
        System.out.println("Current length of the race: "+raceLength);
        System.out.println("Current number of lanes: "+horses.size());
        for (Horse horse : horses) {
            System.out.print(horse.getName()+" is in lane "+horse.getLaneNumber());
            System.out.println(", Confidenece: "+horse.getConfidence());
        }
    }

    private void changeRaceDetails() {
        showRaceDetails();
    }

    // If there is only 1 lane it cannot be empty.
    private void createHorses() {
        horses.clear();
        int input = UserInput.amountOfLanes();
        for (int i = 0; i < input; i++) {
            if (UserInput.moreHorses(i+1)) {
                String name = UserInput.inputString("Horse Name: ");
                char character = UserInput.inputCharacter("Horse Character: ");
                horses.add(i, new Horse(character, name, 0.1, i));
            } else {
                horses.add(i, null);
            }
        }
    }

    private void showWinner() {
        for (Horse horse : horses) {
            if (horse == null) continue;
            if (raceWonBy(horse)) {
                System.out.println(horse.getName()+" has won the Race");
                horse.setConfidence(horse.getConfidence()*1.1);
            } else {
                horse.setConfidence(horse.getConfidence()*0.9);
            }
        }
    }

    private void moveAllHorses() {
        for (Horse horse : horses) {
            if (horse == null) continue;
            moveHorse(horse);
        }
    }

    private void resetHorsesPosition() {
        for (Horse horse : horses) {
            if (horse == null) continue;
            horse.goBackToStart();
        }
    }

    /**
     * Check if the race is Over
     * All horses have  fell
     * Or a horse has finished the race.
     * @param horses check all horses to see if any have won
     * have a boolean flag to check if all of them have fell or not.
     * 
     */
    private boolean raceFinished() {
        boolean allFell = true;
        for (Horse horse : horses) {
            if (horse == null) continue;
            if (raceWonBy(horse)) {
                return true;
            }
            if (!horse.hasFallen()) {
                allFell = false;
            }
        }
        return allFell;
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
     * Horse index in array matches the lane No.
     */
    private void printRace() {
        System.out.print('\u000C');  // Clear the terminal window

        multiplePrint('=', raceLength + 3); // Top edge of track
        System.out.println();

        printAllLanes();

        multiplePrint('=', raceLength + 3); // Bottom edge of track
        System.out.println();    
    }

    private void printAllLanes() {
        for (Horse horse : horses) {
            //if (horse == null) continue;
            printLane(horse);
            System.out.println();
        }
    }

    /**
     * Print a horse's lane during the race
     * For example:
     * |           X                      |
     * to show how far the horse has run
     */
    private void printLane(Horse theHorse) {
        if (theHorse == null) {
            System.out.print('|');
            multiplePrint(' ', raceLength+1);
            System.out.print('|');
            System.out.print(" Empty Lane");
        } else {
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
            System.out.print(" "+theHorse.getName()+" (Current Confidence "+theHorse.getConfidence()+")");
        }
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
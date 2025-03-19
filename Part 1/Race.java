import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

//
// ♘
//
/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author McRaceface
 * @version 1.2
 */
class Race {
    private int raceLength;
    static ArrayList<Horse> horses = new ArrayList<>();

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

    public void addHorsesToLanes() {
        for (int i = 0; i < horses.size(); i++) {
            if (horses.get(i) == null) continue;
            horses.get(i).setLaneNumber(i+1);
        }
    }

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
                // Wait for 125 milliseconds
                try {
                    TimeUnit.MILLISECONDS.sleep(125);
                } catch (InterruptedException e) {}
            }
            showWinner();
            resetHorsesPosition();
            showRaceDetails();

            finishedSimulation = UserInput.askYesNo("STOP SIMULATION:  yes [1] no [0]: ");
            if (!finishedSimulation) {
                finishedRace = false;
                if (UserInput.askYesNo("Would you like to make changes to the next simulation yes [1], no [0]: ")) {
                    changeRaceDetails();
                }
            }
        }
    }

    private void showRaceDetails() {
        System.out.println("\nCurrent length of the race: "+raceLength);
        System.out.println("Current number of lanes: "+horses.size());
        System.out.println("Current number of horses: "+Horse.horseCounter);
        for (Horse horse : horses) {
            if (horse == null) continue;
            System.out.print(horse.getName()+" is in lane "+horse.getLaneNumber());
            System.out.println(", Confidenece: "+horse.getConfidence());
        }
        System.out.println();
    }

    // add/ remove horses
    // change the number of lanes
    // change the length of race
    //
    private void changeRaceDetails() {
        if (UserInput.askYesNo("\n\nWould you like to Remove any lanes yes [1], no [0]: ")) removeLanes();
        if (UserInput.askYesNo("\n\nWould you like to Add any lanes yes [1], no [0]: ")) addLanes();
        if (UserInput.askYesNo("\n\nWould you like to Remove any horses yes [1], no [0]: ")) removeHorses();
        if (UserInput.askYesNo("\n\nWould you like to Add any horses yes [1], no [0]: ")) addHorses();
        // if (UserInput.askYesNo("Would you like to chage the length of the race yes [1], no [0]: ")) {
        //     raceLength = UserInput.trackLength();
        // }
    }

    private void addLanes() {
        horses.add(null); // add lane
        boolean done = false;
        while (horses.size() <= 8 && !done) {
            System.out.println("There are currently "+horses.size()+" lanes.");
            done = UserInput.askYesNo("Would you like to add a lane yes [1], no [0]");
            if (done) {
                horses.add(null);
                done = false;
            } else {
                done = true;
            }
        }
        addHorsesToLanes();
    }

    private void removeLanes() {
        boolean done = false;
        while (horses.size() > 2 && !done) {
            System.out.println("There are currently "+horses.size()+" lanes.");
            System.out.println("The number of lanes cannot be less than 2");
            int input = UserInput.choiceOfAllLanes("Enter the lane number you want to remove: ");
            if (input-1 > horses.size()) {
                System.out.println("Invalid Choice of Lane");
            } else {
                if (horses.get(input-1) != null) {
                    Horse.horseCounter--;
                }
                horses.remove(input-1);
                if (horses.size() == 2) {
                    System.out.println("The number of lanes is now 2, cannot remove more.");
                    return;
                }
                done = UserInput.askYesNo("Stop removing Lanes yes [1], no [0]");
            }
        }
        addHorsesToLanes();
    }

    private void removeHorses() {
        boolean done = false;
        while (horseLanes() >= 2  && !done) {
            showFullLanes();
            int input = UserInput.choiceOfAllLanes("Enter the lane number of the horse you want to remove: ");
            if (input-1 > horses.size() || horses.get(input-1) == null) {
                System.out.println("Invalid Choice of Lane.");
            } else {
                horses.set(input-1, null);
                Horse.horseCounter--;
                done = UserInput.askYesNo("Stop removing horses yes [1], no [0]: ");
            }
        }
    }

    private int horseLanes() {
        int count = 0;
        for (Horse horse : horses) {
            if (horse != null) count++;
        }
        return count;
    }

    private boolean fullLanes() {
        for (Horse horse : horses) {
            if (horse == null) return false;
        }
        System.out.println("All lanes taken");
        return true;
    }

    private void addHorses() {
        boolean done = false;
        while (!fullLanes() && !done) {
            showEmptyLanes();
            int input = UserInput.choiceOfAllLanes("Enter the lane number you want to add a horse to: ");
            if (input-1 > horses.size() || horses.get(input-1) != null) {
                System.out.println("Invalid Lane or Taken Lane.");
            } else {
                horses.set(input-1, createHorse(input));
                Horse.horseCounter++;
                done = UserInput.askYesNo("Stop adding horses yes [1], no [0]: "); 
            }
        }
    }

    // private void removeLanes() {
    //     return;
    // }

    // private void addLanes() {
    //     return;
    // }


    private void showFullLanes() {
        System.out.println("Lanes with Horses: ");
        for (int i = 0; i < horses.size(); i++) {
            if (horses.get(i) == null) continue;
            System.out.println(i+1);
        }
    }
    private void showEmptyLanes() {
        System.out.println("Empty Lanes: ");
        for (int i = 0; i < horses.size(); i++) {
            if (horses.get(i) != null) continue;
            System.out.println(i+1);
        }
    }

    private Horse createHorse(int lane) {
        String name = UserInput.inputString("Horse Name: ");
        char character = UserInput.inputCharacter("Horse Character: ");
        return new Horse(character, name, 0.25, lane);
    }

    // If there is only 1 lane it cannot be empty.
    // private void createHorses() {
    //     horses.clear();
    //     int input = UserInput.pickNumberOfLanes("How many lanes [2, 8]: ");
    //     for (int i = 0; i < input; i++) {
    //         if (UserInput.askYesNo("Add horse to lane "+String.valueOf(i+1)+": yes [1], no [0]: ")) {
    //             horses.add(i, createHorse(i+1));
    //         } else {
    //             horses.add(i, null);
    //         }
    //     }
    // }

    private void createHorses() {
        horses.clear();
        int inputLanes = UserInput.pickNumberOfLanes("How many lanes would ou like [2, 8]: ");
        for (int numberOfLanes = 0; numberOfLanes < inputLanes; numberOfLanes++) {
            horses.add(numberOfLanes, null);
        }
        int inputHorses = UserInput.pickNumberOfHorses("How many horses would you like: ", inputLanes);
        for (int numberOfHorses = 0; numberOfHorses < inputHorses; numberOfHorses++) {
            int lane = UserInput.choseLaneWithLimit("Which lane do you want to add this horse to: ", inputLanes);
            while (horses.get(lane-1) != null) {
                System.out.println("Lane is taken by another horse.");
                lane = UserInput.choseLaneWithLimit("Which lane do you want to add this horse to: ", inputLanes);
            }
            horses.set(lane-1, createHorse(lane));
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
            System.out.print(" Lane: "+theHorse.getLaneNumber());
            System.out.print(", "+theHorse.getName()+" (Current Confidence "+theHorse.getConfidence()+")");
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
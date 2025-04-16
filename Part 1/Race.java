import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

// Horse Character.
//////////
//  ♘  //
//////////

// Next to be done...
// Random-Horse generation

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author McRaceface
 * @author Peter Bojthe
 * @version 10/04/25
 * 
 */
class Race extends UserInput {
    private int raceLength;
    static ArrayList<Horse> horses = new ArrayList<>();
    static ArrayList<String> uniqueHorseNames = new ArrayList<>();

    /**
     * Constructor for objects of class Race
     * Initially, there are no horses in the lanes
     */
    public Race() {
        // Initialize instance variables
        // Empty Constructor
        // Race details chosen by User
    }

    /**
     * Names must be unique
     * check if nameis taken
     * 
     */
    private boolean usedName(String name) {
        for (int i = 0; i < uniqueHorseNames.size(); i++) {
            if (name.equals(uniqueHorseNames.get(i))) {
                System.out.println("Horse name taken.");
                return true;
            }
        }
        return false;
    }

    /**
     * Adds horses to their respective lanes based on their position in the list.
     */
    public void addHorsesToLanes() {
        for (int i = 0; i < horses.size(); i++) {
            if (horses.get(i) == null) continue;
            horses.get(i).setLaneNumber(i+1);
        }
    }

    /**
     * Starts the race simulation.
     * Horses are brought to the start and repeatedly moved forward until the race is finished.
     * 
     * @throws IOException if there is an input/output (File Handling) error during the race simulation.
     */
    public void startRace() throws IOException {
        boolean finishedSimulation = false;
        boolean finishedRace = false;

        chooseSavedHorse();
        raceLength = chooseTrackLength("\nLength of Race [25m, 100m]: ");
        createHorses();
        resetHorsesPosition();

        while (!finishedSimulation) {
            while (!finishedRace) {
                moveAllHorses();
                printRace();
                finishedRace = raceFinished();
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {}
            }
            showWinner();
            resetHorsesPosition();
            showRaceDetails();

            finishedSimulation = askYesNo("STOP SIMULATION:  yes [1] no [0]: ");
            if (!finishedSimulation) {
                finishedRace = false;
                if (askYesNo("Would you like to make changes to the next simulation yes [1], no [0]: ")) {
                    changeRaceDetails();
                }
            }
        }
    }

    private void chooseSavedHorse() throws IOException {
        boolean answer = askYesNo("Would you like to use a previously saved horse yes [1], no [0]: ");
        if (answer) {
            if (HorseDetailsFileHandling.countFileLines() == 0) {
                System.out.println("File is empty");
                return;
            }
            showHorseDetailsFromFile();
            askUserToChooseHorseFromFile();
        }
    }

    private void askUserToChooseHorseFromFile() throws IOException {
        int numberOfSavedHorses = HorseDetailsFileHandling.countFileLines();
        if (numberOfSavedHorses >=1 ) {
            int input = inputNumber("Enter the row number of the horse you want to use: ");
            if (input <= 0 || input > numberOfSavedHorses) {
                System.out.println("Invalid choice of file rows.");
                input = inputNumber("Enter the row number of the horse you want to use: ");
            }
            String[] horseDetails = HorseDetailsFileHandling.getHorseDetails(input);
            Horse horse = new Horse(horseDetails[0], horseDetails[1], horseDetails[2], horseDetails[3], horseDetails[4], horseDetails[5]);
            horses.add(horse);
            System.out.println("This horse will be added to lane 1.");
        }
    }

    private void showHorseDetailsFromFile() {
        System.out.println("\n\n\nSaved Horses: ");
        HorseDetailsFileHandling.printFormattedTable();
    }

    /**
     * Displays the current race details including length, number of lanes, and horses.
     */
    private void showRaceDetails() {
        System.out.println("\nCurrent length of the race: "+raceLength);
        System.out.println("Current number of lanes: "+horses.size());
        System.out.println("Current number of horses: "+Horse.horseCounter);
        for (Horse horse : horses) {
            if (horse == null) continue;
            System.out.print(horse.getName()+" is in lane "+horse.getLaneNumber());
            System.out.println(", Confidence: "+horse.getConfidence());
        }
        System.out.println();
    }

    /**
     * Allows the user to change race details such as removing or adding lanes, removing or adding horses, and changing the race length.
    * @throws IOException 
    */
    private void changeRaceDetails() throws IOException {
        if (askYesNo("\n\nWould you like to Remove any lanes yes [1], no [0]: ")) {
            removeLanes();
            addHorsesToLanes();
        }
        if (askYesNo("\n\nWould you like to Add any lanes yes [1], no [0]: ")) {
            addLanes();
            addHorsesToLanes();
        }
        if (askYesNo("\n\nWould you like to Remove any horses yes [1], no [0]: ")) {
            removeHorses();
            addHorsesToLanes();
        }
        if (askYesNo("\n\nWould you like to Add any horses yes [1], no [0]: ")) {
            addHorses();
            addHorsesToLanes();
        }
        if (askYesNo("\n\nWould yo like to save any of the horse details yes [1], no [0]: ")) {
            saveHorse();
            showHorseDetailsFromFile();
        }
        if (askYesNo("\n\nWould you like to change the length of the race yes [1], no [0]: ")) {
             raceLength = chooseTrackLength("Length of Race [25m, 100m]: ");
        }
    }

    /*
     * Save a horse details
     * User choice which one to save
     */
    private void saveHorse() throws IOException {
        showHorseDetails();
        int pickedHorseIndex = pickAnyHorse("Enter the lane number of the horse you want to save: ") -1;
        while (horses.get(pickedHorseIndex) == null) {
            System.out.println("Lane is empty (invalid). ");
            pickedHorseIndex = pickAnyHorse("Enter the lane number of the horse you want to save: ") -1;
        }
        Horse horse = horses.get(pickedHorseIndex);
        HorseDetailsFileHandling.saveHorseDetails(horse.getName(), horse.getConfidence(), horse.getSymbol(), horse.getTotalWins(), horse.getTotalRaces(), horse.getWinRate());
        // System.exit(0);
    }

    /**
     * Shows the Details of each horse in the race
     */
    private void showHorseDetails() {
        for (Horse horse : horses) {
            if (horse == null) continue;
            System.out.println("Lane: "+horse.getLaneNumber()+", "+horse.getName()+" with confidence "+horse.getConfidence());
        }
    }

    /**
     * Adds lanes to the race.
     */
    private void addLanes() {
        horses.add(null);
        boolean done = false;
        while (horses.size() <= 8 && !done) {
            System.out.println("There are currently "+horses.size()+" lanes.");
            done = askYesNo("Would you like to add a lane yes [1], no [0]");
            if (done) {
                horses.add(null);
                done = false;
            } else {
                done = true;
            }
        }
        addHorsesToLanes();
    }

    /**
     * Removes lanes from the race.
     */
    private void removeLanes() {
        boolean done = false;
        while (horses.size() > 2 && !done) {
            System.out.println("There are currently "+horses.size()+" lanes.");
            System.out.println("The number of lanes cannot be less than 2");
            int input = pickOneOfTheLanes("Enter the lane number you want to remove: ", horses.size());
            if (input-1 > horses.size()) {
                System.out.println("Invalid Choice of Lane");
            } else {
                if (horses.get(input-1) != null) {
                    Horse.horseCounter--;
                }
                uniqueHorseNames.remove(input-1);
                horses.remove(input-1);
                if (horses.size() == 2) {
                    System.out.println("The number of lanes is now 2, cannot remove more.");
                    return;
                }
                done = askYesNo("Stop removing Lanes yes [1], no [0]");
            }
        }
        addHorsesToLanes();
    }

    /**
     * Removes horses from the race.
     */
    private void removeHorses() {
        boolean done = false;
        while (horseLanes() >= 2  && !done) {
            showFullLanes();
            int input = pickOneOfTheLanes("Enter the lane number of the horse you want to remove: ", horses.size());
            if (input-1 > horses.size() || horses.get(input-1) == null) {
                System.out.println("Invalid Choice of Lane.");
            } else {
                horses.set(input-1, null);
                uniqueHorseNames.set(input-1, null);
                Horse.horseCounter--;
                done = askYesNo("Stop removing horses yes [1], no [0]: ");
            }
        }
    }

    /**
     * Counts the number of lanes with horses.
     * 
     * @return the number of lanes with horses.
     */
    private int horseLanes() {
        int count = 0;
        for (Horse horse : horses) {
            if (horse != null) count++;
        }
        return count;
    }

    /**
     * Checks if all lanes are occupied by horses.
     * 
     * @return true if all lanes are occupied, false otherwise.
     */
    private boolean fullLanes() {
        for (Horse horse : horses) {
            if (horse == null) return false;
        }
        System.out.println("All lanes taken");
        return true;
    }

    /**
     * Adds horses to the race.
     */
    private void addHorses() {
        boolean done = false;
        while (!fullLanes() && !done) {
            showEmptyLanes();
            int input = pickOneOfTheLanes("Enter the lane number you want to add a horse to: ", horses.size());
            if (input-1 > horses.size() || horses.get(input-1) != null) {
                System.out.println("Invalid Lane or Taken Lane.");
            } else {
                horses.set(input-1, createHorse(input));
                Horse.horseCounter++;
                done = askYesNo("Stop adding horses yes [1], no [0]: "); 
            }
        }
    }

    /**
     * Displays lanes that are occupied by horses.
     */
    private void showFullLanes() {
        System.out.println("Lanes with Horses: ");
        for (int i = 0; i < horses.size(); i++) {
            if (horses.get(i) == null) continue;
            System.out.println(i+1);
        }
    }

    /**
     * Displays lanes that are empty.
     */
    private void showEmptyLanes() {
        System.out.println("Empty Lanes: ");
        for (int i = 0; i < horses.size(); i++) {
            if (horses.get(i) != null) continue;
            System.out.println(i+1);
        }
    }

    /**
     * Creates a new horse with the specified name, character, and lane.
     * 
     * @param lane the lane number where the horse will be placed.
     * @return the created Horse object.
     */
    private Horse createHorse(int lane) {
        String name = inputString("Horse Name: ");
        while (usedName(name)) {
            name = inputString("Horse Name: ");
        }
        uniqueHorseNames.set(lane-1, name);
        char character = inputCharacter("Horse Character: ");
        return new Horse(name, 0.25, character, 0, 0, 0, lane);
    }

    /**
     * Initializes the horses and lanes for the race.
     */
    private void createHorses() {
        //horses.clear();
        int inputLanes = chooseNumberOfLanes("How many lanes would you like [2, 8]: ");
        for (int numberOfLanes = horses.size(); numberOfLanes < inputLanes; numberOfLanes++) {
            horses.add(numberOfLanes, null);
            uniqueHorseNames.add(numberOfLanes, null);
        }
        int inputHorses = chooseNumberOfHorsesGivenNumberOfLanes("How many horses would you like: ", inputLanes);
        inputHorses = inputHorses - Horse.horseCounter;
        for (int numberOfHorses = 0; numberOfHorses < inputHorses; numberOfHorses++) {
            int lane = pickOneOfTheLanes("Which lane do you want to add this horse to: ", inputLanes);
            while (horses.get(lane-1) != null) {
                System.out.println("Lane is taken by another horse.");
                lane = pickOneOfTheLanes("Which lane do you want to add this horse to: ", inputLanes);
            }
            horses.set(lane-1, createHorse(lane));
        }
    }

    /**
     * Displays the winner of the race and adjusts the confidence of the horses.
     */
    private void showWinner() throws IOException {
        for (Horse horse : horses) {
            if (horse == null) continue;
            horse.setTotalRaces(horse.getTotalRaces()+1);
            if (raceWonBy(horse)) {
                System.out.println(horse.getName()+" has won the Race");
                horse.setConfidence(horse.getConfidence()*1.1);
                horse.setTotalWins(horse.getTotalWins()+1);
            } else {
                horse.setConfidence(horse.getConfidence()*0.9);
            }
            horse.setWinRate(horse.getTotalWins(), horse.getTotalRaces());
            HorseDetailsFileHandling.updateHorseInFile(horse);
        }
    }

    /**
     * Moves all horses in the race forward.
     */
    private void moveAllHorses() {
        for (Horse horse : horses) {
            if (horse == null) continue;
            moveHorse(horse);
        }
    }

    /**
     * Resets the position of all horses to the start line.
     */
    private void resetHorsesPosition() {
        for (Horse horse : horses) {
            if (horse == null) continue;
            horse.goBackToStart();
        }
    }

    /**
     * Checks if the race is finished.
     * 
     * @return true if a horse has won or all horses have fallen, false otherwise.
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
     * Moves a horse forward or makes it fall based on its confidence.
     * 
     * @param theHorse the horse to be moved.
     */
    private void moveHorse(Horse theHorse) {
        if (!theHorse.hasFallen()) {
            if (Math.random() < theHorse.getConfidence()) {
                theHorse.moveForward();
            }

            if (Math.random() < (0.1 * theHorse.getConfidence() * theHorse.getConfidence())) {
                theHorse.fall();
            }
        }
    }

    /**
     * Determines if a horse has won the race.
     * 
     * @param theHorse the horse to check.
     * @return true if the horse has won, false otherwise.
     */
    private boolean raceWonBy(Horse theHorse) {
        return theHorse.getDistanceTravelled() == raceLength;
    }

    /**
     * Prints the current state of the race.
     */
    private void printRace() {
        System.out.print('\u000C');
        multiplePrint('=', raceLength + 3);
        System.out.println();
        printAllLanes();
        multiplePrint('=', raceLength + 3);
        System.out.println();    
    }

    /**
     * Prints all lanes in the race.
     */
    private void printAllLanes() {
        for (Horse horse : horses) {
            printLane(horse);
            System.out.println();
        }
    }

    /**
     * Prints a single lane with the horse's current position.
     * 
     * @param theHorse the horse in the lane to print.
     */
    private void printLane(Horse theHorse) {
        if (theHorse == null) {
            System.out.print('|');
            multiplePrint(' ', raceLength+1);
            System.out.print('|');
            System.out.print(" Empty Lane");
        } else {
            int spacesBefore = theHorse.getDistanceTravelled();
            int spacesAfter = raceLength - theHorse.getDistanceTravelled();

            System.out.print('|');
            multiplePrint(' ', spacesBefore);

            if (theHorse.hasFallen()) {
                System.out.print('\u2322');
            } else {
                System.out.print(theHorse.getSymbol());
            }

            multiplePrint(' ', spacesAfter);
            System.out.print('|');
            System.out.print(" Lane: "+theHorse.getLaneNumber());
            System.out.print(", "+theHorse.getName()+" (Current Confidence "+theHorse.getConfidence()+")");
        }
    }

    /**
     * Prints a character multiple times.
     * 
     * @param aChar the character to print.
     * @param times the number of times to print the character.
     */
    private void multiplePrint(char aChar, int times) {
        for (int i = 0; i < times; i++) {
            System.out.print(aChar);
        }
    }
}
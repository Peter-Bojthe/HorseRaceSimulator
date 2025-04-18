import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author McRaceface
 * @author Peter Bojthe
 * @version 16/04/25
 * 
 */
public class Race extends UserInput {
    private int raceLength;
    private String raceCondition = "Sunny";
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
                System.out.println("\nHorse name taken. Choose another name.");
                return true;
            }
        }
        return false;
    }

    /**
     * weather during race
     * race conditions
     * effects horse' confidence
     */
    private void raceConditions() {
        Random random = new Random();

        String[] weatherConditions = {"Raining", "Wet", "Sunny", "Snow"};
        double[] weatherConditionsEffect = {0.25, 0.15, 1, 0.50};

        int chosen = random.nextInt(weatherConditions.length);
        String newWeatherCondition = weatherConditions[chosen];
        double change = weatherConditionsEffect[chosen];

        if (raceCondition.equals(newWeatherCondition)) {
            System.out.println("\n\nWeather conditions have not changed.");
            return;
        }

        changeHorseConfidenceDueToConditions(newWeatherCondition, change);
    }

    /**
     * 
     * Change horse confidence
     * and output information about the weather
     * @param weather new weather condition
     * @param change the scale factor by which the horse confidence will be 
     */
    private void changeHorseConfidenceDueToConditions(String weather, double change) {
        switch (weather) {
            case "Raining" -> System.out.println("\nHorse confidence is decreased by 0.25, due to rain.");
            case "Wet"     -> System.out.println("\nHorse confidence is decreased by 0.15, due to a wet track.");
            case "Sunny"   -> System.out.println("\nHorse confidence is not affected, it is sunny.");
            case "Snow"    -> System.out.println("\nHorse confidence is decreased by 0.50 due to snow.");
        }
        for (Horse horse : horses) {
            if (horse == null) continue;
            horse.setConfidence(horse.getConfidence()*change);
        }
        raceCondition = weather;
    }

    /**
     * Adds horses to their respective lanes based on their position in the list.
     */
    private void addHorsesToLanes() {
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
        askUserToGenerateRandomHorse();
        raceLength = chooseTrackLength("\nChoose the length of race [25m - 100m]: ");

        // Only Create Horses if 2 or more horses have not yet bee chosen
        if (horses.size() <= 1) {
            createHorses();
        }
        
        raceConditions();
        askToPlaceBet();
        resetHorsesPosition();

        while (!finishedSimulation) {
            // Before Race;
            try {
                System.out.print("\nRace will start soon...");
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {}

            // This is the Race
            while (!finishedRace) {
                moveAllHorses();
                printRace();
                finishedRace = raceFinished();

                // Wait bwtween horse moves
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {}
            } // END RACE

            // End of race PROCEDURES
            showWinner();
            resetHorsesPosition();
            showRaceDetails();
            removeAllBets();

            // Between Races or Quit program
            finishedSimulation = askYesNo("\nSTOP SIMULATION:  yes [1], no [0]: ");
            if (!finishedSimulation) {
                finishedRace = false;
                if (askYesNo("\nWould you like to make changes to the next simulation yes [1], no [0]: ")) {
                    changeRaceDetails();
                }
                System.out.println("\n\nA new race will begin...");
                raceConditions();
                askToPlaceBet();
            }
        }
    }

    private void askToPlaceBet() {
        if (askYesNo("\n\nWould you like to bet on a horse yes [1] no [0]: ")) {
            checkWinnings();
        }
    }

    private void removeAllBets() {
        for (Horse horse : horses) {
            if (horse == null) continue;
            horse.setBetPlacedOn(false);
            horse.setWinnings(0.0);
        }
    }

    private void chooseHorseToPlaceBetOn() {
        String name = inputString("Enter the name of the horse you want to place the bet on: ");
        while (true) {
            for (Horse horse : horses) {
                if (horse == null) continue;
                if (horse.getName().equals(name)) {
                    horse.setBetPlacedOn(true);
                    for (Horse horse1 : horses) {
                        if (horse1 == null) continue;
                        System.out.println(horse.getConfidence());
                    }
                    return;
                }
            }
            System.out.println("Invalid Name.");
            name = inputString("Enter the name of the horse you want to place the bet on: ");
        }
    }

    /**
     * calculate how much money the user would win
     * if their choice of horse wins
     * 
     */
    private void checkWinnings() {
        System.out.println("The balance: "+BettingSystem.balance);
        double usersBet = placeBet("How much money are you putting on this race: ");
        if (usersBet == 0.0) return;
        for (Horse horse : horses) {
            if (horse == null) continue;
            horse.setWinnings(BettingSystem.calculateWinnings(horse, usersBet));
            System.out.println("If "+horse.getName()+" wins then the payout will be £"+horse.getWinnings());
        }
        chooseHorseToPlaceBetOn();
    }


    private void askUserToGenerateRandomHorse() {
        boolean input = askYesNo("\nWould you like to add a randomly generated horse into the race? yes [1] no [0]: ");
        if (!input) return;

        int numberOfRandomHorses = inputNumber("\nHow many Random Horse would you like [0 - 8]: ");
        if (numberOfRandomHorses == 0) return;

        while (numberOfRandomHorses < 0 || numberOfRandomHorses > 8) {
            System.out.println("\nInvalid choice. Try again.");
            numberOfRandomHorses = inputNumber("\nHow many Random Horse would you like [0 - 8]: ");
        }

        int added = 0;
        while (added < numberOfRandomHorses && horses.size() < 8) {
            Horse horse = generateRandomHorse();
            horses.add(Horse.horseCounter-1, horse);
            uniqueHorseNames.add(Horse.horseCounter-1, horse.getName());
            added++;
        }
        if (horses.size() == 8 && added != numberOfRandomHorses) {
            System.out.println("\nAll random horses could not be added,\n8 lane limit reached.");
            System.out.println(numberOfRandomHorses-added+" random horses has not been addded.");
        }
    }

    /**
     * Generates a random horse with a randomly generated name and character
     * @return A new Horse object with random attributes
     */
    private static Horse generateRandomHorse() {
        String name = generateRandomHorseName();
        char character = generateRandomAlphanumericChar();
        Horse randomHorse = new Horse(name, 0.25, character, 0, 0, 0.0, Horse.horseCounter+1);
        System.out.println("\n\nRandom Horse: ");
        System.out.println("Horse Name: "+randomHorse.getName()+",\nHorse Symbol: "+randomHorse.getSymbol());
        
        return randomHorse;
    }

    /**
     * Generates a random horse name by combining prefixes and suffixes
     * @return A randomly generated horse name
     */
    private static String generateRandomHorseName() {
        String[] prefixes = {
            "Thunder", "Midnight", "Shadow", "Lightning", "Silver", 
            "Golden", "Diamond", "Black", "White", "Red",
            "Wild", "Crazy", "Majestic", "Royal", "Brave",
            "Flying", "Dancing", "Galloping", "Mystic", "Spirit"
        };
        
        String[] suffixes = {
            "hoof", "mane", "tail", "storm", "fire",
            "wind", "blaze", "dancer", "chaser", "runner",
            "prince", "king", "queen", "star", "moon",
            "sun", "dream", "whisper", "shadow", "flash"
        };
        
        Random random = new Random();
        String prefix = prefixes[random.nextInt(prefixes.length)];
        String suffix = suffixes[random.nextInt(suffixes.length)];
        
        return prefix + " " + suffix;
    }

    /**
     * Generates a random alphanumeric character (A-Z, a-z, 0-9)
     * @return A random character
     */
    private static char generateRandomAlphanumericChar() {
        Random random = new Random();
        int choice = random.nextInt(3); // 0-2

        return (char) (switch (choice) {
            case 0 -> random.nextInt(26) + 65;
            case 1 -> random.nextInt(26) + 97;
            case 2 -> random.nextInt(10) + 48;
            default -> 'X';
        });
    }

    private void chooseSavedHorse() throws IOException {
        boolean answer = askYesNo("\nWould you like to use a previously saved horse yes [1], no [0]: ");
        if (answer) {
            if (HorseDetailsFileHandling.countFileLines() == 0) {
                System.out.println("\nThere are curently no saved horses...");
                return;
            }
            showHorseDetailsFromFile();
            askUserToChooseHorseFromFile();
        }
    }

    private void askUserToChooseHorseFromFile() throws IOException {
        int numberOfSavedHorses = HorseDetailsFileHandling.countFileLines();
        if (numberOfSavedHorses >=1 ) {
            int input = inputNumber("\nEnter the row number of the horse you want to use: ");
            if (input <= 0 || input > numberOfSavedHorses) {
                System.out.println("\nInvalid choice of file rows.");
                input = inputNumber("\nEnter the row number of the horse you want to use: ");
            }
            String[] horseDetails = HorseDetailsFileHandling.getHorseDetails(input);
            Horse horse = new Horse(horseDetails[0], horseDetails[1], horseDetails[2], horseDetails[3], horseDetails[4], horseDetails[5], Horse.horseCounter+1);
            horses.add(Horse.horseCounter-1, horse);
            uniqueHorseNames.add(Horse.horseCounter-1, horse.getName());
            System.out.println("\nThis horse will be added to lane "+Horse.horseCounter);
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
        System.out.println("Current number of horses: "+Horse.horseCounter+"\n\n");
        for (Horse horse : horses) {
            if (horse == null) continue;
            System.out.print(horse.getName()+" is in lane "+horse.getLaneNumber());
            System.out.println(", Confidence: "+horse.getConfidence());
        }
        System.out.println("\n");
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
        while (askYesNo("\n\nWould yo like to save any of the horse details yes [1], no [0]: ")) {
            saveHorse();
            showHorseDetailsFromFile();
        }
        if (askYesNo("\n\nWould you like to change the length of the race yes [1], no [0]: ")) {
             raceLength = chooseTrackLength("Length of Race [25m - 100m]: ");
        }
    }

    /*
     * Save a horse details
     * User choice which one to save
     */
    private void saveHorse() throws IOException {
        showHorseDetails();
        int pickedHorseIndex = pickAnyHorse("\nEnter the lane number of the horse you want to save: ") -1;
        while (horses.get(pickedHorseIndex) == null) {
            System.out.println("\nLane is empty. ");
            pickedHorseIndex = pickAnyHorse("\nEnter the lane number of the horse you want to save: ") -1;
        }
        Horse horse = horses.get(pickedHorseIndex);
        HorseDetailsFileHandling.saveHorseDetails(horse.getName(), horse.getConfidence(), horse.getSymbol(), horse.getTotalWins(), horse.getTotalRaces(), horse.getWinRate());
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
            System.out.println("\nThere are currently "+horses.size()+" lanes.");
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
            System.out.println("\nThere are currently "+horses.size()+" lanes.");
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
                    System.out.println("\nThe number of lanes is now 2, cannot remove more.");
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
            int input = pickOneOfTheLanes("\nEnter the lane number of the horse you want to remove: ", horses.size());
            if (input-1 > horses.size() || horses.get(input-1) == null) {
                System.out.println("Invalid Choice of Lane.");
            } else {
                horses.set(input-1, null);
                uniqueHorseNames.set(input-1, null);
                Horse.horseCounter--;
                done = askYesNo("\nStop removing horses yes [1], no [0]: ");
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
        System.out.println("\nAll lanes taken");
        return true;
    }

    /**
     * Adds horses to the race.
     */
    private void addHorses() {
        boolean done = false;
        while (!fullLanes() && !done) {
            showEmptyLanes();
            int input = pickOneOfTheLanes("\nEnter the lane number you want to add a horse to: ", horses.size());
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
        System.out.println("\nLanes with Horses: ");
        for (int i = 0; i < horses.size(); i++) {
            if (horses.get(i) == null) continue;
            System.out.println(i+1);
        }
    }

    /**
     * Displays lanes that are empty.
     */
    private void showEmptyLanes() {
        System.out.println("\nEmpty Lanes: ");
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
        String name = inputString("\nHorse Name: ");
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
        int inputLanes = chooseNumberOfLanes("How many lanes would you like [2 - 8]: ");
        for (int numberOfLanes = horses.size(); numberOfLanes < inputLanes; numberOfLanes++) {
            horses.add(numberOfLanes, null);
            uniqueHorseNames.add(numberOfLanes, null);
        }
        int inputHorses = chooseNumberOfHorsesGivenNumberOfLanes("How many horses would you like: ", inputLanes);
        inputHorses = inputHorses - Horse.horseCounter;
        for (int numberOfHorses = 0; numberOfHorses < inputHorses; numberOfHorses++) {
            int lane = pickOneOfTheLanes("\nChoose a lane to add this horse to [1 - "+inputLanes+"]: ", inputLanes);
            while (horses.get(lane-1) != null) {
                System.out.println("Lane "+lane+" is taken by another horse.");
                lane = pickOneOfTheLanes("\nChoose a lane to add this horse to [1 - "+inputLanes+"]: ", inputLanes);
            }
            horses.set(lane-1, createHorse(lane));
        }
    }

    /**
     * Displays the winner of the race and adjusts the confidence of the horses.
     * And adjust the total races, total wins, and win rate of the horse
     */
    private void showWinner() throws IOException {
        for (Horse horse : horses) {
            if (horse == null) continue;
            horse.setTotalRaces(horse.getTotalRaces()+1);
            if (raceWonBy(horse)) {
                System.out.println("\n\n"+horse.getName()+" has won the Race!");
                horse.setConfidence(horse.getConfidence()*1.1);
                horse.setTotalWins(horse.getTotalWins()+1);
                if (horse.isBetPlacedOn()) {
                    BettingSystem.balance = horse.getWinnings();
                    System.out.println("Current balance: "+BettingSystem.balance);
                }
            } else {
                horse.setConfidence(horse.getConfidence()*0.9);
                if (horse.isBetPlacedOn()) {
                    System.out.println("The horse you placed the bet on has lost");
                    System.out.println("Current balance: "+BettingSystem.balance);
                }
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
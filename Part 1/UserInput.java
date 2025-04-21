import java.util.Scanner;

/**
 * Class to handle User input
 * 
 * @author Peter Bojthe
 * @version 16/04/25
 * 
 */

public class UserInput implements UserOptionInterface, UserInputInterface {

    /**
     * Prompts the user for a number input and validates it.
     * 
     * @param statement the prompt message to display to the user.
     * @return the validated integer input from the user.
     */
    @Override
    public int inputNumber(String statement) {
        String input = inputString(statement);
        boolean valid = validateNumber(input);
        while (!valid) {
            input = inputString(statement);
            valid = validateNumber(input);
        }
        return Integer.parseInt(input);
    }

    /**
     * Prompts the user for a number input and validates it as a double.
     * 
     * @param statement the prompt message to display to the user.
     * @return the validated double input from the user.
     */
    @Override
    public double inputDouble(String statement) {
        String input = inputString(statement);
        boolean valid = validateDouble(input);
        while (!valid) {
            input = inputString("Invalid input.\n"+statement+": ");
            valid = validateDouble(input);
        }
        return Double.parseDouble(input);
    }

    /**
     * Validates whether the given string can be parsed as a double.
     * 
     * @param input the string to validate.
     * @return true if the input is a valid double, false otherwise.
     */
    @SuppressWarnings("UnnecessaryTemporaryOnConversionFromString")
    @Override
    public boolean validateDouble(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(input.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Prompts the user for a character input and validates it.
     * 
     * @param statement the prompt message to display to the user.
     * @return the validated character input from the user.
     */
    @Override
    public char inputCharacter(String statement) {
        String input = inputString(statement);
        boolean valid = validateCharacter(input);
        while (!valid) {
            input = inputString(statement);
            valid = validateCharacter(input);
        }
        return input.charAt(0);
    }

    /**
     * Validates if the input string is a valid character.
     * 
     * @param input the string to validate.
     * @return true if the input is a valid character, false otherwise.
     */
    @Override
    public boolean validateCharacter(String input) {
        return (input.length() != 0);
    }

    /**
     * Prompts the user for a string input.
     * 
     * @param statement the prompt message to display to the user.
     * @return the string input from the user.
     */
    @Override
    public String inputString(String statement) {
        @SuppressWarnings("resource") // Resource Leak: scanner never closed
        Scanner scanner = new Scanner(System.in);
        System.out.print(statement);
        return scanner.nextLine();
    }

    /**
     * Validates if the input string is a valid number.
     * 
     * @param input the string to validate.
     * @return true if the input is a valid number, false otherwise.
     */
    @Override
    public boolean validateNumber(String input) {
        if (input.length() == 0) return false;
        for (int i = 0; i < input.length(); i++) {
            char x = input.charAt(i);
            if (x < '0' || x > '9') return false;
        }
        return true;
    }

    /**
     * Prompts the user for a yes/no input and validates it.
     * 
     * @param statement the prompt message to display to the user.
     * @return true if the user input is "1" (yes), false if "0" (no).
     */
    @Override
    public boolean askYesNo(String statement) {
        String input = inputString(statement);
        boolean valid = yesNo(input);
        while (!valid) {
            input = inputString(statement);
            valid = yesNo(input);
        }
        return (input.equals("1"));
    }

    /**
     * Validates if the input is a valid yes/no response.
     * 
     * @param input the string to validate.
     * @return true if the input is "1" or "0", false otherwise.
     */
    @Override
    public boolean yesNo(String input) {
        return (input.equals("1") || input.equals("0"));
    }

    /**
     * Prompts the user to choose a track length and validates it.
     * 
     * @param statement the prompt message to display to the user.
     * @return the validated track length input from the user.
     */
    @Override
    public int chooseTrackLength(String statement) {
        int input = inputNumber(statement);
        while (input < 25 || input > 100) {
            input = inputNumber(statement);
        }
        return input;
    }

    /**
     * Prompts the user to choose a horse
     * 
     * @param statement the prompt message to display to the user.
     * @return the validated horse number
     */
    @Override
    public int pickAnyHorse(String statement) {
        int input = inputNumber(statement);
        while (input < 1  || input > 8) {
            System.out.println("Invalid index for lane.");
            input = inputNumber(statement);
        }
        return input;
    }

    /**
     * Prompts the user to pick a lane and validates it.
     * 
     * @param statement the prompt message to display to the user.
     * @param max the maximum number of lanes currently used.
     * @return the validated lane number input from the user.
     */
    @Override
    public int pickOneOfTheLanes(String statement, int max) {
        int input = inputNumber(statement);
        while (input < 1  || input > max) {
            System.out.println("Invalid index for lane.");
            input = inputNumber(statement);
        }
        return input;
    }

    /**
     * Prompts the user to choose the number of lanes and validates it.
     * 
     * @param statement the prompt message to display to the user.
     * @return the validated number of lanes input from the user.
     */
    @Override
    public int chooseNumberOfLanes(String statement) {
        System.out.println("\n\nNumber of lanes must be greater than 2 to start race simulation.");
        int input = inputNumber(statement);
        while (input < 2 || input > 8) {
            System.out.println("Number of lanes must be greater than 2 to start race simulation.");
            input = inputNumber(statement);
        }
        return input;
    }

    /**
     * Prompts the user to choose the number of horses and validates it.
     * 
     * @param statement the prompt message to display to the user.
     * @return the validated number of horses input from the user.
     */
    @Override
    public int chooseNumberOfHorses(String statement) {
        int input = inputNumber(statement);
        while (input < 2 || input > 8) {
            System.out.println("Number of horses must be greater than 2 and less than or equal to 8.");
            input = inputNumber(statement);
        }
        return input;
    }

    /**
     * Prompts the user to choose the number of horses given a specific number of lanes and validates it.
     * 
     * @param statement the prompt message to display to the user.
     * @param numberOfLanes the number of lanes available.
     * @return the validated number of horses input from the user.
     */
    @Override
    public int chooseNumberOfHorsesGivenNumberOfLanes(String statement, int numberOfLanes) {
        int input = inputNumber(statement);
        while (input < 2 || input > numberOfLanes) {
            System.out.println("Number of horses must be greater than 2 and less than or equal to the number of lanes.");
            input = inputNumber(statement);
        }
        return input;
    }

    /**
     * this method ask for the amount the user want to spend on the race and 
     * returns this value
     * @param statement the prompt given to the user
     * @return the amount of money put on the race
     */
    @Override
    public double placeBet(String statement) {
        if (BettingSystem.balance == 0.0) {
            System.out.println("Balance is £0. Cannot place Bets.");
            return 0.0;
        }

        double bet = inputDouble(statement);
        while (bet <= 0.0 || bet > BettingSystem.balance) {
            if (bet <= 0.0) System.out.println("Bet cannot be less than £0.");
            if (bet > BettingSystem.balance) System.out.println("Not eneough money to place bet.");
            System.out.println("Try again.");
            bet = inputDouble(statement);
        }
        System.out.println("Bet has been placed.");
        BettingSystem.removeLoss(bet);
        System.out.println("Current Balance: "+BettingSystem.balance+"\n");
        return bet;
    }
}
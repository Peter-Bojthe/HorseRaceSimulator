import java.util.Scanner;

public class UserInput implements UserOptionInterface, UserInputInterface {

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

    @Override
    public String inputString(String statement) {
        @SuppressWarnings("resource") // Resource Leak: scanner never closed
        Scanner scanner = new Scanner(System.in);
        System.out.println(statement);
        return scanner.nextLine();
    }

    @Override
    public boolean validateNumber(String input) {
        if (input.length() == 0) return false;
        for (int i = 0; i < input.length(); i++) {
            char x = input.charAt(i);
            if (x < '0' || x > '9') return false;
        }
        return true;
    }

    @Override
    public boolean validateCharacter(String input) {
        return (input.length() != 0);
    }

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

    @Override
    public boolean yesNo(String input) {
        return input.equals("1") || input.equals("2");
    }

    @Override
    public int chooseTrackLength(String statement) {
        int input = inputNumber(statement);
        while (input < 3 || input > 100) {
            input = inputNumber(statement);
        }
        return input;
    }

    @Override
    public int pickOneOfTheLanes(String statement) {
        int input = inputNumber(statement);
        while (input < 1  || input > 8) {
            System.out.println("Invalid index for lane.");
            input = inputNumber(statement);
        }
        return input;
    }

    @Override
    public int chooseNumberOfLanes(String statement) {
        System.out.println("Number of lanes must be greater than 2 to start race simulation.");
        int input = inputNumber(statement);
        while (input < 2 || input > 8) {
            System.out.println("Number of lanes must be greater than 2 to start race simulation.");
            input = inputNumber(statement);
        }
        return input;
    }

    @Override
    public int chooseNumberOfHorses(String statement) {
        int input = inputNumber(statement);
        while (input < 2 || input > 8) {
            System.out.println("Number of horses must be greater than 2 and less than or equal to 8.");
            input = inputNumber(statement);
        }
        return input;
    }

    @Override
    public int chooseNumberOfHorsesGivenNumberOfLanes(String statement, int numberOfLanes) {
        int input = inputNumber(statement);
        while (input < 2 || input > numberOfLanes) {
            System.out.println("Number of horses must be greater than 2 and less than or equal to the number of lanes.");
            input = inputNumber(statement);
        }
        return input;
    }
}

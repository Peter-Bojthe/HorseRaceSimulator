import java.util.Scanner;

public class UserInput {
    public static boolean askYesNo(String statement) {
        String input = inputString(statement);
        boolean valid = yesNo(input);
        while (!valid) {
            input = inputString(statement);
            valid = yesNo(input);
        }
        return (input.equals("1"));
    }

    public static int trackLength() {
        int input = numberInput("Length of Race [25m, 100m]: ");
        while (input < 3 || input > 100) {
            input = numberInput("Length of Race [25m, 100m]: ");
        }
        return input;
    }

    public static int amountOfLanes(String statement) {
        int input = numberInput(statement);
        while (input < 1 || input > 8) {
            input = numberInput(statement);
        }
        return input;
    }

    private static boolean checkCharacter(String input) {
        return (input.length() != 0);
    }

    public static char inputCharacter(String statement) {
        String input = inputString(statement);
        boolean valid = checkCharacter(input);
        while (!valid) {
            input = inputString(statement);
            valid = checkCharacter(input);
        }
        return input.charAt(0);
    }

    private static boolean checkNumber(String input) {
        if (input.length() == 0) return false;
        for (int i = 0; i < input.length(); i++) {
            char x = input.charAt(i);
            if (x < '0' || x > '9') return false;
        }
        return true;
    }

    public static int numberInput(String statement) {
        String input = inputString(statement);
        boolean valid = checkNumber(input);
        while (!valid) {
            input = inputString(statement);
            valid = checkNumber(input);
        }
        return Integer.parseInt(input);
    }

    public static String inputString(String statement) {
        @SuppressWarnings("resource") // Resource Leak: scanner never closed
        Scanner scanner = new Scanner(System.in);
        System.out.println(statement);
        return scanner.nextLine();
    }

    private static boolean yesNo(String input) {
        return (input.equals("1") || input.equals("0"));
    }
}

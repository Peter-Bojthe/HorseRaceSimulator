/**
 * Interface for handling user input and validation.
 * This interface defines methods for prompting the user for input, validating the input,
 * and ensuring it meets specific criteria (e.g., numbers, characters, or strings).
 * It is designed to be implemented by classes that handle user interaction and input validation.
 * 
 * @author Peter Bojthe
 * @version 23/03/25
 * 
 */
interface UserInputInterface {

    /**
     * Prompts the user to input a number and validates it.
     *
     * @param statement the prompt message to display to the user.
     * @return the validated integer input from the user.
     */
    public int inputNumber(String statement);

    /**
     * Prompts the user to input a single character and validates it.
     *
     * @param statement the prompt message to display to the user.
     * @return the validated character input from the user.
     */
    public char inputCharacter(String statement);

    /**
     * Prompts the user to input a string.
     *
     * @param statement the prompt message to display to the user.
     * @return the string input from the user.
     */
    public String inputString(String statement);

    /**
     * Validates if the input string represents a valid number.
     *
     * @param input the string to validate.
     * @return true if the input is a valid number, false otherwise.
     */
    public boolean validateNumber(String input);

    /**
     * Validates if the input string represents a valid single character.
     *
     * @param input the string to validate.
     * @return true if the input is a valid single character, false otherwise.
     */
    public boolean validateCharacter(String input);
}
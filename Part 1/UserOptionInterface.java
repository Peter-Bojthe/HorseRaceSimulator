/**
 * Interface for handling user input options related to race configuration and choices.
 * This interface defines methods for prompting the user, validating input, and making selections.
 * It is designed to be implemented by classes that handle user interaction for race setup.
 * 
 * @author Peter Bojthe
 * @version 23/03/25
 * 
 */
interface UserOptionInterface {

    /**
     * Prompts the user with a yes/no question and validates the response.
     *
     * @param statement the prompt message to display to the user.
     * @return true if the user selects "yes" (1), false if the user selects "no" (2).
     */
    public boolean askYesNo(String statement);

    /**
     * Validates if the input is a valid yes/no response.
     *
     * @param input the user's input to validate.
     * @return true if the input is "1" or "0", false otherwise.
     */
    public boolean yesNo(String input);

    /**
     * Prompts the user to choose the length of the race track and validates the input.
     * The track length must be within a valid range (e.g., 25 to 100 units).
     *
     * @param statement the prompt message to display to the user.
     * @return the validated track length chosen by the user.
     */
    public int chooseTrackLength(String statement);

    /**
     * Prompts the user to choose the number of lanes for the race and validates the input.
     * The number of lanes must be within a valid range (e.g., 2 to 8 lanes).
     *
     * @param statement the prompt message to display to the user.
     * @return the validated number of lanes chosen by the user.
     */
    public int chooseNumberOfLanes(String statement);

    /**
     * Prompts the user to choose the number of horses for the race and validates the input.
     * The number of horses must be within a valid range (e.g., 2 to 8 horses).
     *
     * @param statement the prompt message to display to the user.
     * @return the validated number of horses chosen by the user.
     */
    public int chooseNumberOfHorses(String statement);

    /**
     * Prompts the user to choose the number of horses given a specific number of lanes and validates the input.
     * The number of horses must not exceed the number of lanes.
     *
     * @param statement the prompt message to display to the user.
     * @param numberOfLanes the number of lanes available for the race.
     * @return the validated number of horses chosen by the user.
     */
    public int chooseNumberOfHorsesGivenNumberOfLanes(String statement, int numberOfLanes);

    /**
     * Prompts the user to pick a lane from the available lanes and validates the input.
     * The lane number must be within the range of available lanes (1 to max).
     *
     * @param statement the prompt message to display to the user.
     * @param max the maximum number of lanes available.
     * @return the validated lane number chosen by the user.
     */
    public int pickOneOfTheLanes(String statement, int max);
}
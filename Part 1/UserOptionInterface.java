interface UserOptionInterface {
    public boolean askYesNo(String statement);
    public boolean yesNo(String input);
    public int chooseTrackLength(String statement);
    public int chooseNumberOfLanes(String statement);
    public int chooseNumberOfHorses(String sattement);
    public int chooseNumberOfHorsesGivenNumberOfLanes(String statement, int numberOfLanes);
    public int pickOneOfTheLanes(String statement);
}

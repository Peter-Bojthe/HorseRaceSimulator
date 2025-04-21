/**
 * Class to handle all of the bets
 * @author Peter Bojthe
 * @version 18/04/25
 */
public class BettingSystem {
    // Balance initially set to 100.
    public static double balance = 100.0;

    /**
     * Calculate the possible winnings of a horse
     * @param horse horse we are calculating the winnings of
     * @param bet the amount of money placed on the race by the user
     * @return return the possible winnings by the hore
     */
    public static double calculateWinnings(Horse horse, double bet) {
        return ((horse.getConfidence()+horse.getWinRate()+1.1)*bet);
    }

    /**
     * add to balance
     * @param wonMoney money to add to balance
     */
    public static void addWinnings(double wonMoney) {
        balance = balance + wonMoney;
    }

    /**
     * decrease balance
     * @param lostMoney amount to decrease balcance by
     */
    public static void removeLoss(double lostMoney) {
        balance = balance - lostMoney;
    }
}


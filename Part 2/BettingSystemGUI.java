/**
 * Simple Betting System for the GUI veraion of the simulation
 * 
 * @author Peter Bojthe
 * @version 1.0.0
*/
public class BettingSystemGUI {
    // Starting balance
    public static double balance = 100.0;

    /** @return the amount of money a horse can win, @param horse is the horse, @param bet amount of money placed by the user */
    public static double calculateWinnings(HorseGUI horse, double bet) { return ((Math.round((horse.getConfidence()+horse.getWinRate()+1.1)*bet)+bet)*100)/100.0; }

    /** add money won to the user balannce, @param wonMoney is how much the user won */
    public static void addWinnings(double wonMoney) { balance = balance + wonMoney; }

    /** remove the bets placed from the balance @param lostMoney the bet placed amount */
    public static void removePayment(double lostMoney) { balance = balance - lostMoney; }
}
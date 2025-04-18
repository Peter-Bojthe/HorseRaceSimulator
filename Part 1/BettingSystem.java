public class BettingSystem {
    public static double balance = 100.0;

    public static double calculateWinnings(Horse horse, double bet) {
        return ((horse.getConfidence()+horse.getWinRate()+1.1)*bet);
    }

    public static void addWinnings(double wonMoney) {
        balance = balance + wonMoney;
    }

    public static void removeLoss(double lostMoney) {
        balance = balance - lostMoney;
    }
}
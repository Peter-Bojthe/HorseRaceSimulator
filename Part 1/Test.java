public class Test {
    public static void main(String[] args) {
        Horse horse = new Horse('1', "name", 0.5);
        
        // all getter methods 
        System.out.println("Confidence: "+horse.getConfidence());
        System.out.println("Distance: "+horse.getDistanceTravelled());
        System.out.println("Name: "+horse.getName());
        System.out.println("Symbol: "+horse.getSymbol());
        System.out.println();

        // all setter methods
        horse.setConfidence(0.75);
        horse.setSymbol('2');
        horse.moveForward();

        // See changes of setter methods
        System.out.println("New Confidence: "+horse.getConfidence());
        System.out.println("New Symbol: "+horse.getSymbol());
        System.out.println("New Distance: "+horse.getDistanceTravelled());
        System.out.println();

        // Set back to start
        horse.goBackToStart();
        System.out.println("Reset Distance to Start...");
        System.out.println("New Distance: "+horse.getDistanceTravelled());
        System.out.println();

    }
}
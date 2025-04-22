import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * A graphical horse race simulator that allows users to configure and run races.
 * 
 * @author Peter Bojthe
 * @version 1.0.1
 */
public class HorseRaceClassGUI {
    private static final int RACE_LENGTH = 50;   // The length of the race track in units
    private JFrame frame;                        // The main application window 
    private JTextArea raceArea;                  // Text area displaying the race visualization
    private final ArrayList<HorseGUI> horses;    // List of horses participating in the race
    private int numberOfLanes;                   // Total number of available lanes
    private int numberOfHorses;                  // Number of horses in the current race
    private Timer raceTimer;                     // Timer controlling the race progression

    /**
     * Main entry point for the application.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(HorseRaceClassGUI::new);
    }

    /**
     * Constructs a new horse race simulator and shows the configuration window.
     */
    public HorseRaceClassGUI() {
        horses = new ArrayList<>();
        showConfigurationWindow();
    }

    /**
     * Displays the initial configuration window where users set up the race parameters.
     * Allows setting the number of lanes and horses.
     */
    @SuppressWarnings("unused")
    private void showConfigurationWindow() {
        frame = new JFrame("Horse Race Simulator Configuration");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel configPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        configPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Configuration UI elements
        JLabel laneLabel = new JLabel("Number of Lanes (2-8):");
        JSpinner laneSpinner = new JSpinner(new SpinnerNumberModel(4, 2, 8, 1));

        JLabel horseLabel = new JLabel("Number of Horses (2+):");
        JSpinner horseSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 8, 1));

        JButton nextButton = new JButton("Enter Horse Details");

        configPanel.add(laneLabel);
        configPanel.add(laneSpinner);
        configPanel.add(horseLabel);
        configPanel.add(horseSpinner);
        configPanel.add(new JLabel());
        configPanel.add(nextButton);

        frame.add(configPanel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        nextButton.addActionListener(e -> {
            numberOfLanes = (Integer) laneSpinner.getValue();
            numberOfHorses = (Integer) horseSpinner.getValue();

            if (numberOfHorses > numberOfLanes) {
                JOptionPane.showMessageDialog(frame, "Number of horses cannot exceed number of lanes.");
                return;
            }

            frame.dispose();
            horses.clear();
            promptHorseDetails();
        });
    }

    /**
     * Prompts the user to enter details for each horse in the race.
     * Collects name, symbol, and lane assignment for each horse.
     */
    private void promptHorseDetails() {
        for (int i = 0; i < numberOfHorses; i++) {
            String name = JOptionPane.showInputDialog(null, "Enter name for Horse " + (i + 1) + ":");
            if (name == null || name.isEmpty()) name = "Horse" + (i + 1);

            String symbolStr = JOptionPane.showInputDialog(null, "Enter ONE character for " + name + ":");
            while (symbolStr == null || symbolStr.length() != 1) {
                symbolStr = JOptionPane.showInputDialog(null, "Invalid input. Enter ONE character for " + name + ":");
            }
            char symbol = symbolStr.charAt(0);

            int lane = -1;
            while (lane < 1 || lane > numberOfLanes || isLaneTaken(lane)) {
                String laneStr = JOptionPane.showInputDialog(null, "Choose a lane for " + name + " (1 to " + numberOfLanes + "):");
                try {
                    lane = Integer.parseInt(laneStr);
                } catch (NumberFormatException ignored) {
                    lane = -1;
                }
                if (isLaneTaken(lane)) {
                    JOptionPane.showMessageDialog(null, "Lane already taken. Choose another.");
                    lane = -1;
                }
            }
            horses.add(new HorseGUI(name, symbol, 0.25, lane));
        }

        showRaceWindow();
        runRace();
    }

    /**
     * Checks if a specific lane is already occupied by another horse.
     * 
     * @param lane The lane number to check
     * @return true if the lane is already taken, false otherwise
     */
    private boolean isLaneTaken(int lane) {
        for (HorseGUI h : horses) {
            if (h.getLaneNumber() == lane) return true;
        }
        return false;
    }

    /**
     * Displays the main race window with the race visualization area.
     */
    @SuppressWarnings("unused")
    private void showRaceWindow() {
        frame = new JFrame("Horse Race Simulator");
        frame.setSize(1000, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        raceArea = new JTextArea();
        raceArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        raceArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(raceArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton restartButton = new JButton("Restart");
        JButton exitButton = new JButton("Exit");

        restartButton.addActionListener(e -> {
            frame.dispose();
            SwingUtilities.invokeLater(() -> new HorseRaceClassGUI());
        });

        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(restartButton);
        buttonPanel.add(exitButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Starts the race simulation with a timer that updates the race state periodically.
     */
    @SuppressWarnings("unused")
    private void runRace() {
        raceTimer = new Timer(10, e -> {
            boolean allFell = true;
            boolean raceFinished = false;

            // Update each horse's position
            for (HorseGUI horse : horses) {
                if (!horse.isHorseFallen() && horse.getHorseDistance() < RACE_LENGTH) {
                    moveHorse(horse);
                }
                if (!horse.isHorseFallen()) {
                    allFell = false;
                }
                if (horse.getHorseDistance() >= RACE_LENGTH) {
                    raceFinished = true;
                }
            }

            printRace();

            // Check race completion conditions
            if (raceFinished || allFell) {
                raceTimer.stop();
                announceWinner(raceFinished ? getWinner() : null);
            }
        });
        raceTimer.start();
    }

    /**
     * Moves a horse forward or makes it fall based on its confidence.
     * 
     * @param horse The horse to move
     */
    private void moveHorse(HorseGUI horse) {
        if (!horse.isHorseFallen()) {
            if (Math.random() < horse.getHorseConfidence()) {
                horse.setHorseDistance(horse.getHorseDistance() + 1);
            }
            if (Math.random() < 0.1 * horse.getHorseConfidence() * horse.getHorseConfidence()) {
                horse.setHorseFallen(true);
            }
        }
    }

    /**
     * Generates and displays the current race state in the text area.
     */
    private void printRace() {
        StringBuilder builder = new StringBuilder();
        builder.append("=".repeat(RACE_LENGTH + 3)).append("\n");

        for (int i = 1; i <= numberOfLanes; i++) {
            HorseGUI h = getHorseInLane(i);
            builder.append(printLane(h)).append("\n");
        }

        builder.append("=".repeat(RACE_LENGTH + 3)).append("\n");
        raceArea.setText(builder.toString());
    }

    /**
     * Gets the horse in a specific lane.
     * 
     * @param laneNumber The lane number to check
     * @return The horse in the specified lane, or null if empty
     */
    private HorseGUI getHorseInLane(int laneNumber) {
        for (HorseGUI horse : horses) {
            if (horse.getLaneNumber() == laneNumber) {
                return horse;
            }
        }
        return null;
    }

    /**
     * Generates the string representation of a single lane.
     * 
     * @param horse The horse in the lane (null if empty)
     * @return Formatted string representing the lane's current state
     */
    private String printLane(HorseGUI horse) {
        StringBuilder sb = new StringBuilder("|");
        if (horse == null) {
            sb.append(" ".repeat(RACE_LENGTH + 1)).append("| Empty Lane");
        } else {
            int before = horse.getHorseDistance();
            int after = RACE_LENGTH - before;
            sb.append(" ".repeat(before));
            sb.append(horse.isHorseFallen() ? '⌢' : horse.getHorseSymbol());
            sb.append(" ".repeat(after));
            sb.append("| Lane ").append(horse.getLaneNumber());
            sb.append(": ").append(horse.getHorseName());
            sb.append(" (Conf: ").append(String.format("%.2f", horse.getHorseConfidence())).append(")");
        }
        return sb.toString();
    }

    /**
     * Determines the winning horse if the race has finished.
     * 
     * @return The winning horse, or null if no winner yet
     */
    private HorseGUI getWinner() {
        for (HorseGUI horse : horses) {
            if (horse.getHorseDistance() >= RACE_LENGTH && !horse.isHorseFallen()) {
                return horse;
            }
        }
        return null;
    }

    /**
     * Announces the race winner or no winner if all horses fell.
     * 
     * @param winner The winning horse, or null if no winner
     */
    private void announceWinner(HorseGUI winner) {
        String message = (winner == null)
                ? "All horses have fallen! No winner."
                : "🏁 " + winner.getHorseName() + " has won the race!";
        JOptionPane.showMessageDialog(frame, message);
    }
}
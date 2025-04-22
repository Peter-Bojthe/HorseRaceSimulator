import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * GUI-based horse race simulator with straight and oval tracks.
 * 
 * @author Peter Bojthe
 * @version 1.0.2
 */
public class HorseRaceClassGUI {
    private enum TrackType {STRAIGHT, OVAL}
    private int trackLength = 20;
    private TrackType trackType = TrackType.STRAIGHT;
    private int numberOfLanes = 2;
    private int numberOfHorses = 2;
    private final List<HorseGUI> horses = new ArrayList<>();
    private boolean raceFinished = false;
    private JFrame frame;
    private JTextArea raceDisplay;
    private Timer raceTimer;
    private List<HorseGUI> originalHorses = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HorseRaceClassGUI().start());
    }

    /**
     * Launches the configuration dialog to set up a new race.
     */
    public void start() {
        showConfigurationDialog();
    }

    /**
     * Display to configure race parameters like track type, length, lanes, and horses.
     */
    @SuppressWarnings("unused")
    private void showConfigurationDialog() {
        frame = new JFrame("Horse Race Configuration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JComboBox<TrackType> trackTypeCombo = new JComboBox<>(TrackType.values());
        JSpinner lengthSpinner = new JSpinner(new SpinnerNumberModel(50, 20, 100, 5));
        JSpinner laneSpinner = new JSpinner(new SpinnerNumberModel(4, 2, 8, 1));
        JSpinner horseSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 8, 1));

        panel.add(new JLabel("Track Type:")); panel.add(trackTypeCombo);
        panel.add(new JLabel("Track Length (20-100):")); panel.add(lengthSpinner);
        panel.add(new JLabel("Number of Lanes (2-8):")); panel.add(laneSpinner);
        panel.add(new JLabel("Number of Horses (2+):")); panel.add(horseSpinner);
        panel.add(new JLabel());

        JButton startButton = new JButton("Start Race");
        panel.add(startButton);

        startButton.addActionListener(e -> {
            trackType = (TrackType) trackTypeCombo.getSelectedItem();
            trackLength = (Integer) lengthSpinner.getValue();
            numberOfLanes = (Integer) laneSpinner.getValue();
            numberOfHorses = (Integer) horseSpinner.getValue();

            if (numberOfHorses > numberOfLanes) {
                JOptionPane.showMessageDialog(frame, "Cannot have more horses than lanes!");
                return;
            }

            frame.dispose();
            setupRace();
        });

        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Prompts user to configure each horse and assigns them to a lane.
     */
    private void setupRace() {
        horses.clear();

        for (int i = 0; i < numberOfHorses; i++) {
            String name = JOptionPane.showInputDialog("Enter name for Horse " + (i + 1) + ":");
            if (name == null || name.trim().isEmpty()) name = "Horse " + (i + 1);

            String symbol;
            do {
                symbol = JOptionPane.showInputDialog("Enter symbol for " + name + " (single character):");
            } while (symbol == null || symbol.length() != 1);

            int lane;
            do {
                try {
                    lane = Integer.parseInt(JOptionPane.showInputDialog("Assign lane (1-" + numberOfLanes + ") for " + name + ":"));
                } catch (NumberFormatException e) {
                    lane = -1;
                }
            } while (lane < 1 || lane > numberOfLanes || isLaneTaken(lane));

            HorseGUI horse = new HorseGUI(name, symbol.charAt(0), 0.25, lane);
            horses.add(horse);
        }

        // Clone horses for replay
        originalHorses = new ArrayList<>();
        for (HorseGUI h : horses) {
            originalHorses.add(new HorseGUI(h.getName(), h.getSymbol(), h.getConfidence(), h.getLane()));
        }

        createRaceWindow();
        startRace();
    }

    /**
     * Checks if a lane is already taken.
     * @param lane the lane to check
     * @return true if the lane is taken
     */
    private boolean isLaneTaken(int lane) {
        return horses.stream().anyMatch(h -> h.getLane() == lane);
    }

    /**
     * Creates and displays the main race window for the GUI-based horse race simulator.
     */
    @SuppressWarnings("unused")
    private void createRaceWindow() {
        frame = new JFrame("Horse Race");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        raceDisplay = new JTextArea();
        raceDisplay.setFont(new Font("Monospaced", Font.PLAIN, 14));
        raceDisplay.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(raceDisplay);
        frame.add(scrollPane, BorderLayout.CENTER);

        JButton restartButton = new JButton("Restart");
        JButton replayButton = new JButton("Replay");

        restartButton.addActionListener(e -> {
            frame.dispose();
            new HorseRaceClassGUI().start();
        });

        replayButton.addActionListener(e -> {
            frame.dispose();
            replayRaceWithSameHorses();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(restartButton);
        buttonPanel.add(replayButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Resize window based on track length and lanes
        SwingUtilities.invokeLater(() -> {
            FontMetrics fm = raceDisplay.getFontMetrics(raceDisplay.getFont());

            int maxLineLength = trackLength + 30; 
            int charWidth = fm.charWidth('M');  
            int charHeight = fm.getHeight();     

            int width = charWidth * (maxLineLength + 4);
            int height = charHeight * (numberOfLanes + 8);
            frame.setSize(width, height);

            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }


    /**
     * Replays the race using the same horse configurations (name, symbol, confidence, lane)
     */
    private void replayRaceWithSameHorses() {
        List<HorseGUI> previousHorses = new ArrayList<>(horses);
        horses.clear();

        for (HorseGUI oldHorse : previousHorses) {
            horses.add(new HorseGUI(
                oldHorse.getName(),
                oldHorse.getSymbol(),
                oldHorse.getConfidence(),
                oldHorse.getLane()
            ));
        }

        raceFinished = false;
        createRaceWindow();
        startRace();
    }

    /**
     * Starts the race timer and handles horse movement and falling.
     */
    @SuppressWarnings("unused")
    private void startRace() {
        raceTimer = new Timer(100, e -> {
            if (raceFinished) return;

            for (HorseGUI horse : horses) {
                if (!horse.hasFallen()) {
                    if (Math.random() < horse.getConfidence()) horse.moveForward();

                    if (trackType == TrackType.OVAL) {
                        int lapLength = trackLength * 2;
                        if (horse.getDistance() > 0 && horse.getDistance() % lapLength == 0)
                            horse.completeLap();
                    }

                    if (Math.random() < 0.1 * horse.getConfidence() * horse.getConfidence()) horse.fall();
                }
            }

            updateDisplay();
            checkRaceCompletion();
        });

        raceTimer.start();
    }

    /**
     * Updates the race display in the GUI.
     */
    private void updateDisplay() {
        StringBuilder sb = new StringBuilder();

        if (trackType == TrackType.STRAIGHT) {
            sb.append("=".repeat(trackLength + 2)).append("\n");

            for (int lane = 1; lane <= numberOfLanes; lane++) {
                HorseGUI horse = getHorseInLane(lane);
                sb.append("|");

                if (horse != null) {
                    int pos = Math.min(horse.getDistance(), trackLength);
                    sb.append(" ".repeat(pos))
                      .append(horse.hasFallen() ? '?' : horse.getSymbol())
                      .append(" ".repeat(trackLength - pos));
                } else {
                    sb.append(" ".repeat(trackLength));
                }

                sb.append("| ");
                if (horse != null) {
                    sb.append("Lane ").append(horse.getLane()).append(": ").append(horse.getName()).append(" (Confidence: ").append((int)(horse.getConfidence() * 100)).append("%)");
                } else {
                    sb.append("Empty Lane");
                }
                sb.append("\n");
            }

            sb.append("=".repeat(trackLength + 2)).append("\n");
        } else {
            // Oval track display
            sb.append(" ").append("=".repeat(trackLength + 1)).append("\n");

            for (int lane = 1; lane <= numberOfLanes; lane++) {
                HorseGUI horse = getHorseInLane(lane);
                sb.append("(");

                if (horse != null) {
                    int lapProgress = horse.getDistance() % (trackLength * 2);
                    boolean goingRight = lapProgress < trackLength;
                    int displayPos = goingRight ? lapProgress : (trackLength * 2 - lapProgress);

                    sb.append(" ".repeat(displayPos))
                      .append(horse.hasFallen() ? '?' : horse.getSymbol())
                      .append(" ".repeat(trackLength - displayPos));
                } else {
                    sb.append(" ".repeat(trackLength + 1));
                }

                sb.append(") ");
                if (horse != null) {
                    sb.append("Lane ").append(horse.getLane()).append(": ").append(horse.getName()).append(" (Confidence: ").append((int)(horse.getConfidence() * 100)).append("%)");
                } else {
                    sb.append("Empty Lane");
                }
                sb.append("\n");
            }

            sb.append(" ").append("=".repeat(trackLength + 1)).append("\n");
        }

        raceDisplay.setText(sb.toString());
    }

    /**
     * Finds a horse assigned to a specific lane.
     * @param lane the lane number
     * @return the horse assigned to the lane (or null)
     */
    private HorseGUI getHorseInLane(int lane) {
        return horses.stream().filter(h -> h.getLane() == lane).findFirst().orElse(null);
    }

    /**
     * Checks if the race is over and a winner exists.
     */
    private void checkRaceCompletion() {
        boolean someoneFinished = false;
        boolean allFallen = true;
    
        for (HorseGUI horse : horses) {
            if (!horse.hasFallen()) {
                allFallen = false;
    
                if (trackType == TrackType.STRAIGHT && horse.getDistance() >= trackLength) {
                    someoneFinished = true;
                    break;
                } else if (trackType == TrackType.OVAL && horse.getLapsCompleted() >= 1) {
                    someoneFinished = true;
                    break;
                }
            }
        }
    
        if (someoneFinished || allFallen) {
            raceFinished = true;
            raceTimer.stop();
            announceWinner();
        }
    }
    
    /**
     * Displays a message announcing the race winner or no winner if all horses fell.
     */
    private void announceWinner() {
        HorseGUI winner = horses.stream()
            .filter(h -> !h.hasFallen())
            .filter(h -> trackType == TrackType.STRAIGHT ?
                h.getDistance() >= trackLength :
                h.getLapsCompleted() >= 1)
            .findFirst()
            .orElse(null);

        String message = (winner != null)
            ? "🏆 Winner: " + winner.getName() + "! 🏆"
            : "All horses fell! No winner.";

        JOptionPane.showMessageDialog(frame, message);
    }
}
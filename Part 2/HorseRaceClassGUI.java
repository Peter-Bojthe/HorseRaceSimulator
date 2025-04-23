import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;

/**
 * GUI-based horse race simulator with straight and oval tracks.
 * 
 * @author Peter Bojthe
 * @version 1.0.4
 */
public class HorseRaceClassGUI {
    private final String[] trackTypeChoice = {"STRAIGHT", "OVAL"};
    private final String[] weatherTypeChoice = {"SUNNY", "RAINING", "WET", "MUDDY", "SNOW"};
    private final List<HorseGUI> horses = new ArrayList<>();

    private String trackType = "STRAIGHT";
    private String weatherType = "SUNNY";

    private int trackLength = 20;
    private int numberOfLanes = 2;
    private int numberOfHorses = 2;
    private boolean raceFinished = false;

    private JFrame frame;
    private JTextArea raceDisplay;
    private Timer raceTimer;

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
     * Displays the race and horse configuration window.
     * Users select track type, length, number of lanes, and define each horse.
     */
    @SuppressWarnings("unused")
    private void showConfigurationDialog() {
        frame = new JFrame("Horse Race Configuration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel settingsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Track Settings"));

        JComboBox<String> trackTypeCombo = new JComboBox<>(trackTypeChoice);
        JSpinner lengthSpinner = new JSpinner(new SpinnerNumberModel(20, 20, 100, 5));
        JSpinner laneSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 12, 1));
        JSpinner horseSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 12, 1));

        settingsPanel.add(new JLabel("Track Type:"));
        settingsPanel.add(trackTypeCombo);
        settingsPanel.add(new JLabel("Track Length (20 - 100):"));
        settingsPanel.add(lengthSpinner);
        settingsPanel.add(new JLabel("Number of Lanes (2 - 12):"));
        settingsPanel.add(laneSpinner);
        settingsPanel.add(new JLabel("Number of Horses (2 - 12):"));
        settingsPanel.add(horseSpinner);

        // Panel to hold horse input rows
        JPanel horsesPanel = new JPanel();
        horsesPanel.setLayout(new BoxLayout(horsesPanel, BoxLayout.Y_AXIS));
        horsesPanel.setBorder(BorderFactory.createTitledBorder("Horse Details"));

        List<JTextField> nameFields = new ArrayList<>();
        List<JComboBox<String>> symbolBoxes = new ArrayList<>();
        List<JComboBox<Integer>> laneBoxes = new ArrayList<>();
        List<JComboBox<String>> breedBoxes = new ArrayList<>();
        List<JComboBox<String>> coatBoxes = new ArrayList<>();
        List<JComboBox<String>> saddleBoxes = new ArrayList<>();
        List<JComboBox<String>> horseShoeBoxes = new ArrayList<>();

        horseSpinner.addChangeListener(e -> updateHorseInputs(horsesPanel, nameFields, symbolBoxes, laneBoxes, breedBoxes, coatBoxes, saddleBoxes, horseShoeBoxes, horseSpinner, laneSpinner));
        laneSpinner.addChangeListener(e -> updateHorseInputs(horsesPanel, nameFields, symbolBoxes, laneBoxes, breedBoxes, coatBoxes, saddleBoxes, horseShoeBoxes, horseSpinner, laneSpinner));
        updateHorseInputs(horsesPanel, nameFields, symbolBoxes, laneBoxes, breedBoxes, coatBoxes, saddleBoxes, horseShoeBoxes, horseSpinner, laneSpinner);
        

        JButton startButton = new JButton("Start Race");
        startButton.addActionListener(e -> {
            trackType = (String) trackTypeCombo.getSelectedItem();
            trackLength = (Integer) lengthSpinner.getValue();
            numberOfLanes = (Integer) laneSpinner.getValue();
            numberOfHorses = (Integer) horseSpinner.getValue();

            // Validate lane assignments
            Set<Integer> takenLanes = new HashSet<>();
            horses.clear();

            for (int i = 0; i < numberOfHorses; i++) {
                String name = nameFields.get(i).getText().trim();
                if (name.isEmpty()) name = "Horse " + (i + 1);

                String symbol = symbolBoxes.get(i).getSelectedItem().toString();
                int lane = (Integer) laneBoxes.get(i).getSelectedItem();

                if (takenLanes.contains(lane)) {
                    JOptionPane.showMessageDialog(frame, "Lane " + lane + " is assigned to multiple horses.");
                    return;
                }
                String breed = (String) breedBoxes.get(i).getSelectedItem();
                String coatColour = (String) coatBoxes .get(i).getSelectedItem();

                String saddle = ((String) saddleBoxes.get(i).getSelectedItem());
                String horseShoe = ((String) horseShoeBoxes.get(i).getSelectedItem());
                weatherType = weatherTypeChoice[(int)(Math.random() * weatherTypeChoice.length)];
                double finalConfidence = calculateFinalConfidence(breed, coatColour, saddle, horseShoe, weatherType);

                takenLanes.add(lane);
                horses.add(new HorseGUI(name, symbol, finalConfidence, lane, breed, coatColour, saddle, horseShoe)); 
            }

            frame.dispose();
            createRaceWindow();
            startRace();
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(startButton);

        // Assemble full layout
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(settingsPanel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(horsesPanel);

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }



    /**
     * Updates the horse input fields based on the number of horses and lanes.
     *
     * @param horsesPanel        Panel to which horse input rows are added
     * @param nameFields         List to collect name text fields
     * @param symbolBoxes        List to collect emoji combo boxes
     * @param laneBoxes          List to collect lane number combo boxes
     * @param breedBoxes         List to collect breed of horse
     * @param coatBoxes          List to collect horse coat/ fur
     * @param saddleBoxes        List to collect horse saddle
     * @param horseShoeBoxes     List to collect horse Shoe
     * @param horseSpinner       Spinner that defines the number of horses
     * @param laneSpinner        Spinner that defines the number of lanes
     */
    @SuppressWarnings("unused")
    private void updateHorseInputs(JPanel horsesPanel, List<JTextField> nameFields,
                                    List<JComboBox<String>> symbolBoxes, List<JComboBox<Integer>> laneBoxes,
                                    List<JComboBox<String>> breedBoxes, List<JComboBox<String>> coatBoxes,
                                    List<JComboBox<String>> saddleBoxes, List<JComboBox<String>> horseShoeBoxes,
                                    JSpinner horseSpinner, JSpinner laneSpinner) {
        horsesPanel.removeAll();
        nameFields.clear();
        symbolBoxes.clear();
        laneBoxes.clear();
        breedBoxes.clear();
        coatBoxes.clear();

        int horseCount = (Integer) horseSpinner.getValue();
        int laneCount = (Integer) laneSpinner.getValue();

        final String[] emojiOptions = {"🐎", "🏇", "🐴", "🦄", "🐐", "🐂", "🐘",
                                        "🐓", "🐶", "🐕", "🐩", "🐱", "🐈",
                                        "♔", "♕", "♖", "♗", "♘", "♙", 
                                        "♚", "♛", "♜", "♝", "♞", "♟"};

        final String[] prefixes = {"Thunder", "Midnight", "Shadow", "Lightning", "Silver",
                "Golden", "Diamond", "Black", "White", "Red",
                "Wild", "Crazy", "Majestic", "Royal", "Brave",
                "Flying", "Dancing", "Galloping", "Mystic", "Spirit"};
        final String[] suffixes = {"hoof", "mane", "tail", "storm", "fire",
                "wind", "blaze", "dancer", "chaser", "runner",
                "prince", "king", "queen", "star", "moon",
                "sun", "dream", "whisper", "shadow", "flash"};

        final String[] coats = {"Black", "Chestnut", "Bay", "Palomino", "Grey"};
        final String[] breeds = {"Arabian","Thoroughbred","Mustang","Clydesdale","Appaloosa"};

        final String[] saddles = {"Racing Saddle", "Comfort Saddle", "Old Saddle"};
        final String[] horseShoes = {"Steel Shoes", "Rubber Shoes", "Worn Shoes"};

        for (int i = 0; i < horseCount; i++) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));

            JTextField nameField = new JTextField("Horse " + (i + 1), 10);
            JComboBox<String> emojiBox = new JComboBox<>(emojiOptions);
            JComboBox<Integer> laneBox = new JComboBox<>();
            JComboBox<String> breedBox = new JComboBox<>(breeds);
            JComboBox<String> coatBox = new JComboBox<>(coats);
            JComboBox<String> saddleDropdown = new JComboBox<>(saddles);
            JComboBox<String> horseshoeDropdown = new JComboBox<>(horseShoes);
            JButton randomButton = new JButton("Random");

            for (int l = 1; l <= laneCount; l++) {
                laneBox.addItem(l);
            }

            randomButton.addActionListener(e -> {
                String randomName = prefixes[(int)(Math.random() * prefixes.length)]+" "+suffixes[(int)(Math.random() * suffixes.length)];
                nameField.setText(randomName);
                emojiBox.setSelectedIndex((int)(Math.random() * emojiOptions.length));
                laneBox.setSelectedIndex((int)(Math.random() * laneBox.getItemCount()));
                breedBox.setSelectedIndex((int)(Math.random() * breeds.length));
                coatBox.setSelectedIndex((int)(Math.random() * coats.length));
                saddleDropdown.setSelectedIndex((int)(Math.random() * saddles.length));
                horseshoeDropdown.setSelectedIndex((int)(Math.random() * horseShoes.length));
            });

            row.add(new JLabel("Name:"));
            row.add(nameField);
            row.add(new JLabel("Emoji:"));
            row.add(emojiBox);
            row.add(new JLabel("Lane:"));
            row.add(laneBox);
            row.add(new JLabel("Breed:"));
            row.add(breedBox);
            row.add(new JLabel("Coat:"));
            row.add(coatBox);
            row.add(new JLabel("Saddle:"));
            row.add(saddleDropdown);
            row.add(new JLabel("Shoes:"));
            row.add(horseshoeDropdown);
            row.add(randomButton);

            nameFields.add(nameField);
            symbolBoxes.add(emojiBox);
            laneBoxes.add(laneBox);
            breedBoxes.add(breedBox);
            coatBoxes.add(coatBox);
            saddleBoxes.add(saddleDropdown);
            horseShoeBoxes.add(horseshoeDropdown);
            horsesPanel.add(row);
        }

        horsesPanel.revalidate();
        horsesPanel.repaint();
        frame.pack();
    }

    /**
     * Returns the horse confidence based of its attributes
     * 
     * @param breed      breed of the horse
     * @param coatColour coat/ fur colour of the horse
     * @param saddle     saddle on the horse
     * @param horseshoe  the shoes on the horse
     * @return           the confidence of the horse based of its attributes
     */
    public static double calculateFinalConfidence(String breed, String coatColour, String saddle, String horseshoe, String weather) {
        double baseConfidence = 0.15;
        double multiplier = 1.0;

        multiplier *= switch (breed.toLowerCase()) {
            case "arabian" -> 1.1;
            case "thoroughbred" -> 1.0;
            case "mustang" -> 0.95;
            case "clydesdale" -> 0.95;
            case "appaloosa" -> 0.95;
            default -> 1.0;
        };
        multiplier *= switch (coatColour.toLowerCase()) {
            case "Black" -> 1.05;
            case "Chestnut" -> 1.0;
            case "Bay" -> 0.95;
            case "Palomino" -> 1.05;
            case "Grey" -> 1.0;
            default -> 1.0;
        };
        multiplier *= switch (saddle.toLowerCase()) {
            case "racing saddle" -> 1.1;
            case "comfort saddle" -> 1.0;
            case "old saddle" -> 0.9;
            default -> 1.0;
        };
        multiplier *= switch (horseshoe.toLowerCase()) {
            case "steel shoes" -> 1.05;
            case "rubber shoes" -> 1.0;
            case "worn shoes" -> 0.85;
            default -> 1.0;
        };
        multiplier *= switch (weather.toLowerCase()) {
            case "sunny" -> 1.05;
            case "raining" -> 0.85;
            case "wet" -> 0.75;
            case "muddy" -> 0.65;
            case "snow" -> 0.45;
            default -> 1.0;
        };
        return baseConfidence * multiplier;
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

            int maxLineLength = trackLength + 50; 
            int charWidth = fm.charWidth('M');  
            int charHeight = fm.getHeight();     

            int width = charWidth * (maxLineLength + 4);
            int height = charHeight * (numberOfLanes + 16);
            frame.setSize(width, height);

            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    /**
     * Replays the race using the same horse configurations (name, symbol, confidence, lane, breed, coat, saddle, shoe)
     */
    private void replayRaceWithSameHorses() {
        List<HorseGUI> previousHorses = new ArrayList<>(horses);
        weatherType = weatherTypeChoice[(int)(Math.random() * weatherTypeChoice.length)];
        horses.clear();

        for (HorseGUI oldHorse : previousHorses) {
            double finalConfidence = calculateFinalConfidence(oldHorse.getBreed(), oldHorse.getCoatColour(), oldHorse.getSaddle(), oldHorse.getShoes(), weatherType);
            horses.add(new HorseGUI(oldHorse.getName(),oldHorse.getSymbol(),finalConfidence,oldHorse.getLane(),oldHorse.getBreed(),oldHorse.getCoatColour(),oldHorse.getSaddle(),oldHorse.getShoes()
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

                    if (trackType.equals("OVAL")) {
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

        String weatherSymbol = switch(weatherType.toUpperCase()) {
            case "SUNNY" -> "☀️";
            case "RAINING" -> "🌧️";
            case "WET" -> "💧";
            case "MUDDY" -> "🌧️💧";
            case "SNOW" -> "❄️";
            default -> "🌤️";
        };

        String weatherDeatils = "|Current Weather: "+weatherSymbol+" "+weatherType+" |";

        sb.append("┌").append("─".repeat(weatherDeatils.length()+2)).append("┐\n");
        sb.append(weatherDeatils).append("\n");
        sb.append("└").append("─".repeat(weatherDeatils.length()+2)).append("┘\n");

        if (trackType.equals("STRAIGHT")) {
            sb.append("=".repeat(trackLength + 2)).append("\n");

            for (int lane = 1; lane <= numberOfLanes; lane++) {
                HorseGUI horse = getHorseInLane(lane);
                sb.append("|");

                if (horse != null) {
                    int pos = Math.min(horse.getDistance(), trackLength);
                    sb.append(" ".repeat(pos))
                      .append(horse.hasFallen() ? "\u274C" : horse.getSymbol())
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
                      .append(horse.hasFallen() ? "\u274C" : horse.getSymbol())
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
    
                if (trackType.equals("STRAIGHT")&& horse.getDistance() >= trackLength) {
                    someoneFinished = true;
                    break;
                } else if (trackType.equals("OVAL") && horse.getLapsCompleted() >= 1) {
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
            .filter(h -> trackType.equals("STRAIGHT")?
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
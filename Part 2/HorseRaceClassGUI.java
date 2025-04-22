import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class HorseRaceClassGUI {
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        JFrame welcomeFrame = new JFrame("Horse Race Simulator");
        welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        welcomeFrame.setSize(400, 400);
        welcomeFrame.setResizable(false);
        welcomeFrame.setLayout(new BorderLayout());

        JPanel welcomePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel welcomeLabel = new JLabel("Welcome to Horse Race Simulator!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        welcomePanel.add(welcomeLabel, gbc);

        JButton startButton = new JButton("Configure Simulation");
        startButton.setFont(new Font("Arial", Font.PLAIN, 16));
        startButton.setPreferredSize(new Dimension(180, 40));
        gbc.gridy = 1;
        welcomePanel.add(startButton, gbc);

        JFrame configFrame = new JFrame("Simulation Configuration");
        configFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        configFrame.setSize(400, 400);
        configFrame.setResizable(false);

        startButton.addActionListener(e -> {
            welcomeFrame.dispose();
            showConfiguration(configFrame);
        });

        welcomeFrame.add(welcomePanel, BorderLayout.CENTER);
        welcomeFrame.setLocationRelativeTo(null);
        welcomeFrame.setVisible(true);
    }

    @SuppressWarnings("unused")
    private static void showConfiguration(JFrame frame) {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Configure Your Race", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel configPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel laneLabel = new JLabel("Number of Lanes (2-8):");
        laneLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JSpinner laneSpinner = new JSpinner(new SpinnerNumberModel(4, 2, 8, 1));
        ((JSpinner.DefaultEditor)laneSpinner.getEditor()).getTextField().setEditable(false);

        JLabel horseLabel = new JLabel("Number of Horses (2+):");
        horseLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JSpinner horseSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 4, 1));
        ((JSpinner.DefaultEditor)horseSpinner.getEditor()).getTextField().setEditable(false);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.LINE_END;
        configPanel.add(laneLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        configPanel.add(laneSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.LINE_END;
        configPanel.add(horseLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        configPanel.add(horseSpinner, gbc);

        laneSpinner.addChangeListener(e -> {
            int maxHorses = (Integer) laneSpinner.getValue();
            horseSpinner.setModel(new SpinnerNumberModel(
                Math.min(maxHorses, (Integer)horseSpinner.getValue()),
                2, maxHorses, 1));
        });

        JButton startSimButton = new JButton("Next");
        startSimButton.setFont(new Font("Arial", Font.PLAIN, 16));
        startSimButton.setPreferredSize(new Dimension(150, 40));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startSimButton);

        startSimButton.addActionListener(e -> {
            int lanes = (Integer) laneSpinner.getValue();
            int horses = (Integer) horseSpinner.getValue();

            if (horses > lanes) {
                JOptionPane.showMessageDialog(frame,
                        "Number of horses cannot exceed number of lanes!",
                        "Invalid Configuration", JOptionPane.ERROR_MESSAGE);
                return;
            }

            askForHorseDetails(frame, lanes, horses);
        });

        mainPanel.add(configPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.getContentPane().removeAll();
        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void askForHorseDetails(JFrame frame, int lanes, int horses) {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Character> symbols = new ArrayList<>();

        for (int i = 1; i <= horses; i++) {
            String name = null;
            Character symbol = null;

            while (true) {
                name = JOptionPane.showInputDialog(frame, "Enter name for Horse " + i + ":");
                if (name == null || name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                break;
            }

            while (true) {
                String symStr = JOptionPane.showInputDialog(frame, "Enter 1-character symbol for " + name + ":");
                if (symStr == null || symStr.length() != 1) {
                    JOptionPane.showMessageDialog(frame, "Please enter exactly 1 character.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                symbol = symStr.charAt(0);
                break;
            }

            names.add(name);
            symbols.add(symbol);
        }

        startSimulation(lanes, horses, names, symbols);
    }

    private static void startSimulation(int lanes, int horses, ArrayList<String> names, ArrayList<Character> symbols) {
        StringBuilder sb = new StringBuilder();
        sb.append("Simulation Starting with ").append(lanes).append(" lanes and ").append(horses).append(" horses!\n\n");
        for (int i = 0; i < horses; i++) {
            sb.append("Horse ").append(i + 1).append(": ").append(names.get(i)).append(" (").append(symbols.get(i)).append(")\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString(), "Simulation Starting", JOptionPane.INFORMATION_MESSAGE);
    }
}

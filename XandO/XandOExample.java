package XandO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class XandOExample {

    ArrayList<Integer> playerOneMoves = new ArrayList<>();
    ArrayList<Integer> playerTwoMoves = new ArrayList<>();

    JFrame xand0;
    JPanel gridPanel = new JPanel(new GridLayout(3, 3));
    JLabel scoreLabel = new JLabel("Score - Player One: 0 | Player Two: 0", SwingConstants.CENTER);
    JButton[] buttons = new JButton[9];

    int flag = 0;
    int playerOneScore = 0;
    int playerTwoScore = 0;
    String playerOneName = "Player One";
    String playerTwoName = "Player Two";

    public XandOExample() {
        showWelcomeScreen();
    }

    void showWelcomeScreen() {
        JFrame welcomeFrame = new JFrame("X and O Game - Welcome");
        welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        welcomeFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        welcomeFrame.getContentPane().setBackground(new Color(30, 30, 60));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(150, 300, 150, 300));

        JLabel title = new JLabel("X and O Battle Arena", SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 48));
        title.setForeground(Color.ORANGE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(50));

        JTextField playerOneInput = new JTextField();
        JTextField playerTwoInput = new JTextField();

        setPlaceholder(playerOneInput, "Enter Player One Name");
        setPlaceholder(playerTwoInput, "Enter Player Two Name");

        playerOneInput.setMaximumSize(new Dimension(400, 50));
        playerTwoInput.setMaximumSize(new Dimension(400, 50));
        playerOneInput.setFont(new Font("SansSerif", Font.PLAIN, 22));
        playerTwoInput.setFont(new Font("SansSerif", Font.PLAIN, 22));
        playerOneInput.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                "Player One Name (X)", 0, 0,
                new Font("SansSerif", Font.BOLD, 16), Color.WHITE));

        playerTwoInput.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                "Player Two Name (O)", 0, 0,
                new Font("SansSerif", Font.BOLD, 16), Color.WHITE));

        panel.add(playerOneInput);
        panel.add(Box.createVerticalStrut(20));
        panel.add(playerTwoInput);
        panel.add(Box.createVerticalStrut(40));

        JButton startBtn = new JButton("Start Game");
        startBtn.setFont(new Font("Arial", Font.BOLD, 26));
        startBtn.setBackground(new Color(50, 205, 50));
        startBtn.setForeground(Color.WHITE);
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        startBtn.addActionListener((ActionEvent e) -> {
            playerOneName = playerOneInput.getText().equals("Enter Player One Name") || playerOneInput.getText().isBlank()
                    ? "Player One" : playerOneInput.getText();

            playerTwoName = playerTwoInput.getText().equals("Enter Player Two Name") || playerTwoInput.getText().isBlank()
                    ? "Player Two" : playerTwoInput.getText();

            welcomeFrame.dispose();
            initGameWindow();
        });

        panel.add(startBtn);
        welcomeFrame.add(panel);
        welcomeFrame.setVisible(true);
    }

    private void setPlaceholder(JTextField field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setText(placeholder);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }

    void initGameWindow() {
        xand0 = new JFrame("X and O Game");
        xand0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        xand0.setExtendedState(JFrame.MAXIMIZED_BOTH);
        xand0.setLayout(new BorderLayout());

        scoreLabel.setFont(new Font("Arial", Font.BOLD, 28));
        scoreLabel.setForeground(Color.DARK_GRAY);
        xand0.add(scoreLabel, BorderLayout.NORTH);

        gridPanel.setBackground(Color.BLACK);
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(new Font("Arial", Font.BOLD, 100));
            buttons[i].setBackground(new Color(245, 245, 245));
            buttons[i].setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
            int position = i + 1;
            buttons[i].addActionListener(e -> {
                animateButton((JButton) e.getSource());
                handleMove((JButton) e.getSource(), position);
            });
            gridPanel.add(buttons[i]);
        }

        xand0.add(gridPanel, BorderLayout.CENTER);
        xand0.setVisible(true);
    }

    void animateButton(JButton button) {
        Timer timer = new Timer(15, null);
        final float[] size = {1.0f};
        timer.addActionListener(e -> {
            size[0] -= 0.05f;
            button.setFont(button.getFont().deriveFont(100f * size[0]));
            if (size[0] <= 0.85f) {
                ((Timer) e.getSource()).stop();
                button.setFont(button.getFont().deriveFont(100f));
            }
        });
        timer.start();
    }

    void handleMove(JButton button, int position) {
        if (!button.getText().equals("")) return;

        if (flag == 0) {
            playerOneMoves.add(position);
            button.setText("X");
            button.setForeground(Color.BLUE);
            flag = 1;
        } else {
            playerTwoMoves.add(position);
            button.setText("O");
            button.setForeground(Color.RED);
            flag = 0;
        }

        checkWin();
    }

    void checkWin() {
        int[][] winConditions = {
                {1, 2, 3}, {4, 5, 6}, {7, 8, 9},
                {1, 4, 7}, {2, 5, 8}, {3, 6, 9},
                {1, 5, 9}, {3, 5, 7}
        };

        for (int[] condition : winConditions) {
            if (playerOneMoves.contains(condition[0]) &&
                    playerOneMoves.contains(condition[1]) &&
                    playerOneMoves.contains(condition[2])) {
                playerOneScore++;
                showResult(playerOneName);
                return;
            } else if (playerTwoMoves.contains(condition[0]) &&
                    playerTwoMoves.contains(condition[1]) &&
                    playerTwoMoves.contains(condition[2])) {
                playerTwoScore++;
                showResult(playerTwoName);
                return;
            }
        }

        if (playerOneMoves.size() + playerTwoMoves.size() == 9) {
            showResult("No one");
        }
    }

    void showResult(String winnerName) {
        String message = winnerName.equals("No one") ? "It's a draw!" : winnerName + " wins!";
        JOptionPane.showMessageDialog(xand0, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        updateScore();
        int option = JOptionPane.showConfirmDialog(xand0, "Play again?", "Next Round", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            resetBoard();
        } else {
            xand0.dispose();
        }
    }

    void updateScore() {
        scoreLabel.setText("Score - " + playerOneName + ": " + playerOneScore + " | " + playerTwoName + ": " + playerTwoScore);
    }

    void resetBoard() {
        for (JButton btn : buttons) {
            btn.setText("");
            btn.setEnabled(true);
        }
        playerOneMoves.clear();
        playerTwoMoves.clear();
        flag = 0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new XandOExample());
    }
}

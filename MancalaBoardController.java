import javax.swing.*;


public class MancalaBoardController {
    private MancalaBoardView view;
    private GameLogic gameLogic;

    public MancalaBoardController(MancalaBoardView view, GameLogic gameLogic) {
        this.view = view;
        this.gameLogic = gameLogic;
        setupPitListeners();
        setupUndoButton();
    }

    private void setupPitListeners() {
        // Setup listeners for Player A's pits
        for (int i = 0; i < 6; i++) {
            final int pitIndex = i;
            view.getPlayerAPits()[i].addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    handlePitClick(pitIndex, true);
                }
            });
        }

        // Setup listeners for Player B's pits
        for (int i = 0; i < 6; i++) {
            final int pitIndex = i;
            view.getPlayerBPits()[i].addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    handlePitClick(pitIndex, false);
                }
            });
        }
    }

    private void setupUndoButton() {
        view.getUndoButton().addActionListener(e -> handleUndo());
    }

    private void handleUndo() {
        if (gameLogic.undo()) {
            updateView();
        } else {
            JOptionPane.showMessageDialog(view, "Cannot undo further!");
        }
    }

    private void handlePitClick(int pitIndex, boolean isPlayerA) {
        if (gameLogic.isGameOver()) {
            JOptionPane.showMessageDialog(view, "Game is over! " + gameLogic.getWinner() + " wins!");
            return;
        }

        if (isPlayerA != gameLogic.isPlayerATurn()) {
            JOptionPane.showMessageDialog(view, "It's not your turn!");
            return;
        }

        if (gameLogic.makeMove(pitIndex)) {
            updateView();
            if (gameLogic.isGameOver()) {
                JOptionPane.showMessageDialog(view, "Game Over! " + gameLogic.getWinner() + " wins!");
            }
        } else {
            JOptionPane.showMessageDialog(view, "Invalid move!");
        }
    }

    private void updateView() {
        // Update Player A's pits
        int[] playerAPits = gameLogic.getPlayerAPits();
        for (int i = 0; i < 6; i++) {
            view.getPlayerAPits()[i].updateBeadCount(playerAPits[i]);
        }

        // Update Player B's pits
        int[] playerBPits = gameLogic.getPlayerBPits();
        for (int i = 0; i < 6; i++) {
            view.getPlayerBPits()[i].updateBeadCount(playerBPits[i]);
        }

        // Update mancalas
        view.getMancalaA().updateBeadCount(gameLogic.getMancalaA());
        view.getMancalaB().updateBeadCount(gameLogic.getMancalaB());

        // Update current player label
        view.updateCurrentPlayer(gameLogic.isPlayerATurn() ? "Player A" : "Player B");
    }

    public static void main(String[] args) {
        //call prompt for the style of board
        BoardStyleStrategy style = stylePrompt();
        if (style == null) {
            System.exit(0);
        }

        int initialBeadCount = beadCountPrompt();
        if (initialBeadCount == 0) {
            System.exit(0);
        }

        MancalaBoardView view = new MancalaBoardView(initialBeadCount, style);
        GameLogic gameLogic = new GameLogic(initialBeadCount);
        new MancalaBoardController(view, gameLogic);
    }

    private static int beadCountPrompt() {
        while (true) {
            String input = JOptionPane.showInputDialog("Enter number of Beads to Start (3 or 4). Enter 0 to exit: ");

            if (input.trim().equals("0")) {
                return 0;
            }

            try {
                int beadCount = Integer.parseInt(input.trim());
                if (beadCount == 3 || beadCount == 4) {
                    return beadCount;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter 3 or 4 for bead count or 0 to exit.");
            }
        }
    }

    //prompt window to choose style
    private static BoardStyleStrategy stylePrompt() {
        Object[] options = {"Old", "Bright"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Choose your board style:",
                "Board Style",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if (choice == JOptionPane.CLOSED_OPTION) return null;
        return switch (choice) {
            case 0 -> new oldStyle();
            case 1 -> new brightStyle();
            default -> new oldStyle();
        };
    }
} 
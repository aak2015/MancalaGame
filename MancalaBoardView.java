import javax.swing.*;
import java.awt.*;


public class MancalaBoardView extends JFrame {
    private static final String[] PLAYER_A_PITS = {"A1","A2","A3","A4","A5","A6"};
    private static final String[] PLAYER_B_PITS = {"B6","B5","B4","B3","B2","B1"};

    private Pit[] playerAPits;
    private Pit[] playerBPits;
    private MancalaPit MancalaA;
    private MancalaPit MancalaB;
    private JLabel currentPlayer;
    private JButton undoButton;
    private BoardStyleStrategy boardStyle;

    public MancalaBoardView(int initialBeadCount, BoardStyleStrategy style) {
        setTitle("Mancala Game Board");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.boardStyle = style;

        // Create top panel with undo button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        undoButton = new JButton("Undo");
        undoButton.setFont(new Font("Serif", Font.BOLD, 14));
        undoButton.setToolTipText("Undo last move");
        topPanel.add(undoButton);
        add(topPanel, BorderLayout.NORTH);

        // Current turn panel (left)
        JPanel turnPanel = new JPanel();
        turnPanel.setLayout(new BorderLayout());
        turnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        currentPlayer = new JLabel("Player A", SwingConstants.CENTER);
        currentPlayer.setFont(new Font("Serif", Font.BOLD, 20));
        currentPlayer.setOpaque(true);
        currentPlayer.setBackground(new Color(200, 230, 255));
        currentPlayer.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        turnPanel.add(currentPlayer, BorderLayout.NORTH);
        add(turnPanel, BorderLayout.WEST);

        //Set up player A pits
        JPanel playerAPitPanel = new JPanel(new GridLayout(1,6,5,5));
        playerAPitPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        playerAPits = new Pit[6];
        for(int i = 0; i < 6; i++){
            playerAPits[i] = new Pit(PLAYER_A_PITS[i], initialBeadCount);
            boardStyle.stylePit(playerAPits[i]);
            playerAPitPanel.add(playerAPits[i]);
        }

        //Combine pits so B row is on top of A row
        JPanel playerBPitPanel = new JPanel(new GridLayout(1,6,5,5));
        playerBPitPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        playerBPits = new Pit[6];
        for(int i = 0; i < 6; i++){
            playerBPits[i] = new Pit(PLAYER_B_PITS[i],initialBeadCount);
            boardStyle.stylePit(playerBPits[i]);
            playerBPitPanel.add(playerBPits[i]);
        }

        // Combine pit rows
        JPanel pitRows = new JPanel(new BorderLayout());
        pitRows.add(playerBPitPanel, BorderLayout.NORTH);
        pitRows.add(playerAPitPanel, BorderLayout.SOUTH);

        //Mancala Pits
        MancalaA = new MancalaPit("Mancala A", 0);
        boardStyle.stylePit(MancalaA);

        MancalaB = new MancalaPit("Mancala B", 0);
        boardStyle.stylePit(MancalaB);

        // Board center layout
        JPanel boardCenter = new JPanel(new BorderLayout(10,10));
        boardCenter.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        boardCenter.add(MancalaB, BorderLayout.WEST);
        boardCenter.add(pitRows, BorderLayout.CENTER);
        boardCenter.add(MancalaA, BorderLayout.EAST);

        // Direction labels above and below board
        JLabel playerBDirection = new JLabel("<-- Player B", SwingConstants.CENTER);
        playerBDirection.setFont(new Font("Serif", Font.BOLD, 18));
        JLabel playerADirection = new JLabel("Player A -->", SwingConstants.CENTER);
        playerADirection.setFont(new Font("Serif", Font.BOLD, 18));

        JPanel boardPanel = new JPanel(new BorderLayout());
        boardPanel.add(playerBDirection, BorderLayout.NORTH);
        boardPanel.add(boardCenter, BorderLayout.CENTER);
        boardPanel.add(playerADirection, BorderLayout.SOUTH);
        add(boardPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void updateCurrentPlayer(String player){
        currentPlayer.setText(player);
    }

    // Getter methods for controller access
    public Pit[] getPlayerAPits() {
        return playerAPits;
    }
    public Pit[] getPlayerBPits() {
        return playerBPits;
    }
    public MancalaPit getMancalaA() {
        return MancalaA;
    }
    public MancalaPit getMancalaB() {
        return MancalaB;
    }
    public JButton getUndoButton() {
        return undoButton;
    }
} 
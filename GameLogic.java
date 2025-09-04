import java.util.Stack;

public class GameLogic {
    private int[] playerAPits;
    private int[] playerBPits;
    private int mancalaA;
    private int mancalaB;
    private boolean isPlayerATurn;
    private boolean gameOver;
    
    // Stack to store game states for undo
    private Stack<GameState> stateHistory;

    // Inner class to store game state
    private static class GameState {
        int[] playerAPits;
        int[] playerBPits;
        int mancalaA;
        int mancalaB;
        boolean isPlayerATurn;
        boolean gameOver;

        GameState(int[] playerAPits, int[] playerBPits, int mancalaA, int mancalaB, 
                 boolean isPlayerATurn, boolean gameOver) {
            this.playerAPits = playerAPits.clone();
            this.playerBPits = playerBPits.clone();
            this.mancalaA = mancalaA;
            this.mancalaB = mancalaB;
            this.isPlayerATurn = isPlayerATurn;
            this.gameOver = gameOver;
        }
    }

    public GameLogic(int initialBeadCount) {
        playerAPits = new int[6];
        playerBPits = new int[6];
        for (int i = 0; i < 6; i++) {
            playerAPits[i] = initialBeadCount;
            playerBPits[i] = initialBeadCount;
        }
        mancalaA = 0;
        mancalaB = 0;
        isPlayerATurn = true;
        gameOver = false;
        stateHistory = new Stack<>();
        saveState(); // Save initial state
    }

    private void saveState() {
        stateHistory.push(new GameState(playerAPits, playerBPits, mancalaA, mancalaB, 
                                      isPlayerATurn, gameOver));
    }

    public boolean undo() {
        if (stateHistory.size() <= 1) { // Don't undo past initial state
            return false;
        }
        
        // Get previous state
        GameState previousState = stateHistory.peek();
        
        // Restore previous state
        playerAPits = previousState.playerAPits.clone();
        playerBPits = previousState.playerBPits.clone();
        mancalaA = previousState.mancalaA;
        mancalaB = previousState.mancalaB;
        isPlayerATurn = previousState.isPlayerATurn;
        gameOver = previousState.gameOver;
        
        return true;
    }

    public boolean makeMove(int pitIndex) {
        if (gameOver || pitIndex < 0 || pitIndex >= 6) {
            return false;
        }

        int[] currentPlayerPits = isPlayerATurn ? playerAPits : playerBPits;
        int stones = currentPlayerPits[pitIndex];
        
        if (stones == 0) {
            return false;
        }

        // Save state before making move
        saveState();

        currentPlayerPits[pitIndex] = 0;
        int currentIndex = pitIndex;
        boolean lastStoneInMancala = false;

        while (stones > 0) {
            if (isPlayerATurn) {
                // Player A's turn - move counterclockwise
                currentIndex++;
                if (currentIndex < 6) {
                    currentPlayerPits[currentIndex]++;
                } else if (currentIndex == 6) {
                    mancalaA++;
                    lastStoneInMancala = stones == 1;
                } else {
                    // Switch to opponent's side (Player B's pits)
                    // Start at B1 (index 0) and move counterclockwise
                    int opponentIndex = currentIndex - 7;
                    if (opponentIndex < 6) {
                        playerBPits[5 - opponentIndex]++; // Reverse the index to start at B1
                    }
                    if (currentIndex == 12) { // After going through all pits
                        currentIndex = -1; // Reset to start of current player's side
                    }
                }
            } else {
                // Player B's turn - move counterclockwise
                currentIndex--;
                if (currentIndex >= 0) {
                    currentPlayerPits[currentIndex]++;
                } else if (currentIndex == -1) {
                    mancalaB++;
                    lastStoneInMancala = stones == 1;
                } else {
                    // Switch to opponent's side (Player A's pits)
                    int opponentIndex = -currentIndex - 2;
                    if (opponentIndex < 6) {
                        playerAPits[opponentIndex]++;
                    }
                    if (currentIndex == -7) {
                        currentIndex = 6; // Reset to start of current player's side
                    }
                }
            }
            stones--;
        }

        // Check for capture - same logic for both players
        if (!lastStoneInMancala && currentIndex >= 0 && currentIndex < 6) {
            int[] currentPits = isPlayerATurn ? playerAPits : playerBPits;
            int[] opponentPits = isPlayerATurn ? playerBPits : playerAPits;
            
            if (currentPits[currentIndex] == 1) {
                // For both players, the capture index is the same as the current index
                int capturedStones = opponentPits[currentIndex];
                if (capturedStones > 0) {
                    // Add captured stones to the appropriate mancala
                    if (isPlayerATurn) {
                        mancalaA += capturedStones;
                    } else {
                        mancalaB += capturedStones;
                    }
                    // Clear the opponent's pit
                    opponentPits[currentIndex] = 0;
                }
            }
        }

        // Switch turns unless last stone landed in mancala
        if (!lastStoneInMancala) {
            isPlayerATurn = !isPlayerATurn;
        }

        // Check if game is over
        checkGameOver();

        return true;
    }

    private void checkGameOver() {
        boolean playerAEmpty = true;
        boolean playerBEmpty = true;

        for (int i = 0; i < 6; i++) {
            if (playerAPits[i] > 0) {
                playerAEmpty = false;
            }
            if (playerBPits[i] > 0) {
                playerBEmpty = false;
            }
        }

        if (playerAEmpty || playerBEmpty) {
            gameOver = true;
            // Add remaining stones to respective mancalas
            for (int i = 0; i < 6; i++) {
                mancalaA += playerAPits[i];
                mancalaB += playerBPits[i];
                playerAPits[i] = 0;
                playerBPits[i] = 0;
            }
        }
    }

    public int[] getPlayerAPits() {
        return playerAPits.clone();
    }

    public int[] getPlayerBPits() {
        return playerBPits.clone();
    }

    public int getMancalaA() {
        return mancalaA;
    }

    public int getMancalaB() {
        return mancalaB;
    }

    public boolean isPlayerATurn() {
        return isPlayerATurn;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getWinner() {
        if (!gameOver) {
            return null;
        }
        if (mancalaA > mancalaB) {
            return "Player A";
        } else if (mancalaB > mancalaA) {
            return "Player B";
        } else {
            return "Tie";
        }
    }
} 
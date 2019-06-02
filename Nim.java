/**
 * This is the java implementation of the old strategy game Nim.
 * The class has three attributes:
 * currentPlayer saves if it's the machines or the humans turn.
 * lastMove saves the last move performed.
 * sticks saves the current state of the game. The length of sticks
 * represents the number of rows, and the value in each row represents the
 * number of sticks in that row.
 */
public class Nim implements Board {

    protected Player currentPlayer;

    protected Move lastMove;

    protected int[] sticks;

    /**
     * Constructor for the nim class, which takes two attributes.
     * @param numberOfSticks int array which represents the number of rows
     *                       and the sticks in each row
     * @param player player who begins the game
     */
    public Nim(int[] numberOfSticks, Player player) {

        this.sticks = numberOfSticks;
        this.currentPlayer = player;
        this.lastMove = null;
    }

    /**
     *  Executes human move. The method first checks if it's the
     *  humans turn and if the input given by the human is valid.
     *  If both of these conditions are met, the move is executed, the
     *  current player changes to the machine and the move is saved in the
     *  lastMove attribute.
     * @param row The number of the zero indexed row ascending top down.
     * @param s The number of sticks to remove from row {@code row}. Must be at
     *        least 1 and at most the number of sticks of the row.
     */
    @Override
    public void remove(int row, int s) {
        if (currentPlayer != Player.HUMAN) {
            throw new IllegalStateException("It's the machines turn.");
        }
        if (!checkForValidInput(row, s)) {
            throw new IllegalArgumentException("Not a valid move.");
        }
        sticks[row] -= s;
        lastMove = new Move(row, s, currentPlayer);
        changePlayer();
    }

    /**
     * Execute a machine move. The machine plays optimally from the
     * beginning.
     * The method first checks if it's the machines turn. Then the nim sum of
     * the current board is calculated.
     * If the nim sum is zero, the board is already in a safe combination,
     * and half of the sticks in the first row that is not zero are removed.
     * If it is not zero, the machine executes a move which converts the
     * board to a safe combination. This is done by removing sticks from the
     * row with the highest one bit.
     */
    @Override
    public void machineRemove() {
        // The machine plays optimally, no matter what.
        if (currentPlayer != Player.MACHINE) {
            throw new IllegalStateException("It's the humans turn.");
        }

        int nimSum = calculateNimSum();
        // If the combination is already safe, remove half of the sticks
        // (rounded up) of the first row that is not empty
        if (nimSum == 0) {

            for (int i = 0; i < sticks.length; i++) {

                if (sticks[i] != 0) {
                    int numberOfSticksToRemove =
                            (int) Math.ceil(sticks[i] / 2d);
                    sticks[i] -= numberOfSticksToRemove;
                    lastMove = new Move(i, numberOfSticksToRemove,
                            currentPlayer);
                    break;
                }
            }
        // If the combination is NOT safe, choose the row with a 1 in the
            // highest bit and the lowest number
        } else {
            for (int i = 0; i < sticks.length; i++) {
                int c = (int) (Math.log(nimSum) / Math.log(2));
                if (((sticks[i] >> c) & 1) == 1) {
                    int previousSticks = sticks[i];
                    sticks[i] = sticks[i] ^ nimSum;
                    lastMove = new Move(i, previousSticks
                            - sticks[i], currentPlayer);
                    break;
                }
            }
        }
        changePlayer();
    }

    /**
     * Get the last move performed in the game.
     * @return last move executed, which includes the number of sticks
     * removed and the row from which they were removed.
     */
    @Override
    public Move getLastMove() {
        return lastMove;
    }

    /**
     * Get the number of rows the current game is played with.
     * This is represented by the length of the sticks array.
     * @return number of rows of the current game.
     */
    @Override
    public int getRowCount() {
        return sticks.length;
    }

    /**
     * This method returns the number of sticks in a row. The row attribute
     * specifies which row to get the number of sticks for.
     * If row is not a valid value to get the sticks for, -1 is returned.
     * @param row The number of the zero indexed row ascending top down.
     * @return the number of sticks in row
     */
    @Override
    public int getSticks(int row) {
        if (checkForValidInt(row)) {
            return sticks[row];
        }
        return -1;
    }

    /**
     * Method to determine the winner of the game
     * @return winner of the game
     */
    @Override
    public Player getWinner() {
        changePlayer();
        return currentPlayer;
    }

    /**
     * Check if the game is over. A game is over when all rows are zero.
     * @return true if game is over, false if it is still running.
     */
    @Override
    public boolean isGameOver() {
        for (int i : sticks) {
            if (i != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Deep copy of the current board.
     * @return deep copy of the current board.
     */
    @Override
    public Board clone() {
        int[] arrayClone = new int[sticks.length];
        System.arraycopy(sticks, 0, arrayClone, 0, sticks.length);
        return new Nim(arrayClone, currentPlayer);
    }

    private boolean checkForValidInput(int r, int s) {
        return checkForValidInt(r) && sticks[r] - s >= 0;
    }

    private boolean checkForValidInt(int i) {
        return i >= 0 && i < sticks.length;
    }

    /**
     * Helper method to change the current player. Executed after every
     * successfully performed move. currentPlayer switches to Machine if it was
     * the humans turn and to human if it was the machines turn.
     */
    protected void changePlayer() {
        if (currentPlayer == Player.HUMAN) {
            currentPlayer = Player.MACHINE;
            return;
        }
        currentPlayer = Player.HUMAN;
    }

    /**
     * Calculates the nim sum of the game and returns it.
     * A safe combination is given when the nim sum is 0.
     * @return nim sum of the game
     */
    private int calculateNimSum() {
        int nimSum = sticks[0];
        for (int i = 1; i < sticks.length; i++) {
            nimSum = nimSum ^ sticks[i];
        }
        return nimSum;
    }

    /**
     * Converts sticks to a binary string representation. If verbose mode is
     * turned on, it is used in the toString method to provide additional
     * information about the game.
     * @return string representation of the sticks as binary numbers.
     */
    private String[] convertSticksToBinary() {
        String[] binarySticks = new String[sticks.length];
        for (int i = 0; i < sticks.length; i++) {
            binarySticks[i] = Integer.toBinaryString(sticks[i]);
        }
        return binarySticks;
    }

    /**
     * String representation of the game. If the global flag verboseMode is
     * set to true, additional information about the current state of the
     * game is returned, such as the number of sticks in each row as a binary
     * number, and the current nim sum.
     * If it is set to false, a simpler representation of the game is
     * returned, containing just the number of rows and the sticks in each row.
     * @return string representation of the game
     */
    @Override
    public String toString() {

        StringBuilder nimToString = new StringBuilder();
        String lineToAppend;

        if (Shell.getVerboseMode()) {
            String[] binarySticks = convertSticksToBinary();

            for (int i = 0; i < sticks.length; i++) {
                lineToAppend = (i + 1) + ": " + sticks[i]
                        + " (" + binarySticks[i] + ")" + "\n";
                nimToString.append(lineToAppend);
            }

            int nimSum = calculateNimSum();
            lineToAppend = "Nim sum: " + nimSum + " ("
                    + Integer.toBinaryString(nimSum) + ")";
            nimToString.append(lineToAppend);

        } else {

            for (int i = 0; i < sticks.length; i++) {
                lineToAppend = (i + 1) + ": " + sticks[i] + "\n";
                nimToString.append(lineToAppend);
            }

        }
        return nimToString.toString();
    }

}

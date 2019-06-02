/**
 * The NIM game. Two players remove alternately at least one up to at most all
 * sticks from a single row (= pile = heap) of sticks. The winner is determined
 * by who removes the last available stick(s) from the table.
 *
 * A human plays against the machine.
 */
public interface Board extends Cloneable {

    /**
     * Gets the number of rows of the game.

     * @return The number of rows.
     */
    int getRowCount();

    /**
     * Gets the number of sticks currently in the row {@code row}.
     *
     * @param row The number of the zero indexed row ascending top down.
     * @return The number of sticks in row {@code row}.
     */
    int getSticks(int row);

    /**
     * Executes a human move.
     *
     * @param row The number of the zero indexed row ascending top down.
     * @param s The number of sticks to remove from row {@code row}. Must be at
     *        least 1 and at most the number of sticks of the row.
     * @throws IllegalStateException It is not the human's turn.   
     * @throws IllegalArgumentException The provided move is illegal.
     */
    void remove(int row, int s);

    /**
     * Executes a machine move.
     *
     * @throws IllegalStateException It is not the machine's turn.   
     */
    void machineRemove();

    /**
     * Gets the last move made on the game.
     *
     * @return The number of sticks and the 0-indexed row from which they were
     *         removed in the last move (irrespective of which player has
     *         performed it).
     */
    Move getLastMove();

    /**
     * Checks if the game is over, i.e., if one player has won and thus no
     * sticks are left on the table.
     *
     * @return {@code true} if and only if the game is over.
     */
    boolean isGameOver();

    /**
     * Checks who has won the game. Should only be called if
     * {@link #isGameOver()} returns {@code true}.
     *
     * A game is won by a player if she (or the opponent in Misère mode,
     * respectively) takes the last stick from the table.
     *
     * @return The winner of the game.
     */
    Player getWinner();

    /**
     * Creates and returns a deep copy of this board.
     *
     * @return A clone of this object.
     */
    Board clone();

    /**
     * Gets the string representation of the current board with the numbers of
     * sticks per row.
     *
     * @return The string representation of the current game status in lines
     *         with ascending row numbers.
     */
    @Override
    String toString();

}
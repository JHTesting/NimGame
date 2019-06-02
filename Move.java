/**
 * The Move class is just a wrapper class for the last move executed in the
 * game. It saves just three attributes: The player who executed the move,
 * the number of sticks removed, and the row from which they were removed.
 */
public class Move {

    private int row;

    private int numberOfSticks;

    private Player player;

    /**
     * Constructs a new move object. Contains the number of sticks removed,
     * the row from which they were removed, and the player who executed the
     * move.
     * @param row row from which sticks were removed.
     * @param numberOfSticks number of sticks removed.
     * @param player player who executed the move.
     */
    public Move(int row, int numberOfSticks, Player player) {
        this.row = row;
        this.numberOfSticks = numberOfSticks;
        this.player = player;
    }

    /**
     * Getter method for the player attribute
     * @return player who executed this move
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * String representation of the last move executed.
     * @return string representation of the move method.
     */
    @Override
    public String toString() {
        return "Player machine removed " + numberOfSticks
                + " stick(s) from row " + (row + 1) + ".";
    }


}

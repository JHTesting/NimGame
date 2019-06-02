/**
 * The misere mode of the nim game. This mode is identical
 * to the normal nim game, except that the player who takes the last stick
 * loses. It is a subclass of the normal game, since all methods except
 * machineRemove are identical.
 */
public class Misere extends Nim {

    /**
     * Constructor for the Misere class. It does nothing besides calling the
     * constructor of nim.
     * @param numberOfSticks int array which represents the number of rows
     *      *                       and the sticks in each row
     * @param player player who begins the game
     */
    public Misere(int[] numberOfSticks, Player player) {
        super(numberOfSticks, player);
    }

    /**
     * Slightly changed machineRemove method. The fundamental idea is the
     * same, the method is very similar to the implementation in the nim class.
     * It accounts for changes in the misere mode.
     */
    @Override
    public void machineRemove() {
        // The machine plays optimally, no matter what.
        if (currentPlayer != Player.MACHINE) {
            throw new IllegalStateException("It's the humans turn.");
        }

        int rowsWithOneStick = 0;
        int moreThanOneStick = 0;
        int index = -1;
        for (int i = 0; i < sticks.length; i++) {
            if (sticks[i] > 1) {
                moreThanOneStick++;
                index = i;
            } else if  (sticks[i] == 1) {
                rowsWithOneStick++;
            }
        }
        if (moreThanOneStick == 1) {
            if (rowsWithOneStick % 2 == 0) {
                int j = sticks[index];
                sticks[index] = 1;
                super.lastMove = new Move(index, j - 1,
                        super.currentPlayer);
            } else {
                int j = sticks[index];
                sticks[index] = 0;
                super.lastMove = new Move(index, j,
                        super.currentPlayer);
            }
            super.changePlayer();
            return;
        }
        super.machineRemove();
    }

    /**
     * Returns the player who won the game. The difference between the two
     * methods is that the player who takes the last stick loses. This change
     * is accounted for here.
     * @return player who won the game.
     */
    @Override
    public Player getWinner() {
        if (lastMove.getPlayer() == Player.HUMAN) {
            return Player.MACHINE;
        }
        return Player.HUMAN;
    }
 }

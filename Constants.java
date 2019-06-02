/**
 * Class for the constants used in the implementation of the nim game
 */
public final class Constants {

    private Constants() {
    }
    /**
     * Constant for the shell prompt.
     * This constant indicates to the user that they may enter a command.
     */
    public static final String SHELL_PROMPT = "nim> ";

    /**
     * This constant is printed when the program starts, additionally it is
     * also printed when the starting player of the next game changes from
     * machine to human.
     */
    public static final String HUMAN_OPENER = "The human makes the initial "
            + "move of the next new game.";

    /**
     * This constant is printed when the starting player of the next game
     * changes from human to machine by calling the "SWITCH" command in the
     * shell.
     */
    public static final String MACHINE_OPENER = "The machine makes the "
            + "initial move of the next new game.";

    /**
     * This string is printed when a command is executed without a game running.
     */
    public static final String GAME_NOT_RUNNING_ERROR = "There is no game "
            + "running at the moment";
}


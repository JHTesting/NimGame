import java.util.Arrays;
import java.util.Scanner;

/**
 * Shell class to get and evaluate user input. The user input is converted
 * into commands and passed to the game, which then executes the commands
 * given to the shell.
 */
public final class Shell {

    /**
     * Global flag, which indicates whether verbose mode is on or off. If it
     * is on, additional information about the game, such as the current nim
     * sum and the binary representation of the sticks, is printed out when
     * the print command is called.
     * By default, verbose mode is turned off.
     */
    private static boolean verboseMode = false;
    /**
     * Scanner object to get input from the user. It is static because every
     * Shell object uses the same Scanner, there is no reason to give every
     * single object its own scanner.
     */
    private static Scanner reader = new Scanner(System.in);

    /**
     * The shell class does not have a public constructor.
     */
    private Shell() {
    }

    /**
     * Getter method for the global verbose mode flag.
     * @return true if verbose mode is on, false if it is not.
     */
    public static boolean getVerboseMode() {
        return verboseMode;
    }


    /**
     * Method to get input from the user by using the nextLine method of the
     * Scanner object.
     * @return user input as a string
     */
    private static String getInput() {
        return reader.nextLine();
    }

    /**
     *  This method evaluates the input given to the shell and prints an error
     *  message if it is invalid. The input is first converted into valid
     *  input and then evaluated. The evaluated input is then passed to the
     *  correct helper method, which executes the command and performs the
     *  requested action on the game.
     */
    private static void evaluateInput() {

        System.out.println(Constants.HUMAN_OPENER);
        String[] inputArray = readInput();
        char input = evaluateCommand(inputArray[0]);
        Board board = null;
        Player beginner = Player.HUMAN;

        while (!(input == 'Q')) {

            switch (input) {

                // Normal Game
                case ('N'):

                    board = startNormalGame(beginner, Arrays.copyOfRange(
                            inputArray, 1, inputArray.length));
                    break;

                // Misere game
                case ('M'):

                    board = startMisereGame(beginner, Arrays.copyOfRange(
                            inputArray, 1, inputArray.length));
                    break;

                // SWITCH
                case ('S'):

                    beginner = switchOpener(beginner);
                    break;

                // REMOVE s sticks from row r
                case ('R'):

                    Integer row = tryParseStrToInt(inputArray[1]);
                    Integer sticks = tryParseStrToInt(inputArray[2]);
                    removeSticks(board, row, sticks);
                    break;

                // Verbose mode
                case ('V'):

                    switchVerbose(inputArray[1]);
                    break;

                // Prints the state of the game
                case ('P'):

                    printGame(board);
                    break;

                case ('H'):

                    printHelp();
                    break;

                default:
                    System.out.println("Error! Invalid command.");
            }
            inputArray = readInput();
            input = evaluateCommand(inputArray[0]);
        }
    }

    /**
     * Helper method to start a new normal game. The method first checks if
     * the given input is valid and initializes a new game if it is. If it
     * isn't an error message is printed and the method terminates.
     * The input given by the user is converted to an array of integers,
     * which represent the numbers in each row of the game.
     * @param input string array, which contains the number of sticks in each
     *             row
     */
    private static Board startNormalGame(Player beginner, String[] input) {

        if (checkIfInputHasParameters(input)) {
            printInvalidInputError();
            return null;
        }
        int[] sticks = parseInputArrayToInt(input);
        if (sticks == null || sticks.length == 0) {
            printInvalidInputError();
            return null;
        }
        Board board = new Nim(sticks, beginner);
        if (beginner == Player.MACHINE) {
            board.machineRemove();
            System.out.println(board.getLastMove());
        }
        return board;
    }

    /**
     * Helper method to start a new misere game. The method first checks if
     * the given input is valid and initializes a new game if it is. If it
     * isn't an error message is printed and the method terminates.
     * The input given by the user is converted to an array of integers,
     * which represent the numbers in each row of the game.
     * @param input string array, which contains the number of sticks in each
     *             row
     */
    private static Board startMisereGame(Player beginner, String[] input) {
        if (checkIfInputHasParameters(input)) {
            printInvalidInputError();
            return null;
        }
        int[] sticks = parseInputArrayToInt(input);
        if (sticks == null || sticks.length == 0) {
            printInvalidInputError();
            return null;
        }
        Board board = new Misere(sticks, beginner);
        if (beginner == Player.MACHINE) {
            board.machineRemove();
            System.out.println(board.getLastMove());
        }
        return board;
    }


    private static void removeSticks(Board board, Integer row,
                                     Integer sticks) {
        if (board == null) {
            System.out.println(Constants.GAME_NOT_RUNNING_ERROR);
            return;
        } else if (board.isGameOver()) {
            checkIfGameIsOver(board, board.getLastMove());
            return;
        }

        try {
            board.remove(row - 1, sticks);
        } catch (IllegalStateException e) {
            System.err.println("It's not the humans turn.");
            return;
        } catch (IllegalArgumentException e) {
            System.err.println("Error! The provided move is illegal.");
            return;
        }

        checkIfGameIsOver(board, board.getLastMove());

        if (board.isGameOver()) {
            return;
        }

        try {
            board.machineRemove();
        } catch (IllegalStateException e) {
            System.err.println("It's not the machines turn");
            return;
        }

        Move lastMove = board.getLastMove();
        System.out.println(lastMove);

        checkIfGameIsOver(board, lastMove);

    }

    private static Player switchOpener(Player beginner) {
        if (beginner == Player.HUMAN) {
            beginner = Player.MACHINE;
            System.out.println(Constants.MACHINE_OPENER);
            return beginner;
        }
        System.out.println(Constants.HUMAN_OPENER);
        return Player.HUMAN;
    }

    private static void checkIfGameIsOver(Board board, Move move) {
        if (board == null) {
            System.out.println(Constants.GAME_NOT_RUNNING_ERROR);
            return;
        }
        if (board.isGameOver()) {
            if (board.getWinner() == Player.HUMAN) {
                System.out.println("Congratulations! You won.");
            } else {
                System.out.println("Sorry! Machine wins.");
            }
        }
    }

    private static void switchVerbose(String v) {

        if (checkForInvalidKey(v)) {
            printInvalidInputError();
            return;
        }

        v = v.toUpperCase();

        if (v.equals("ON")) {
            verboseMode = true;
        } else if (v.equals("OFF")) {
            verboseMode = false;
        } else {
            printInvalidInputError();
        }
    }

    /**
     * Prints a helpful info about how to use the trie, which commands are
     * valid and what kind of input is accepted.
     */
    private static void printHelp() {

        String helpString = "NEW <s1> <s2> ... <sn>: Creates a new nim game "
                + "with n >= 1 rows and si >= 1 sticks per row. The human "
                + "player starts by default.\n"
                + "MISERE <s1> <s2> ... <sn>: Creates a new misere game.\n"
                + "SWITCH: Changes the opener of the next game.\n"
                + "PRINT: Prints the current board.\n"
                + "VERBOSE (ON|OFF): Provides additional details about the "
                + "state of the game\n"
                + "QUIT: Quits the game"
                + "HELP: Show this helpful guide\n";
        System.out.println(helpString);

    }

    /**
     * Prints out the current game board. If verbose mode is enabled,
     * additional information about the game, like the nim sum and the binary
     * representation of the sticks, is also printed out.
     */
    private static void printGame(Board board) {
        if (board == null) {
            System.out.println(Constants.GAME_NOT_RUNNING_ERROR);
            return;
        }
        System.out.println(board);
    }

    private static char evaluateCommand(String command) {
        if (checkForInvalidKey(command)) {
            return ' ';
        }
        return Character.toUpperCase(command.charAt(0));
    }

    private static String[] readInput() {
        System.out.print(Constants.SHELL_PROMPT);
        return splitInput(getInput());
    }

    /**
     * This method cleans up user input and converts it into a string array
     * of size 3, as that is the maximum number of parameters allowed for any
     * given input. First trim is called to remove any unnecessary whitespace
     * from the input. The input is then split and transferred into a new
     * array, which is then returned. This method makes it easier to evaluate
     * user input.
     * @param input input given by the user as a string
     * @return cleaned up input, split by whitespace and stored in a string
     * array of size 3.
     */
    private static String[] splitInput(String input) {

        input = input.trim();
        String[] inputArray = input.split(" ");
        String[] trimmedArray;
        if (inputArray.length < 3) {
            trimmedArray = Arrays.copyOfRange(inputArray, 0, 3);
        } else {
            trimmedArray = Arrays.copyOfRange(inputArray, 0,
                    inputArray.length);
        }
        return trimmedArray;
    }

    /**
     * This method tries to convert a string into an Integer object. If the
     * given string can be converted, the parsed string is returned as an
     * Integer. If the given input is not valid, null is returned.
     * @param stringToParse string to convert to an integer object
     * @return parsed string, null if it couldn't be converted.
     */
    private static Integer tryParseStrToInt(String stringToParse) {
        try {
            return Integer.parseInt(stringToParse);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * This method is just a helper method to make the code more readable. It
     * prints an error message in case the input is invalid.
     */
    private static void printInvalidInputError() {
        System.err.println("Error! The input is invalid.");
    }

    /**
     * This method checks if a given key is a valid input for the
     * trie. Key may not be null, it mustn't contain any uppercase
     * characters and it mustn't be empty.
     * @param key key to check
     * @return true if key is invalid, false otherwise.
     */
    private static boolean checkForInvalidKey(String key) {
        if (key == null) {
            return true;
        }
        return (key.equals("") || key.equals(" "));
    }

    private static boolean checkIfInputHasParameters(String[] inputArray) {
        return (inputArray.length < 2);
    }

    private static int[] parseInputArrayToInt(String[] input) {
        int[] sticks = new int[input.length];

        for (int i = 0; i < input.length; i++) {
            int j = tryParseStrToInt(input[i]);
            if (j < 1) {
                return null;
            }
            sticks[i] = j;
        }
        return sticks;
    }

    /**
     * Main method for the Shell class. Calls the evaluateInput method, which
     * then takes input
     * @param args arguments for main
     */
    public static void main(String[] args) {
        Shell.evaluateInput();
    }

}

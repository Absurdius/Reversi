package game;

public class Game {
    private Board board;
    private ReversiPlayer black;
    private ReversiPlayer white;

    private long timeLimit;
    private int winner = 0;
    private boolean debugMode;

    public Game() {
        board = new Board();
    }

    /**
     * Starts the game by prompting {@code player1} for configuration options and running the main game loop
     * until no player has any valid moves left.
     *
     * @param player1 reversi player, handles game configuration
     * @param player2 reversi player
     */
    public void startGame(ReversiPlayer player1, ReversiPlayer player2) {
        printWelcomeMessage();

        if (player1.getColorPreference() == Board.BLACK) {
            player1.setMyColor(Board.BLACK);
            player2.setMyColor(Board.WHITE);
            black = player1;
            white = player2;
        } else {
            player1.setMyColor(Board.WHITE);
            player2.setMyColor(Board.BLACK);
            black = player2;
            white = player1;
        }

        this.timeLimit = player1.getTimeLimitPreference();
        black.setTimeLimit(timeLimit);
        white.setTimeLimit(timeLimit);

        printInstructions();

        while (board.hasMoves()) {
            board.move(black.getNextMove(board), Board.BLACK);
            board.move(white.getNextMove(board), Board.WHITE);
        }

        winner = board.getWinner();
        printResults();
    }


    public long getTimeLimit() {
        return timeLimit;
    }

    public int getWinner() {
        return winner;
    }

    /**
     * Disables printing when true.
     *
     * @param debugMode
     */
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    private void printWelcomeMessage() {
        if (debugMode) return;
        System.out.println(" _       __     __                             __           ____                           _    ___    ____\n" +
                "| |     / /__  / /________  ____ ___  ___     / /_____     / __ \\___ _   _____  __________(_)  /   |  /  _/\n" +
                "| | /| / / _ \\/ / ___/ __ \\/ __ `__ \\/ _ \\   / __/ __ \\   / /_/ / _ \\ | / / _ \\/ ___/ ___/ /  / /| |  / /  \n" +
                "| |/ |/ /  __/ / /__/ /_/ / / / / / /  __/  / /_/ /_/ /  / _, _/  __/ |/ /  __/ /  (__  ) /  / ___ |_/ /   \n" +
                "|__/|__/\\___/_/\\___/\\____/_/ /_/ /_/\\___/   \\__/\\____/  /_/ |_|\\___/|___/\\___/_/  /____/_/  /_/  |_/___/   \n" +
                "                                                                                                           ");
    }

    private void printInstructions() {
        if (debugMode) return;
        System.out.println();
        System.out.println("Chose where to place your piece by entering a column and a row.");
        System.out.println("e.g. a1 to place a piece in the top right cornet");
        System.out.println();
    }

    public void printResults() {
        if (debugMode) return;
        System.out.println("\n" +
                "              ,,,,,,,,,,,,,\n" +
                "          .;;;;;;;;;;;;;;;;;;;,.\n" +
                "        .;;;;;;;;;;;;;;;;;;;;;;;;,\n" +
                "      .;;;;;;;;;;;;;;;;;;;;;;;;;;;;.\n" +
                "      ;;;;;@;;;;;;;;;;;;;;;;;;;;;;;;' .............\n" +
                "      ;;;;@@;;;;;;;;;;;;;;;;;;;;;;;;'.................\n" +
                "      ;;;;@@;;;;;;;;;;;;;;;;;;;;;;;;'...................\n" +
                "      `;;;;@;;;;;;;;;;;;;;;@;;;;;;;'.....................\n" +
                "       `;;;;;;;;;;;;;;;;;;;@@;;;;;'..................;....\n" +
                "         `;;;;;;;;;;;;;;;;@@;;;;'....................;;...\n" +
                "           `;;;;;;;;;;;;;@;;;;'...;.................;;....\n" +
                "              `;;;;;;;;;;;;'   ...;;...............;.....\n" +
                "                 `;;;;;;'        ...;;..................\n" +
                "                    ;;              ..;...............\n" +
                "                    `                  ............\n" +
                "                   `                      ......\n" +
                "                  `                         ..\n" +
                "                 `                           '\n" +
                "                `                           '\n" +
                "               `                           '\n" +
                "              `                           `\n" +
                "              `                           `,\n" +
                "              `\n" +
                "               `\n" +
                "                 `.\n" +
                " ");
        if (winner == Board.WHITE) {
            System.out.println("Good job white, you are the winner!");
        } else if (winner == Board.BLACK) {
            winner = Board.BLACK;
            System.out.println("Good job black, you are the winner!");
        } else {
            System.out.println("It's a draw! Well played black AND white!");
        }
        System.out.println();
        System.out.println("End results:");
        System.out.println("Black: " + black);
        System.out.println("White: " + white);
        System.out.println();
    }
}

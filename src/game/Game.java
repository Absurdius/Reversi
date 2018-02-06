package game;

import java.util.ArrayList;

public class Game {
    public static final int BLACK = 1;
    public static final int WHITE = -1;
    private static final int EMPTY = 0;
    private static final int BOARD_SIZE = 8;

    private int[][] board;
    private ReversiPlayer black;
    private ReversiPlayer white;

    private long timeLimit;

    public Game() {
        board = new int[BOARD_SIZE][BOARD_SIZE]; //Default value of elements is zero

        board[3][3] = BLACK;
        board[3][4] = WHITE;
        board[4][3] = WHITE;
        board[4][4] = BLACK;
    }

    public void startGame(ReversiPlayer player1, ReversiPlayer player2) {
        System.out.println("Welcome to Reversi");

        if (player1.getColorPreference() == BLACK) {
            black = player1;
            white = player2;
        } else {
            black = player2;
            white = player1;
        }

        this.timeLimit = player1.getTimeLimitPreference();

        while (true) {
            ArrayList<int[]> blackMoves = getMoves(board, BLACK);
            ArrayList<int[]> whiteMoves = getMoves(board, WHITE);

            if (!blackMoves.isEmpty()) {
                black.getNextMove(board, getMoves(board, BLACK));
            }
            if (!whiteMoves.isEmpty()) {
                white.getNextMove(board, getMoves(board, WHITE));
            }
            if (blackMoves.isEmpty() && whiteMoves.isEmpty()) {
                break;
            }
        }

        printResults();
    }

    private void printWelcomeMessage() {

    }

    //Simply counts each color
    public void printResults() {
        int black = 0;
        int white = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == BLACK) {
                    black++;
                } else if (board[i][j] == WHITE) {
                    white++;
                }
            }
        }
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
        if (white > black) {
            System.out.println("Good job white, you are the winner!");
        } else if (black > white) {
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


    public boolean move(int[] move, int color) {
        if (move.length < 2) {
            return getMoves(board, color).contains(move);
        } else {
            return false;
        }
    }

    public ArrayList<int[]> getMoves(int[][] board, int color) {
        // return possible moves for THE CURRENT PLAYER in form of an Arraylist.
        int opColor = (color == BLACK) ? WHITE : BLACK;

        ArrayList<int[]> moves = new ArrayList<int[]>();


        //loop over the board row by row
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                //loop over every piece in the corresponding color
                if (board[i][j] == color) {
                    int row = i;
                    int col = j;
                    int[] move = new int[2];
                    //jumps cells with opcolor until a different value is encountered
                    //if different value is empty, add to movelist
                    //check in all eight directions

                    //down
                    if (i < 6) {
                        //begin at cell next to current piece
                        //increment index as long as the value in the next is enemy
                        //when stopped, check for empty.
                        //add empty to the move list.
                        //rinse and repeat fo the other directions
                        while (board[row + 1][col] == opColor && row < 6) {
                            row++;
                        }
                        if (board[row + 1][col] == EMPTY) {
                            move[0] = row + 1;
                            move[1] = col;
                            moves.add(move);
                        }
                        //reset row and col
                        row = i;
                        col = j;
                    }

                    //up
                    if (i > 0) {
                        while (board[row - 1][col] == opColor && row > 0) {
                            row--;
                        }
                        if (board[row - 1][col] == EMPTY) {
                            move[0] = row - 1;
                            move[1] = col;
                        }
                        //reset row and col
                        row = i;
                        col = j;
                    }

                    //left
                    if (i > 0) {
                        while (board[row][col - 1] == opColor && col > 0) {
                            col--;
                        }
                        if (board[row][col - 1] == EMPTY) {
                            move[0] = row;
                            move[1] = col - 1;
                        }
                        //reset row and col
                        row = i;
                        col = j;
                    }

                    //right
                    if (i > 0) {
                        while (board[row][col + 1] == opColor && col < 6) {
                            col++;
                        }
                        if (board[row][col + 1] == EMPTY) {
                            move[0] = row;
                            move[1] = col + 1;
                        }
                        //reset row and col
                        row = i;
                        col = j;
                    }

                    // AND NOW FOR THE DIAGONALS //

                    // down + right  ++
                    if (i > 0) {
                        while (board[row + 1][col + 1] == opColor && row < 6 && col < 6) {
                            row++;
                            col++;
                        }
                        if (board[row + 1][col + 1] == EMPTY) {
                            move[0] = row + 1;
                            move[1] = col + 1;
                        }
                        //reset row and col
                        row = i;
                        col = j;
                    }

                    // down + left  +-
                    if (i > 0) {
                        while (board[row + 1][col - 1] == opColor && row < 6 && col > 0) {
                            row++;
                            col--;
                        }
                        if (board[row + 1][col - 1] == EMPTY) {
                            move[0] = row + 1;
                            move[1] = col - 1;
                        }
                        //reset row and col
                        row = i;
                        col = j;
                    }

                    // up + left  --
                    if (i > 0) {
                        while (board[row - 1][col - 1] == opColor && row > 0 && col > 0) {
                            row--;
                            col--;
                        }
                        if (board[row - 1][col - 1] == EMPTY) {
                            move[0] = row - 1;
                            move[1] = col - 1;
                        }
                        //reset row and col
                        row = i;
                        col = j;
                    }

                    // up + right  -+
                    if (i > 0) {
                        while (board[row - 1][col + 1] == opColor && row > 0 && col < 6) {
                            row--;
                            col++;
                        }
                        if (board[row - 1][col + 1] == EMPTY) {
                            move[0] = row - 1;
                            move[1] = col + 1;
                        }
                        //reset row and col
                        row = i;
                        col = j;
                    }

                }
            }
        }
        return moves;
    }


    /**
     * Places a {@code color} piece on {@code board} according to {@code move} and updates {@code board} accordingly.
     *
     * @param board reversi board to update
     * @param move  move to make
     * @param color color to place
     * @return
     */
    public int[][] updateBoard(int[][] board, int[] move, int color) {
        // simulate a move on board and return a preview of the borad state
        return null;
    }

    public long getTimeLimit() {
        return timeLimit;
    }
}

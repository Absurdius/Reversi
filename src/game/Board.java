package game;

import java.util.ArrayList;

public class Board {
    public static final int BOARD_SIZE = 8;

    public static final int BLACK = 1;
    public static final int WHITE = -1;
    public static final int EMPTY = 0;

    private int[][] board;

    /**
     * Creates a new board with standard starting positions.
     */
    public Board() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        board[3][3] = WHITE;
        board[4][4] = WHITE;
        board[3][4] = BLACK;
        board[4][3] = BLACK;
    }

    /**
     * Creates a new board with the same state as {@code toCopy}
     *
     * @param toCopy the board state to clone
     */
    public Board(Board toCopy) {
        int[][] boardState = toCopy.board;
        this.board = new int[boardState.length][];
        for (int i = 0; i < boardState.length; i++) {
            this.board[i] = boardState[i].clone();
        }
    }

    /**
     * Determines the winner of the board by counting the number of pieces of each color.
     * No additional checks are performed on the board.
     *
     * @return winning color or zero if tie
     */
    public int getWinner() {
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

        if (white > black) return WHITE;
        if (black > white) return BLACK;

        return 0;
    }

    public boolean hasMoves() {
        return getMoves(BLACK).size() != 0 || getMoves(WHITE).size() != 0;
    }

    /**
     * Checks if the passed move is valid, as defined by the passed ArrayList of validMoves
     *
     * @param validMoves list of valid moves
     * @param move       the move to be evaluated
     * @return true if move is valid
     */
    public boolean isValidMove(ArrayList<int[]> validMoves, int[] move) {
        if (move.length != 2) return false;

        for (int[] validMove : validMoves) {
            if (move[0] == validMove[0] && move[1] == validMove[1]) {
                return true;
            }
        }

        return false;
    }

    /**
     * Places a {@code color} piece on the board according to {@code move} and updates the board accordingly.
     *
     * @param move  move to make, null for pass
     * @param color color to place
     * @return the board or null if move was invalid
     */
    public int[][] move(int[] move, int color) {
        int opColor = (color == BLACK) ? WHITE : BLACK;
        if (move != null && isValidMove(getMoves(color), move)) {
            board[move[0]][move[1]] = color;
            int row = move[0];
            int col = move[1];

            // Similar to possible moves we check if there is any squeezed pieces
            // check for pieces of the opposite color
            // walk along this line
            int flops = 0; // number of pieces to be flopped

            // DOWN
            if (row < 6) {
                while (row < 6 && board[row + 1][col] == opColor) {
                    row++;
                    flops++;
                }
                // when done, check if different value is of color
                if (board[row + 1][col] == color) {
                    // if so, flip every piece between move and the other piece
                    for (int i = 1; i <= flops; i++) {
                        board[move[0] + i][move[1]] = color;
                    }
                }
                //reset values for next direction
                row = move[0];
                col = move[1];
                flops = 0;
            }
            // UP
            if (row > 1) {
                while (row > 1 && board[row - 1][col] == opColor) {
                    row--;
                    flops++;
                }
                if (board[row - 1][col] == color) {
                    for (int i = 1; i <= flops; i++) {
                        board[move[0] - i][move[1]] = color;
                    }
                }
                row = move[0];
                col = move[1];
                flops = 0;
            }
            // RIGHT
            if (col < 6) {
                while (col < 6 && board[row][col + 1] == opColor) {
                    col++;
                    flops++;
                }
                if (board[row][col + 1] == color) {
                    for (int i = 1; i <= flops; i++) {
                        board[move[0]][move[1] + i] = color;
                    }
                }
                row = move[0];
                col = move[1];
                flops = 0;
            }
            // LEFT
            if (col > 1) {
                while (col > 1 && board[row][col - 1] == opColor) {
                    col--;
                    flops++;
                }
                if (board[row][col - 1] == color) {
                    for (int i = 1; i <= flops; i++) {
                        board[move[0]][move[1] - i] = color;
                    }
                }
                row = move[0];
                col = move[1];
                flops = 0;
            }

            // AND NOW FOR THE DIAGONALS //
            // DOWN RIGHT + +
            if (row < 6 && col < 6) {
                while (row < 6 && col < 6 && board[row + 1][col + 1] == opColor) {
                    row++;
                    col++;
                    flops++;
                }
                if (board[row + 1][col + 1] == color) {
                    for (int i = 1; i <= flops; i++) {
                        board[move[0] + i][move[1] + i] = color;
                    }
                }
                row = move[0];
                col = move[1];
                flops = 0;
            }
            if (row < 6 && col > 1) {
                // DOWN LEFT + -
                while (row < 6 && col > 1 && board[row + 1][col - 1] == opColor) {
                    row++;
                    col--;
                    flops++;
                }
                if (board[row + 1][col - 1] == color) {
                    for (int i = 1; i <= flops; i++) {
                        board[move[0] + i][move[1] - i] = color;
                    }
                }
                row = move[0];
                col = move[1];
                flops = 0;
            }

            // UP LEFT - -
            if (row > 1 && col > 1) {
                while (row > 1 && col > 1 && board[row - 1][col - 1] == opColor) {
                    row--;
                    col--;
                    flops++;
                }
                if (board[row - 1][col - 1] == color) {
                    for (int i = 1; i <= flops; i++) {
                        board[move[0] - i][move[1] - i] = color;
                    }
                }
                row = move[0];
                col = move[1];
                flops = 0;
            }
            // UP RIGHT - +
            if (row > 1 && col < 6) {
                while (row > 1 && col < 6 && board[row - 1][col + 1] == opColor) {
                    row--;
                    col++;
                    flops++;
                }
                if (board[row - 1][col + 1] == color) {
                    for (int i = 1; i <= flops; i++) {
                        board[move[0] - i][move[1] + i] = color;
                    }
                }
                row = move[0];
                col = move[1];
                flops = 0;
            }
            // END OF MOVE CODE
            return board;
        } else

        {
            return null;
        }

    }

    /**
     * Finds available moves for player {@code color} on the board.
     *
     * @param color, color to be placed
     * @return ArrayList of all possible moves for that state
     */
    public ArrayList<int[]> getMoves(int color) {
        // return possible moves for THE CURRENT PLAYER in form of an Arraylist.
        int opColor = (color == BLACK) ? WHITE : BLACK;

        ArrayList<int[]> moves = new ArrayList<>();

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
                        //rinse and repeat for the other directions
                        while (board[row + 1][col] == opColor && row < 6) {
                            row++;
                        }
                        if (board[row + 1][col] == EMPTY && board[row][col] == opColor) {
                            moves.add(new int[]{row + 1, col});
                        }

                        //reset row and col
                        row = i;
                        col = j;
                    }

                    //up
                    if (i > 1) {
                        while (board[row - 1][col] == opColor && row > 1) {
                            row--;
                        }
                        if (board[row - 1][col] == EMPTY && board[row][col] == opColor) {
                            moves.add(new int[]{row - 1, col});
                        }
                        //reset row and col
                        row = i;
                        col = j;
                    }

                    //left
                    if (j > 1) {
                        while (board[row][col - 1] == opColor && col > 1) {
                            col--;
                        }
                        if (board[row][col - 1] == EMPTY && board[row][col] == opColor) {
                            moves.add(new int[]{row, col - 1});
                        }
                        //reset row and col
                        row = i;
                        col = j;
                    }

                    //right
                    if (j < 6) {
                        while (board[row][col + 1] == opColor && col < 6) {
                            col++;
                        }
                        if (board[row][col + 1] == EMPTY && board[row][col] == opColor) {
                            moves.add(new int[]{row, col + 1});
                        }
                        //reset row and col
                        row = i;
                        col = j;
                    }

                    // AND NOW FOR THE DIAGONALS //

                    // down + right  ++
                    if (i < 6 && j < 6) {
                        while (board[row + 1][col + 1] == opColor && row < 6 && col < 6) {
                            row++;
                            col++;
                        }
                        if (board[row + 1][col + 1] == EMPTY && board[row][col] == opColor) {
                            moves.add(new int[]{row + 1, col + 1});
                        }
                        //reset row and col
                        row = i;
                        col = j;
                    }

                    // down + left  +-
                    if (i < 6 && j > 1) {
                        while (board[row + 1][col - 1] == opColor && row < 6 && col > 1) {
                            row++;
                            col--;
                        }
                        if (board[row + 1][col - 1] == EMPTY && board[row][col] == opColor) {
                            moves.add(new int[]{row + 1, col - 1});
                        }
                        //reset row and col
                        row = i;
                        col = j;
                    }

                    // up + left  --
                    if (i > 1 && j > 1) {
                        while (board[row - 1][col - 1] == opColor && row > 1 && col > 1) {
                            row--;
                            col--;
                        }
                        if (board[row - 1][col - 1] == EMPTY && board[row][col] == opColor) {
                            moves.add(new int[]{row - 1, col - 1});
                        }
                        //reset row and col
                        row = i;
                        col = j;
                    }

                    // up + right  -+
                    if (i > 1 && j < 6) {
                        while (board[row - 1][col + 1] == opColor && row > 1 && col < 6) {
                            row--;
                            col++;
                        }
                        if (board[row - 1][col + 1] == EMPTY && board[row][col] == opColor) {
                            moves.add(new int[]{row - 1, col + 1});
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

    public void printBoard() {
        System.out.println(toString());
    }

    public int[][] getBoardState() {
        return board;
    }

    @Override
    public String toString() {
        String format = "%s | %s | %s | %s | %s | %s | %s | %s | %s |\n";
        String divider = "  +---+---+---+---+---+---+---+---+\n";
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(format, " ", "a", "b", "c", "d", "e", "f", "g", "h").replace("|", " "));
        sb.append(divider);
        for (int i = 0; i < board.length; i++) {
            sb.append(String.format(format, (Object[]) boardRowToString(i, board[i])));
            sb.append(divider);
        }

        return sb.toString();
    }

    private String[] boardRowToString(int index, int[] row) {
        String[] rowString = new String[row.length + 1];
        rowString[0] = Integer.toString(++index);
        for (int i = 0; i < row.length; i++) {
            rowString[i + 1] = boardElementToString(row[i]);
        }

        return rowString;
    }

    private String boardElementToString(int element) {
        switch (element) {
            case BLACK:
                return "B";
            case WHITE:
                return "W";
            default:
                return " ";
        }
    }

}

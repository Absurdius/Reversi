package game;

import java.util.ArrayList;

import ai.ReversiAI;

public class Game {
    private static final int BLACK = 1;
    private static final int WHITE = -1;
    private static final int EMPTY = 0;

    private int[][] board;
    private ReversiAI ai;
    private ReversiPlayer black;
    private ReversiPlayer white;

    public Game() {
        //initialize board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = 0;
            }
        }
        board[3][3] = 1;
        board[3][4] = -1;
        board[4][3] = -1;
        board[4][4] = 1;

        // ai = new ReversiAI(this);
        //CONSTRUCTOR INCOMPLET.
    }

    public void startGame() {

        if (prompPlayerForcolor() == BLACK) {
            black = new HumanPlayer(BLACK);
            white = new AiPlayer(WHITE);
        } else {
            black = new AiPlayer(BLACK);
            white = new HumanPlayer(WHITE);
        }

        while (!hasEnded()) {
            black.getNextMove(board, getMoves(board, BLACK));
            white.getNextMove(board, getMoves(board, WHITE));
        }

        return getWinner();

    }

    public boolean hasEnded(int color) {
        return getMoves(board, color).isEmpty();
    }

    //Simply counts each color
    public String getWinner() {
        int black = 0;
        int white = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == BLACK) {
                    black++;
                }
                if (board[i][j] == WHITE) {
                    white++;
                }
            }
        }
        if (white > black) {
            return "White";
        } else if (black > white) {
            return "Black";
        } else {
            return "Draw";
        }
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
        int opColor = BLACK;
        if (color == BLACK)
            opColor = WHITE;

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

    public int[][] previewBoard(int[][] board, int row, int col) {
        // simulate a move on board and return a preview of the borad state
    }

}

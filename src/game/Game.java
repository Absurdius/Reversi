package game;

import java.util.ArrayList;

public class Game {
    public static final int BLACK = 1;
    public static final int WHITE = -1;
    public static final int EMPTY = 0;
    public static final int BOARD_SIZE = 8;

    private int[][] board;
    private ReversiPlayer black;
    private ReversiPlayer white;

    private long timeLimit;

    public Game() {
        board = new int[BOARD_SIZE][BOARD_SIZE]; //Default value of elements is zero

        board[3][3] = WHITE;
        board[4][4] = WHITE;
        board[3][4] = BLACK;
        board[4][3] = BLACK;
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
            int[] move;
            
            if (!blackMoves.isEmpty()) {
            	move = black.getNextMove(board, getMoves(board, BLACK));
            	board = move(board, move, BLACK);
            }
            if (!whiteMoves.isEmpty()) {
                move = white.getNextMove(board, getMoves(board, WHITE));
                board = move(board, move, WHITE);
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

    /**
     *
     * @param board, the board to me changed
     * @param move move to be judged
     * @param color color that makes the move
     * @return the changed (or unchanged board)
     */
    public int[][] move(int[][] board, int[] move, int color) {
    	int[][] origboard = board;
    	int opColor = (color == BLACK) ? WHITE : BLACK; 
        if (move.length < 2) {
        	// checks if move is valid
            if(getMoves(board, color).contains(move)){
            	board[move[0]][move[1]]=color;
            	int row = move[0];
            	int col = move[1];
            	
            	// Similar to possible moves we check if there is any squeezed pieces
            	// check for pieces of the opposite color
            	// walk along this line
            	int flops = 0; // number of pieces to be flopped
            	
            	// DOWN
            	while(board[row+1][col] == opColor && row < 7){
            		row++;
            		flops++;
            	}
            	// when done, check if different value is of color
            	if(board[row+1][col] == color){
            		// if so, flip every piece between move and the other piece
            		for(int i = 1; i < flops; i++){
            			board[move[0]+i][move[1]] = color;
            		}
            	}
            	//reset values for next direction
            	row = move[0];
            	col = move[1];
            	flops = 0;
            	
            	// UP
            	while(board[row-1][col] == opColor && row > 0){
            		row--;
            		flops++;
            	}
            	if(board[row-1][col] == color){
            		for(int i = 1; i < flops; i++){
            			board[move[0]-i][move[1]] = color;
            		}
            	}
            	row = move[0];
            	col = move[1];
            	flops = 0;
            	
            	// RIGHT
            	while(board[row][col+1] == opColor && col < 7){
            		col++;
            		flops++;
            	}
            	if(board[row][col+1] == color){
            		for(int i = 1; i < flops; i++){
            			board[move[0]][move[1]+i] = color;
            		}
            	}
            	row = move[0];
            	col = move[1];
            	flops = 0;
            	
            	// LEFT
            	while(board[row][col-1] == opColor && col > 0){
            		col--;
            		flops++;
            	}
            	if(board[row][col-1] == color){
            		for(int i = 1; i < flops; i++){
            			board[move[0]][move[1]-i] = color;
            		}
            	}
            	row = move[0];
            	col = move[1];
            	flops = 0;
            	
            	// AND NOW FOR THE DIAGONALS //
            	// DOWN RIGHT + +
            	while(board[row+1][col+1] == opColor && row < 7 && col < 7){
            		row++;
            		col++;
            		flops++;
            	}
            	if(board[row+1][col+1] == color){
            		for(int i = 1; i < flops; i++){
            			board[move[0]+i][move[1]+i] = color;
            		}
            	}
            	row = move[0];
            	col = move[1];
            	flops = 0;
            	
            	// DOWN LEFT + - 
            	while(board[row+1][col-1] == opColor && row < 7 && col > 0){
            		row++;
            		col--;
            		flops++;
            	}
            	if(board[row+1][col-1] == color){
            		for(int i = 1; i < flops; i++){
            			board[move[0]+i][move[1]-i] = color;
            		}
            	}
            	row = move[0];
            	col = move[1];
            	flops = 0;
            	
            	// UP LEFT - - 
            	while(board[row-1][col-1] == opColor && row > 0 && col > 0){
            		row--;
            		col--;
            		flops++;
            	}
            	if(board[row-1][col-1] == color){
            		for(int i = 1; i < flops; i++){
            			board[move[0]-i][move[1]-i] = color;
            		}
            	}
            	row = move[0];
            	col = move[1];
            	flops = 0;
            	
            	// UP RIGHT - +
            	while(board[row-1][col+1] == opColor && row > 0 && col < 7){
            		row--;
            		col++;
            		flops++;
            	}
            	if(board[row-1][col+1] == color){
            		for(int i = 1; i < flops; i++){
            			board[move[0]-i][move[1]+i] = color;
            		}
            	}
            	row = move[0];
            	col = move[1];
            	flops = 0;
            	// END OF MOVE CODE
            	return board;
            } else { // invalid move will return board in the same state 
            	return origboard;
            }
        } else {
            return origboard; // same for bad input
        }
    }

    /**
     * @param board, current board state
     * @param color, color to be placed
     * @return Arraylist of all possible moves for that state
     */

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
                    if (i > 1) {
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


    /**
     * Places a {@code color} piece on {@code board} according to {@code move} and updates {@code board} accordingly.
     *
     * @param board reversi board to update
     * @param move  move to make
     * @param color color to place
     * @return
     */
    public int[][] updateBoard(int[][] board, int[] move, int color) {
    	return move(board, move, color);
    }

    public long getTimeLimit() {
        return timeLimit;
    }
}

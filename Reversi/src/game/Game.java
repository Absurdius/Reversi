package game;

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
		// init board state
		ai = new ReversiAI(this);

	}

	public void startGame() {

		if(prompPlayerForcolor() == BLACK) {
			black = new HumanPlayer(BLACK);
			white = new AiPlayer(WHITE);
		} else {
			black = new AiPlayer(BLACK);
			white = new HumanPlayer(WHITE);
		}

		while(!hasEnded()) {
			black.getNextMove(board, getMoves());
			white.getNextMove(board, getMoves());
		}

		return getWinner();

	}

	public boolean hasEnded() {
		// are there any moves left
		return false;
	}

	public String getWinner() {
		return 'White'
	}

	public boolean move(int row, int col) {
		// returns true if the move is valid
		return false;
	}

	public int[][] getMoves(int[][] board, int color) {
		// return possible moves for THE CURRENT PLAYER in form of a 50x2 matrix
		int opColor = BLACK;
		if(color == BLACK)
		opColor = WHITE; 
		
		// dont know the max number of moves
		int number_moves = 0; 
		int[][] moves = new int[50][2]; 
		for(int i = 0; i < 50; i++)	{
			for(int j = 0; j<2; j++){
				moves[i][j] = -1; // make sure to error check ebfore letting the AI 
			}
		}
		//loop over the board row by row
		for(int i = 0; i<8; i++){
			for(int j = 0; j<8; j++)
			{
				//loop over every piece in the corresponding color
				if(board[i][j] == color){
					int row = i;
					int col = j;
					//jumps cells with opcolor until a different value is encountered
					//check in all eight directions 
					
					//down
					if(i < 6){
						while(board[row+1][col] == opColor && row < 6){row++;}
						if(board[row+1][col] == EMPTY){
							moves[number_moves][0] = row+1;
							moves[number_moves][1] = col; 
						}
					}
					
					//up
					if(i > 0){
						while(board[row-1][col] == opColor && row > 0){row--;}
						if(board[row-1][col] == EMPTY){
							moves[number_moves][0] = row-1;
							moves[number_moves][1] = col; 
						}
					}
					
					//left
					if(i > 0){
						while(board[row][col-1] == opColor && col > 0){col--;}
						if(board[row][col-1] == EMPTY){
							moves[number_moves][0] = row;
							moves[number_moves][1] = col-1; 
						}
					}
					
					//right
					if(i > 0){
						while(board[row][col+1] == opColor && col < 6){col++;}
						if(board[row][col+1] == EMPTY){
							moves[number_moves][0] = row;
							moves[number_moves][1] = col+1; 
						}
					}
					
					// AND NOW FOR THE DIAGONALS // 
					
					// down + right  ++ 
					if(i > 0){
						while(board[row+1][col+1] == opColor  && row < 6 && col < 6){row++; col++; }
						if(board[row+1][col+1] == EMPTY){
							moves[number_moves][0] = row+1;
							moves[number_moves][1] = col+1; 
						}
					}
					
					// down + left  +-  
					if(i > 0){
						while(board[row+1][col-1] == opColor && row < 6 && col > 0 ){row++; col--;}
						if(board[row+1][col-1] == EMPTY){
							moves[number_moves][0] = row+1;
							moves[number_moves][1] = col-1; 
						}
					}
					
					// up + left  --  
					if(i > 0){
						while(board[row-1][col-1] == opColor  && row > 0 && col > 0 ){row--; col--;}
						if(board[row-1][col-1] == EMPTY){
							moves[number_moves][0] = row-1;
							moves[number_moves][1] = col-1; 
						}
					}
					
					// up + right  -+  
					if(i > 0){
						while(board[row-1][col+1] == opColor && row > 0 && col < 6){row--; col++;}
						if(board[row-1][col+1] == EMPTY){
							moves[number_moves][0] = row-1;
							moves[number_moves][1] = col+1; 
						}
					}
				}
			}
		}	
		return null;
	}

	public int[][] previewBoard(int[][] board, int row, int col) {
		// simulate a move on board and return a preview of the borad state
	}

}

package game;

import ai.ReversiAI;

public class Game {
	private static final int BLACK = 0;
	private static final int WHITE = 0;
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
			black = new HumanPlayer();
			white = new AiPlayer();
		} else {
			black = new AiPlayer();
			white = new HumanPlayer();
		}

		while(!hasEnded()) {
			black.getNextMove()
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

	public int[][] getMoves(int[][] board) {
		// return possible moves
		return null;
	}

	public int[][] previewBoard(int[][] board, int row, int col) {
		// simulate a move on board and return a preview of the borad state
	}

}

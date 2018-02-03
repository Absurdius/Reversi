package game;

public class Main {

	public static void main(String[] args) {
		System.out.println("Welcome to Reversi");
		System.out.println("Please choose if you want to play as black or white. \n");
		
		//board[row][col]
		int[][] board = new int[8][8];
		
		//initialize board
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				board[i][j] = 0;
			}
		}
		
		board[7][0] = 1; 
		board[0][3] = -1; 
		
		// Printboard //
		for(int i=0; i<8; i++){
			System.out.print(" _");
		}
		System.out.print("\n");
		for(int i = 0; i < 8; i++){
			System.out.print("|");
			for(int j = 0; j < 8; j++){
				switch (board[i][j]){
				case 1: System.out.print("x");
				break;
				case -1: System.out.print("o");
				break;
				default: System.out.print("_");
				}
				System.out.print("|");
			}
			System.out.print("\n");
		}

	}
}

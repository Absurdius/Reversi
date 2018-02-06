package game;

import ai.AiPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void testGame() {
        Game game = new Game();
        ReversiPlayer player1 = new HumanPlayer();
        ReversiPlayer player2 = new AiPlayer(game);
        // First player picks game settings
        game.startGame(player1, player2);
    }

    public static void testHumanPlayer() {
        HumanPlayer hp = new HumanPlayer();
        int[][] board = {
                {-1, -1, 3, 2, 2, -1, -1, -1,},
                {-1, -1, 1, 1, 1, 1, -5, -5,},
                {-1, -1, 1, 1, 1, 1, 3, 3,},
                {-1, -1, 1, 1, 1, 1, 1, 2,},
                {2, 1, 1, 1, 1, 1, 1, 2,},
                {3, 3, 2, 1, 1, 1, 3, 3,},
                {-5, -5, 1, 1, 1, 1, -5, -5,},
                {10, -5, 3, 2, 2, 3, -5, 10,}
        };
        ArrayList<int[]> validMoves = new ArrayList<>();
        validMoves.add(new int[]{0, 0});
        validMoves.add(new int[]{1, 1});
        validMoves.add(new int[]{7, 1});
        hp.getNextMove(board, validMoves);
    }

    public static void main(String[] args) {
        //testGame();
        //testHumanPlayer();
        //testGetMoves();

//		//board[row][col]
//		int[][] board = new int[8][8];
//
//		//initialize board
//		for(int i = 0; i < 8; i++){
//			for(int j = 0; j < 8; j++){
//				board[i][j] = 0;
//			}
//		}
//
//		board[7][0] = 1;
//		board[0][3] = -1;
//		board[3][6] = -1;
//
//		// Printboard //
//		// REMINDER: ENUMERATION GOES RIGHT AND DOWN
//
//		for(int i=0; i<8; i++){
//			System.out.print(" _");
//		}
//		System.out.print("\n");
//		for(int i = 0; i < 8; i++){
//			System.out.print("|");
//			for(int j = 0; j < 8; j++){
//				switch (board[i][j]){
//				case 1: System.out.print("x");
//				break;
//				case -1: System.out.print("o");
//				break;
//				default: System.out.print("_");
//				}
//				System.out.print("|");
//			}
//			System.out.print("\n");
//		}

    }

    private static void testGetMoves() {
        Game g = new Game();
        int[][] board = new int[Game.BOARD_SIZE][Game.BOARD_SIZE]; //Default value of elements is zero

        board[3][3] = Game.WHITE;
        board[4][4] = Game.WHITE;
        board[3][4] = Game.BLACK;
        board[4][3] = Game.BLACK;

        ArrayList<int[]> validMoves = g.getMoves(board, Game.BLACK);
        for (int[] move : validMoves) {
            System.out.println(Arrays.toString(move));
        }
    }
}

package game;

import java.util.ArrayList;
import java.util.Scanner;

public class HumanPlayer implements ReversiPlayer {
    private Scanner sc = new Scanner(System.in);
    private int mycolor;

    public HumanPlayer(int color) {
        mycolor = color;
    }

    public int[] getNextMove(int[][] board, ArrayList<int[]> validMoves) {
        printBoard(board);

        int[] move = null;
        do {
            System.out.print("\nYour move:");

            String userInput = sc.nextLine();
            if (userInput.length() != 2) {
                System.out.print("Please enter a move! e.g. h5");
                continue;
            }

            int col = userInput.charAt(0) - 97;
            if (col < 0 || col > 7) {
                System.out.print("Invalid column, please use a letter between a and h.");
                continue;
            }

            int row = userInput.charAt(1) - 49;
            if (row < 0 || row > 7) {
                System.out.print("Invalid row, please use a number between 1 and 8.");
                continue;
            }

            if (!isValidMove(row, col, validMoves)) {
                System.out.println("Invalid move");
                continue;
            }

            move = new int[]{row, col};
        } while (move == null);

        return move;
    }

    private boolean isValidMove(int row, int col, ArrayList<int[]> validMoves) {
        for (int[] validMove : validMoves) {
            if (row == validMove[0] && col == validMove[1]) {
                return true;
            }
        }
        return false;
    }
}

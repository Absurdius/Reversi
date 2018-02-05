package game;

import java.util.Scanner;

public class HumanPlayer implements ReversiPlayer {

    private Scanner sc = new Scanner(System.in);
    private int mycolor;

    public HumanPlayer(int color) {
        mycolor = color;
    }

    public int[] getNextMove(int[][] moves) {
        int row = 0;
        int col = 0;
        System.out.print("Your move:");

        // checks if input is correct in different aspects
        boolean inputFail = true;
        while (inputFail) {
            String s = sc.next();
            if (s.length() > 2) {
                System.out.print("Please enter a move! e.g. h5");
                break;
            }
            // typecast chars to ints
            col = s.charAt(0) - 97;
            row = s.charAt(1) - 49;

            if (col < 0 || col > 7) {
                System.out.print("Column wrong, please use a letter between a and h.");
                break;
            }
            if (row < 0 || row > 7) {
                System.out.print("Row wrong, please use a number between 1 and 8.");
                break;
            }
            // TODO: IS IT A VALID MOVE

            // input passed all tests
            inputFail = false;
        }
        int[] move = new int[2];
        move[0] = row;
        move[1] = col;
        return move;
    }
}

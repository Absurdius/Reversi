package game;

import java.util.ArrayList;
import java.util.Scanner;

public class HumanPlayer implements ReversiPlayer {
    private Scanner sc = new Scanner(System.in);
    private int myColor;

    @Override
    public int[] getNextMove(Board board) {
        board.printBoard();

        ArrayList<int[]> validMoves = board.getMoves(myColor);
        if (validMoves.size() == 0) {
            return null;
        }

        int[] move = null;
        do {
            System.out.print("\nYour move: ");

            String userInput = sc.nextLine().toLowerCase();
            if (userInput.length() != 2) {
                System.out.print("Please enter a move! e.g. h5\n");
                continue;
            }

            int col = userInput.charAt(0) - 97;
            if (col < 0 || col > 7) {
                System.out.print("Invalid column, please use a letter between a and h.\n");
                continue;
            }

            int row = userInput.charAt(1) - 49;
            if (row < 0 || row > 7) {
                System.out.print("Invalid row, please use a number between 1 and 8.\n");
                continue;
            }

            if (!board.isValidMove(validMoves, new int[]{row, col})) {
                System.out.println("Invalid move.\n");
                continue;
            }

            move = new int[]{row, col};
        } while (move == null);

        return move;
    }

    @Override
    public int getColorPreference() {
        while (true) {
            System.out.println("Chose a color:\n\tEnter w for white\n\tEnter b for black");
            System.out.print("Color: ");

            String userInput = sc.nextLine().toLowerCase();
            if (userInput.equals("w")) {
                return Board.WHITE;
            } else if (userInput.equals("b")) {
                return Board.BLACK;
            }

            System.out.println("Invalid color choice.\n");
        }
    }

    @Override
    public long getTimeLimitPreference() {
        while (true) {
            System.out.print("Enter maximum computer thinking time in seconds: ");

            String userInput = sc.nextLine();
            if (userInput.matches("\\d+") && Long.parseLong(userInput) > 0) {
                return Long.parseLong(userInput) * 1000;
            }

            System.out.println("Invalid thinking time.\n");
        }
    }

    @Override
    public void setTimeLimit(long timeLimit) {}

    @Override
    public void setMyColor(int color) {
        myColor = color;
    }
}

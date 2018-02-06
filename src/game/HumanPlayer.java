package game;

import java.util.ArrayList;
import java.util.Scanner;

public class HumanPlayer implements ReversiPlayer {
    private Scanner sc = new Scanner(System.in);
    private int myColor;

    @Override
    public int[] getNextMove(int[][] board, ArrayList<int[]> validMoves) {
        printBoard(board);

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

            if (!isValidMove(row, col, validMoves)) {
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
                return Game.WHITE;
            } else if (userInput.equals("b")) {
                return Game.BLACK;
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
    public void setMyColor(int color) {
        myColor = color;
    }

    private boolean isValidMove(int row, int col, ArrayList<int[]> validMoves) {
        for (int[] validMove : validMoves) {
            if (row == validMove[0] && col == validMove[1]) {
                return true;
            }
        }
        return false;
    }

    private void printBoard(int[][] board) {
        String format = "%s | %s | %s | %s | %s | %s | %s | %s | %s |\n";
        String divider = "  +---+---+---+---+---+---+---+---+\n";
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(format, " ", "a", "b", "c", "d", "e", "f", "g", "h").replace("|", " "));
        sb.append(divider);
        for (int i = 0; i < board.length; i++) {
            sb.append(String.format(format, (Object[]) boardRowToString(i, board[i])));
            sb.append(divider);
        }
        System.out.println(sb.toString());
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
            case Game.BLACK:
                return "B";
            case Game.WHITE:
                return "W";
            default:
                return " ";
        }
    }
}

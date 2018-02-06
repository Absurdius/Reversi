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
            case 1:
                return "X";
            case -1:
                return "O";
            default:
                return " ";
        }
    }
}

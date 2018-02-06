package game;

import ai.AiPlayer;
import ai.RandomAi;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        //testGame();
        //testAiVsAi();
        //testHumanPlayer();
        //testGetMoves();
        testVsRandomAi();
    }

    /*
     * Limit execution time or set constant maxDepth otherwise this takes forever.
     *
     * Position AI wins 90%+ of games vs random even when depth is small.
     *
     */
    private static void testVsRandomAi() {
        int gamesToPlay = 100;
        int wins = 0;
        for (int i = 0; i < gamesToPlay; i++) {
            System.out.println("Game: " + i);
            Game game = new Game();
            game.setDebugMode(true);
            ReversiPlayer player1 = new RandomAi(game);
            ReversiPlayer player2 = new AiPlayer(game);
            // First player picks game settings
            game.startGame(player1, player2);
            if (game.getWinner() == Game.WHITE) {
                wins++;
            }
        }

        System.out.println("Position weight AI won " + wins + " of " + gamesToPlay + " vs random AI");

    }

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

    public static void testAiVsAi() {
        for (int i = 0; i < 100; i++) {
            Game game = new Game();
            ReversiPlayer player1 = new AiPlayer(game);
            ReversiPlayer player2 = new AiPlayer(game);
            game.startGame(player1, player2);
        }
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

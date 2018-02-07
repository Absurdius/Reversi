package game;

import ai.AiPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        //testGame();
        testAiVsAi();
        //testHumanPlayer();
        //testGetMoves();
    }

    public static void testGame() {
        Game game = new Game();
        ReversiPlayer player1 = new HumanPlayer();
        ReversiPlayer player2 = new AiPlayer(game);
        // First player picks game settings
        game.startGame(player1, player2);
    }

    /*
     * Use small time limit/depth or this will take forever!
     *
     * maxDepth = 4 is reasonably fast
     *
     * Not completely scientific but gives some indication of evaluation function performance.
     *
     * Starting color does influence the results.
     */
    public static void testAiVsAi() {

        AiPlayer random = new AiPlayer(null);
        random.tweakEvaluationFunction(true, false, false, false, false);

        //========================================================================
        AiPlayer positionWeights = new AiPlayer(null);
        random.tweakEvaluationFunction(false, true, false, false, false);
        aiVsAiHelper(random, "Random", positionWeights, "Position weights", 10);
        //========================================================================


        //========================================================================
        AiPlayer myMobility = new AiPlayer(null);
        myMobility.tweakEvaluationFunction(false, false, true, false, false);
        aiVsAiHelper(random, "Random", myMobility, "My mobility", 10);
        //========================================================================


        //========================================================================
        AiPlayer opponentMobility = new AiPlayer(null);
        opponentMobility.tweakEvaluationFunction(false, false, false, true, false);
        aiVsAiHelper(random, "Random", opponentMobility, "Opponent mobility", 20);
        //========================================================================

        //========================================================================
        aiVsAiHelper(myMobility, "My mobility", opponentMobility, "Opponents mobility", 20);
        //========================================================================

        //========================================================================
        aiVsAiHelper(positionWeights, "Positional weights", myMobility, "My mobility", 20);
        //========================================================================

        //========================================================================
        aiVsAiHelper(positionWeights, "Positional weights", opponentMobility, "Opponent mobility", 20);
        //========================================================================

        //========================================================================
        AiPlayer weightMobility = new AiPlayer(null); // <--- king
        weightMobility.tweakEvaluationFunction(false, true,true, true, false);
        aiVsAiHelper(positionWeights, "Positional weights", weightMobility, "Weight Mobility", 20);
        //========================================================================
    }

    public static void aiVsAiHelper(AiPlayer p1, String p1Name, AiPlayer p2, String p2Name, int gamesToPlay) {
        System.out.printf("---------- %s vs. %s -----------\n", p1Name, p2Name);
        int wins = 0;
        p1.setMyColor(Game.BLACK);
        for (int i = 0; i < gamesToPlay; i++) {
            Game game = new Game();
            game.setDebugMode(true);
            p1.setGame(game);
            p2.setGame(game);
            game.startGame(p1, p2);
            if (game.getWinner() == Game.BLACK) {
                wins++;
            }
        }

        System.out.printf("%s: %s\n", p1Name, wins);
        System.out.printf("%s: %s\n", p2Name, gamesToPlay - wins);
        System.out.println("-----------------------------------------------------------");
        System.out.println();
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

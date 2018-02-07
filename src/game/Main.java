package game;

import ai.AiPlayer;

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
        weightMobility.tweakEvaluationFunction(false, true, true, true, false);
        aiVsAiHelper(positionWeights, "Positional weights", weightMobility, "Weight Mobility", 20);
        //========================================================================
    }

    public static void aiVsAiHelper(AiPlayer p1, String p1Name, AiPlayer p2, String p2Name, int gamesToPlay) {
        System.out.printf("---------- %s vs. %s -----------\n", p1Name, p2Name);
        int wins = 0;
        p1.setMyColor(Board.BLACK);
        for (int i = 0; i < gamesToPlay; i++) {
            Game game = new Game();
            game.setDebugMode(true);
            p1.setGame(game);
            p2.setGame(game);
            game.startGame(p1, p2);
            if (game.getWinner() == Board.BLACK) {
                wins++;
            }
        }

        System.out.printf("%s: %s\n", p1Name, wins);
        System.out.printf("%s: %s\n", p2Name, gamesToPlay - wins);
        System.out.println("-----------------------------------------------------------");
        System.out.println();
    }
}

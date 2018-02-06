package ai;

import game.Game;

import java.util.ArrayList;

/**
 * Reversi AI player
 *
 * Strategy: Picks a valid move at random.
 *
 * Always prefers black.
 *
 * Used as baseline for other AI players.
 */
public class RandomAi extends AiPlayer {
    public RandomAi(Game game) {
        super(game);
    }

    @Override
    public int[] getNextMove(int[][] board, ArrayList<int[]> validMoves) {
        return validMoves.get((int) (Math.random() * (validMoves.size() - 1)));
    }

    @Override
    public int getColorPreference() {
       return Game.BLACK;
    }
}

package ai;

import game.Game;
import game.ReversiPlayer;

import java.util.ArrayList;

public class AiPlayer implements ReversiPlayer {
    private static final int NO_COLOR_PREFERENCE = -10;

    private Game game;

    private int colorPreference = NO_COLOR_PREFERENCE;
    private int myColor;
    private int opponentColor;

    private boolean useRandomMoves;
    private boolean usePositionWeights = true;
    private boolean useMyMobility = true;
    private boolean useOpponentMobility = true;
    private boolean useWinLose = false;


    public AiPlayer(Game game) {
        this.game = game;
    }

    /**
     * These options tweak the evaluation function.
     *
     * @param useRandomMoves      overrides all other options if true. Always returns a random move.
     * @param usePositionWeights  use position weight table.
     * @param useMyMobility       use number of moves.
     * @param useOpponentMobility use number of opponent moves.
     * @param useWinLose          use win/lose evaluation
     */
    public void tweakEvaluationFunction(boolean useRandomMoves, boolean usePositionWeights, boolean useMyMobility, boolean useOpponentMobility, boolean useWinLose) {
        this.useRandomMoves = useRandomMoves;
        this.usePositionWeights = usePositionWeights;
        this.useMyMobility = useMyMobility;
        this.useOpponentMobility = useOpponentMobility;
        this.useWinLose = useWinLose;
    }

    /**
     * Decides what move to play next by using min-max with alpha-beta pruning and iterative deepening.
     * Moves found after the time limit are discarded.
     *
     * @param board      the board to place a piece on
     * @param validMoves available moves to play
     * @return next move
     */
    @Override
    public int[] getNextMove(int[][] board, ArrayList<int[]> validMoves) {
        if (useRandomMoves) {
            return validMoves.get((int) (Math.random() * (validMoves.size() - 1)));
        }

        long startTime = System.currentTimeMillis();
        Node root = new Node(true, board, null);
        Node bestAction = null;
        int maxDepth = 1;

        while (true) {
            Node newBest = getNodeValue(root, Integer.MIN_VALUE, Integer.MAX_VALUE, maxDepth++).best;

            if (System.currentTimeMillis() - startTime < game.getTimeLimit()) {
                bestAction = newBest;
            } else {
                break;
            }

            root.children = new ArrayList<>();
        }

        if (bestAction == null) {
            return validMoves.size() > 0 ? validMoves.get((int) (Math.random() * (validMoves.size() - 1))) : null;
        }

        return bestAction.move;
    }

    /**
     * Adds the possible states reachable from the passed node by looking at the available actions.
     *
     * @param n node to add children to
     */
    private void addChildren(Node n) {
        int currentColor = n.isMax ? myColor : opponentColor;
        ArrayList<int[]> validMoves = game.getMoves(n.board, currentColor);
        for (int[] move : validMoves) {
            int[][] nextBoard = game.move(cloneBoard(n.board), move, currentColor);
            n.addChild(new Node(!n.isMax, nextBoard, move));
        }
    }

    /**
     * Recursive starting point min-max.
     *
     * @param n        current state
     * @param alpha    largest value on the path to the root
     * @param beta     smallest value on the path to the root
     * @param maxDepth depth limit for the current sub-tree
     * @return current state with value
     */
    private Node getNodeValue(Node n, int alpha, int beta, int maxDepth) {
        maxDepth--;

        addChildren(n);
        if (n.isLeaf() || maxDepth < 0) {
            return evaluateState(n);
        }
        if (n.isMax) {
            return getMaxNodeValue(n, alpha, beta, maxDepth);
        } else {
            return getMinNodeValue(n, alpha, beta, maxDepth);
        }
    }

    /**
     * Recursively finds the min-max value of min nodes.
     */
    private Node getMinNodeValue(Node n, int alpha, int beta, int maxDepth) {
        for (Node child : n.children) {
            getNodeValue(child, alpha, beta, maxDepth);
            if (child.value < n.value) {
                n.value = child.value;
                n.best = child;
            }
            if (n.value <= alpha) {
                return n; // Prune
            }
            beta = Math.min(beta, n.value);
        }
        return n;
    }

    /**
     * Recursively finds the min-max value of max nodes.
     */
    private Node getMaxNodeValue(Node n, int alpha, int beta, int maxDepth) {
        for (Node child : n.children) {
            getNodeValue(child, alpha, beta, maxDepth);
            if (child.value > n.value) {
                n.value = child.value;
                n.best = child;
            }
            if (n.value >= beta) {
                return n; //Prune
            }
            alpha = Math.max(alpha, n.value);
        }
        return n;
    }

    /**
     * Applies the evaluation function, as defined by {@code tweakEvaluationFunction}, to the state {@code n}.
     * Possible parameters taken into consideration are
     * - Weighted sum of current pieces based on position
     * - Number of available moves
     * - Number of available moves for opponent
     * - Win/lose
     *
     * @param n state to be evaluated
     * @return state with evaluated value
     */
    private Node evaluateState(Node n) {
        int value = 0;
        int[][] board = n.board;

        if (usePositionWeights) {
            int[][] positionWeights = {
                    {10, -5, 3, 2, 2, 3, -5, 10,},
                    {-5, -5, 1, 1, 1, 1, -5, -5,},
                    {3, 3, 1, 1, 1, 1, 3, 3,},
                    {2, 1, 1, 1, 1, 1, 1, 2,},
                    {2, 1, 1, 1, 1, 1, 1, 2,},
                    {3, 3, 2, 1, 1, 1, 3, 3,},
                    {-5, -5, 1, 1, 1, 1, -5, -5,},
                    {10, -5, 3, 2, 2, 3, -5, 10,}
            };

            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (board[i][j] == myColor) value += positionWeights[i][j];
                }
            }
        }
        int myMobility = (useMyMobility || useWinLose) ? game.getMoves(board, myColor).size() : 0;
        int opponentMobility = (useOpponentMobility || useWinLose) ? game.getMoves(board, opponentColor).size() : 0;

        if (useMyMobility) value += 5 * myMobility;
        if (useOpponentMobility) value -= 2 * opponentMobility;

        if (useWinLose && myMobility == 0 && opponentMobility == 0) {
            int winner = game.determineWinner(board);
            if (winner == myColor)
                value += 100;
            if (winner == opponentColor)
                value -= 100;
        }
        n.value = value;
        return n;
    }

    /**
     * Clones the board {@code board} so that changes to one board doesn't effect the other.
     *
     * @param board board to clone
     * @return cloned board
     */
    private int[][] cloneBoard(int[][] board) {
        int[][] clonedBoard = new int[board.length][];
        for (int i = 0; i < board.length; i++) {
            clonedBoard[i] = board[i].clone();
        }

        return clonedBoard;
    }

    /**
     * Used to add a color preference for this player.
     *
     * @param colorPreference the preferred color
     */
    public void setColorPreference(int colorPreference) {
        if (colorPreference != Game.BLACK || colorPreference != Game.WHITE) {
            throw new IllegalArgumentException("Invalid color preference");
        }

        this.colorPreference = colorPreference;
    }

    @Override
    public int getColorPreference() {
        if (colorPreference != NO_COLOR_PREFERENCE) {
            return colorPreference;
        }

        if (Math.random() < 0.5) return Game.BLACK;

        return Game.WHITE;
    }

    @Override
    public void setMyColor(int color) {
        myColor = color;
        opponentColor = (myColor == Game.BLACK) ? Game.WHITE : Game.BLACK;
    }

    @Override
    public long getTimeLimitPreference() {
        return 1000;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Describes the game state.
     *
     * Used to construct the game tree.
     */
    private class Node {
        private boolean isMax;
        private int value;
        private int[][] board;
        private int[] move;
        private Node best;
        private ArrayList<Node> children;

        private Node(boolean isMax, int[][] board, int[] move) {
            this.isMax = isMax;
            this.move = move;
            this.board = board;
            children = new ArrayList<>();
            if (isMax) {
                value = Integer.MIN_VALUE;
            } else {
                value = Integer.MAX_VALUE;
            }
        }

        private void addChild(Node child) {
            children.add(child);
        }

        private boolean isLeaf() {
            return children.isEmpty();
        }
    }
}

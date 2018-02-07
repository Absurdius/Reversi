package ai;

import game.Board;
import game.Move;
import game.ReversiPlayer;

import java.util.ArrayList;
import java.util.HashSet;

public class AiPlayer implements ReversiPlayer {
    private static final int NO_COLOR_PREFERENCE = -10;

    private int colorPreference = NO_COLOR_PREFERENCE;
    private int myColor;
    private int opponentColor;
    private long timeLimit = 1000;

    private boolean useRandomMoves = false;
    private boolean usePositionWeights = true;
    private boolean useMyMobility = true;
    private boolean useOpponentMobility = true;
    private boolean useWinLose = true;

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
     * @param board current game board
     * @return next move
     */
    @Override
    public Move getNextMove(Board board) {
        HashSet<Move> validMoves = board.getMoves(myColor);
        if (validMoves.size() == 0) {
            return null;
        }

        if (useRandomMoves) {
            return getRandomMove(validMoves);
        }

        long startTime = System.currentTimeMillis();
        Node root = new Node(true, board, null);
        Node bestAction = null;
        int maxDepth = 1;

        while (true) {
            Node newBest = getNodeValue(root, Integer.MIN_VALUE, Integer.MAX_VALUE, maxDepth++).best;
            if (System.currentTimeMillis() - startTime < timeLimit) {
                bestAction = newBest;
            } else {
                break;
            }

            root.children = new ArrayList<>();
        }

        if (bestAction == null) {
            System.out.println("Couldn't find action, playing random move.");
            return getRandomMove(validMoves);
        }

        return bestAction.move;
    }

    public Move getRandomMove(HashSet<Move> moves) {
        ArrayList<Move> moveList = new ArrayList<>(moves);
        return moveList.get((int) (Math.random() * (moveList.size() - 1)));
    }


    /**
     * Adds the possible states reachable from the passed node by looking at the available actions.
     *
     * @param n node to add children to
     */
    private void addChildren(Node n) {
        int currentColor = n.isMax ? myColor : opponentColor;
        HashSet<Move> validMoves = n.board.getMoves(currentColor);
        for (Move move : validMoves) {
            Board nextBoard = new Board(n.board);
            nextBoard.move(move, currentColor, false);

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
        int[][] board = n.board.getBoardState();

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

        int myMobility = (useMyMobility || useWinLose) ? n.board.getMoves(myColor).size() : 0;
        int opponentMobility = (useOpponentMobility || useWinLose) ? n.board.getMoves(opponentColor).size() : 0;

        if (useMyMobility) value += 2 * myMobility;
        if (useOpponentMobility){
            if (opponentMobility == 0) {
                value += 20;
            } else {
                value -= opponentMobility;
            }
        }

        if (useWinLose && myMobility == 0 && opponentMobility == 0) {
            int winner = n.board.getResult()[2];
            if (winner == myColor)
                value += 100;
            if (winner == opponentColor)
                value -= 100;
        }
        n.value = value;

        return n;
    }

    /**
     * Used to add a color preference for this player.
     *
     * @param colorPreference the preferred color
     */
    public void setColorPreference(int colorPreference) {
        if (colorPreference != Board.BLACK && colorPreference != Board.WHITE) {
            throw new IllegalArgumentException("Invalid color preference");
        }

        this.colorPreference = colorPreference;
    }

    @Override
    public int getColorPreference() {
        if (colorPreference != NO_COLOR_PREFERENCE) {
            return colorPreference;
        }

        if (Math.random() < 0.5) return Board.BLACK;

        return Board.WHITE;
    }

    @Override
    public void setMyColor(int color) {
        myColor = color;
        opponentColor = (myColor == Board.BLACK) ? Board.WHITE : Board.BLACK;
    }

    @Override
    public long getTimeLimitPreference() {
        return 1000;
    }

    @Override
    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
    }

    /**
     * Describes a game state. Used to construct the game tree.
     */
    private class Node {
        private boolean isMax;
        private int value;
        private Board board;
        private Move move;
        private Node best;
        private ArrayList<Node> children;

        private Node(boolean isMax, Board board, Move move) {
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

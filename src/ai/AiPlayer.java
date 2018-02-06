package ai;

import game.Game;
import game.ReversiPlayer;

import java.util.ArrayList;

/*
 * Done:
 *      - Min-max
 *      - Alpha-beta pruning
 *      - Iterative deepening
 *      - Time limit
 *
 * TODO:
 *      - Clean-up (Remove unused, better names, access etc.)
 *      - Evaluation function (greedy, mobility, position weights)
 *          - Stop Increasing depth once leafs are reached (win/lose/tie)
 *
 * Maybe:
 *      - Move ordering
 *      - Transposition table
 */
public class AiPlayer implements ReversiPlayer {
    private Game game;
    private int myColor;
    private int opponentColor;

    private int temp = 1;

    public AiPlayer(Game game) {
        this.game = game;
    }

    @Override
    public int getColorPreference() {
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

    @Override
    public int[] getNextMove(int[][] board, ArrayList<int[]> validMoves) {
        long startTime = System.currentTimeMillis();
        Node root = new Node(true, board, null);
        Node bestAction = null;
        int maxDepth = 1;
        while (true) {
            Node newBest = getNodeValue(root, Integer.MIN_VALUE, Integer.MAX_VALUE, maxDepth++).best;
            root.children = new ArrayList<>();
            if (System.currentTimeMillis() - startTime < game.getTimeLimit()) {
                bestAction = newBest;
            } else {
                break;
            }
        }

        if (bestAction == null) {
            return root.children.size() > 0 ? root.children.get((int) (Math.random() * (root.children.size() - 1))).move : null;
        }

        return bestAction.move;
    }

    private void addChildren(Node n) {
        int currentColor = n.isMax ? myColor : opponentColor;
        ArrayList<int[]> validMoves = game.getMoves(n.board, currentColor);
        for (int[] move : validMoves) {
            int[][] nextBoard = game.updateBoard(cloneBoard(n.board), move, currentColor);
            n.addChild(new Node(!n.isMax, nextBoard, move));
        }
    }

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

    private Node getMinNodeValue(Node n, int alpha, int beta, int maxDepth) {
        for (Node child : n.children) {
            getNodeValue(child, alpha, beta, maxDepth);
            if (child.value < n.value) {
                n.value = child.value;
                n.best = child;
            }
            if (n.value <= alpha) {
                temp++;
                return n; // Prune
            }
            beta = Math.min(beta, n.value);
        }
        return n;
    }

    private Node getMaxNodeValue(Node n, int alpha, int beta, int maxDepth) {
        for (Node child : n.children) {
            getNodeValue(child, alpha, beta, maxDepth);
            if (child.value > n.value) {
                n.value = child.value;
                n.best = child;
            }
            if (n.value >= beta) {
                temp++;
                return n; //Prune
            }
            alpha = Math.max(alpha, n.value);
        }
        return n;
    }

    public Node evaluateState(Node n) {

        int[][] positionWeights = {
                { 10,  -5,   3,   2,   2,   3,  -5,  10,},
                { -5,  -5,   1,   1,   1,   1,  -5,  -5,},
                {  3,   3,   1,   1,   1,   1,   3,   3,},
                {  2,   1,   1,   1,   1,   1,   1,   2,},
                {  2,   1,   1,   1,   1,   1,   1,   2,},
                {  3,   3,   2,   1,   1,   1,   3,   3,},
                { -5,  -5,   1,   1,   1,   1,  -5,  -5,},
                { 10,  -5,   3,   2,   2,   3,  -5,  10,}
        };

        int value = 0;

        int[][] board = n.board;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == myColor) value += positionWeights[i][j];
            }
        }

        // Foreach move my color add 5
        // Foreach move opponent color subtract 2
        // Win-bonus 1000? + handle leaf
        // Lose-penalty -1000? + handle leaf
        n.value = value;
        return n;
    }

    private int[][] cloneBoard(int[][] board) {
        int[][] clonedBoard = new int[board.length][];
        for (int i = 0; i < board.length; i++) {
            clonedBoard[i] = board[i].clone();
        }

        return clonedBoard;
    }

    private class Node {
        private boolean isMax;
        private int value;
        private int[][] board;
        private int[] move;
        private Node best;
        private ArrayList<Node> children;

        public Node(boolean isMax, int[][] board, int[] move) {
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

        public void addChild(Node child) {
            children.add(child);
        }

        public boolean isLeaf() {
            return children.isEmpty();
        }

        public boolean isRoot() {
            return move == null;
        }
    }
}

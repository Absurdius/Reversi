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
 *      - Integrate with game
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
    private long timeLimit = 1000;

    private int temp = 1;

    public AiPlayer(Game game, long timeLimit, int myColor, int opponentColor) {
        this.game = game;
        this.timeLimit = timeLimit;
        this.myColor = myColor;
        this.opponentColor = opponentColor;
    }

    public int[] getNextMove(int[][] board, ArrayList<int[]> validMoves) {
        long startTime = System.currentTimeMillis();
        Node root = new Node(true, board, null);
        Node bestAction = null;
        int maxDepth = 1;
        while (true) {
            Node newBest = getNodeValue(root, Integer.MIN_VALUE, Integer.MAX_VALUE, maxDepth++).best;
            root.children = new ArrayList<>();
            if (System.currentTimeMillis() - startTime < timeLimit) {
                bestAction = newBest;
                System.out.println(bestAction.value);
            } else {
                break;
            }
        }

        if (bestAction == null) {
            return root.children.size() > 0 ? root.children.get(0).move : null;
        }

        return bestAction.move;
    }

    private void addChildren(Node n, int[][] possibleMoves) {
        for (int i = 0; i < possibleMoves.length; i++) {
            int row = possibleMoves[i][0];
            int col = possibleMoves[i][1];

            int nextColor = n.isMax ? opponentColor : myColor;
            n.addChild(new Node(!n.isMax, game.previewMove(n.board, row, col, nextColor), possibleMoves[i]));
        }
    }

    private Node getNodeValue(Node n, int alpha, int beta, int maxDepth) {
        maxDepth--;

        int currentColor = n.isMax ? myColor : opponentColor;
        addChildren(n, game.getMoves(n.board, currentColor));
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

        n.value = temp++;
        return n;
/*        int[][] positionWeights = {
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

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == color) value += positionWeights[i][j];
            }
        }

        return value;*/
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

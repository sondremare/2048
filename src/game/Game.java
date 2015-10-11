package game;

import search.AdversarialSearch;
import search.alphabeta.AlphaBeta;

public class Game {
    private Board board;
    private AdversarialSearch adversarialSearch;
    public static int[] loopOrder;
    public static int[] reverseLoopOrder;
    private int maxDepth;

    public Game(AdversarialSearch adversarialSearch, int maxDepth) {
        this.board = new Board();
        this.adversarialSearch = adversarialSearch;
        this.maxDepth = maxDepth;
        loopOrder = new int[Board.SIZE];
        reverseLoopOrder = new int[Board.SIZE];
        for (int j = 0; j < Board.SIZE; j++) {
            loopOrder[j] = j;
            reverseLoopOrder[j] = Math.abs((Board.SIZE-1)-j);
        }
    }

    public boolean playStep() {
        Direction direction = adversarialSearch.decision(board, maxDepth);
        System.out.println(direction);
        if (direction != null) {
            this.board = applyDirection(board, direction);
            return true;
        } else {
            return false;
        }
    }

    public static Board applyDirection(Board board, Direction direction) {
        Board child = new Board(board);
        boolean moved = child.move(direction);
        if (moved) {
            return child;
        } else {
            return null;
        }
    }

    public Board getBoard() {
        return board;
    }

    public static int[][] getLoopOrder(Direction direction) {
        int[][] loopOrders = new int[2][Board.SIZE];
        loopOrders[0] = loopOrder;
        loopOrders[1] = loopOrder;
        if (direction == Direction.DOWN) {
            loopOrders[1] = reverseLoopOrder;
        } else if (direction == Direction.RIGHT) {
            loopOrders[0] = reverseLoopOrder;
        }
        return loopOrders;
    }
}

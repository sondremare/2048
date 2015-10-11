package search.alphabeta;

import game.Board;
import game.Cell;
import game.Direction;
import game.Game;
import search.AdversarialSearch;
import search.Heuristic;

import java.util.ArrayList;

public class AlphaBeta implements AdversarialSearch{
    private double alpha;
    private double beta;
    private Heuristic heuristic;

    public AlphaBeta(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public Direction decision(Board board, int maxDepth) {
        Direction bestMove = null;
        alpha = -Double.MAX_VALUE;
        beta = Double.MAX_VALUE;
        double bestMoveValue = -Double.MAX_VALUE;
        for (Direction direction : Direction.values()) {
            Board child = Game.applyDirection(board, direction);
            if (child == null) continue; //If child is null, no cell was moved or merged with the given direction. So we skip it
            double moveValue = minValue(child, alpha , beta, maxDepth);
            System.out.println(direction + ": "+moveValue);
            if (moveValue > bestMoveValue || bestMove == null) {
                bestMoveValue = moveValue;
                bestMove = direction;
            }
        }
        return bestMove;
    }

    @Override
    public double getHeuristicValue(Board board) {
        return heuristic.getHeuristicValue(board);
    }

    private double minValue(Board board, double alpha, double beta, int depth) {
        if (cutOffTest(depth, board)) {
            return getHeuristicValue(board);
        }
        double value = Double.MAX_VALUE;
        for (Board child : getMinSuccessors(board)) {
            value = Math.min(value, maxValue(child, alpha, beta, depth - 1));
            if (value <= alpha) {
                return value;
            }
            beta = Math.min(beta, value);
        }
        return value;
    }

    private double maxValue(Board board, double alpha, double beta, int depth) {
        if (cutOffTest(depth, board)) {
            return getHeuristicValue(board);
        }
        double value = -Double.MAX_VALUE;
        for (Board child : getMaxSuccessors(board)) {
            value = Math.max(value, minValue(child, alpha, beta, depth - 1));
            if (value >= beta) {
                return value;
            }
            alpha = Math.max(alpha, value);
        }
        return value;
    }

    private boolean cutOffTest(int depth, Board board) {
        return (depth <= 0 || board.hasMovesLeft());
    }

    private ArrayList<Board> getMinSuccessors(Board board) {
        ArrayList<Board> children = new ArrayList<>();
        ArrayList<Cell> emptyCells = board.getEmptyCells();
        for (Cell emptyCell : emptyCells) {
            Board childWithValueTwo = new Board(board);
            childWithValueTwo.getCells()[emptyCell.getX()][emptyCell.getY()].setValue(2);
            children.add(childWithValueTwo);
            Board childWithValueFour = new Board(board);
            childWithValueFour.getCells()[emptyCell.getX()][emptyCell.getY()].setValue(4);
            children.add(childWithValueFour);
        }
        return children;
    }

    private ArrayList<Board> getMaxSuccessors(Board board) {
        ArrayList<Board> children = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            Board child = Game.applyDirection(board, direction);
            if (child != null) { //We only add children where there direction caused a move or a merge.
                children.add(child);
            }

        }
        return children;
    }
}

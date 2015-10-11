package search.expectimax;

import game.Board;
import game.Cell;
import game.Direction;
import game.Game;
import search.AdversarialSearch;
import search.Heuristic;

import java.util.ArrayList;

public class Expectimax implements AdversarialSearch{
    public static int MAX_DEPTH = 7;
    private Heuristic heuristic;

    public Expectimax(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public Direction decision(Board board) {
        Direction bestMove = null;
        double bestMoveValue = -Double.MAX_VALUE;
        for (Direction direction : Direction.values()) {
            Board child = Game.applyDirection(board, direction);
            if (child == null) continue; //If child is null, no cell was moved or merged with the given direction. So we skip it
            double moveValue = expValue(child, MAX_DEPTH);
            System.out.println(direction + ": "+moveValue);
            if (moveValue > bestMoveValue || bestMove == null) {
                bestMoveValue = moveValue;
                bestMove = direction;
            }
        }
        return bestMove;
    }

    private double expValue(Board board, int depth) {
        if (cutOffTest(depth, board)) {
            return getHeuristicValue(board);
        }
        double value = 0;
        ArrayList<ChanceNode> children = getExpSuccessors(board);
        double childLength = children.size();
        for (ChanceNode child : children) {
            double probability = child.getProbability() * (1.0/board.getEmptyCells().size());
            value += probability * maxValue(child.getBoard(), depth - 1);
        }
        return value;
    }

    private double maxValue(Board board, int depth) {
        if (cutOffTest(depth, board)) {
            return getHeuristicValue(board);
        }
        double value = -Double.MAX_VALUE;
        for (Board child : getMaxSuccessors(board)) {
            value = Math.max(value, expValue(child, depth - 1));
        }
        return value;
    }

    @Override
    public double getHeuristicValue(Board board) {
        return heuristic.getHeuristicValue(board);
    }


    private boolean cutOffTest(int depth, Board board) {
        return (depth <= 0 || board.hasMovesLeft());
    }

    private ArrayList<ChanceNode> getExpSuccessors(Board board) {
        ArrayList<ChanceNode> children = new ArrayList<>();
        ArrayList<Cell> emptyCells = board.getEmptyCells();
        for (Cell emptyCell : emptyCells) {
            Board childWithValueTwo = new Board(board);
            childWithValueTwo.getCells()[emptyCell.getX()][emptyCell.getY()].setValue(2);
            children.add(new ChanceNode(0.9, childWithValueTwo));
            Board childWithValueFour = new Board(board);
            childWithValueFour.getCells()[emptyCell.getX()][emptyCell.getY()].setValue(4);
            children.add(new ChanceNode(0.1, childWithValueFour));
        }
        return children;
    }

    private ArrayList<Board> getMaxSuccessors(Board board) {
        ArrayList<Board> children = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            Board child = Game.applyDirection(board, direction);
            if (child != null) { //We only add children where there direction caused a move or a merge.
                children.add(Game.applyDirection(board, direction));
            }

        }
        return children;
    }
}

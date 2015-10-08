package search.expectimax;

import game.Board;

public class ChanceNode {
    private double probability;
    private Board board;

    public ChanceNode(double probability, Board board) {
        this.probability = probability;
        this.board = board;
    }

    public double getProbability() {
        return probability;
    }

    public Board getBoard() {
        return board;
    }
}

package search;

import game.Board;
import game.Cell;

public class Heuristic {
    //TODO, figure out if I need to remember last two moves for better gradient heuristic
    public double[][] topLeftGradient;
    public double[][] bottomLeftGradient;
    public double[][] topRightGradient;
    public double[][] bottomRightGradient;
    private double maxWeight = 1.2;
    private double weightDecrement = 0.2;

    public Heuristic() {
        this.topLeftGradient = new double[Board.SIZE][Board.SIZE];
        this.bottomLeftGradient = new double[Board.SIZE][Board.SIZE];
        this.topRightGradient = new double[Board.SIZE][Board.SIZE];
        this.bottomRightGradient = new double[Board.SIZE][Board.SIZE];
        int outerCounter = 0;
        while (outerCounter < Board.SIZE) {
            int innerCounter = 0;
            double weight = maxWeight;
            while (innerCounter < Board.SIZE) {
                topLeftGradient[outerCounter][innerCounter] = weight;
                topRightGradient[Math.abs((Board.SIZE - 1) - outerCounter)][innerCounter] = weight;
                bottomLeftGradient[outerCounter][Math.abs((Board.SIZE - 1) - innerCounter)] = weight;
                bottomRightGradient[Math.abs((Board.SIZE - 1) - outerCounter)][Math.abs((Board.SIZE - 1) - innerCounter)] = weight;
                weight -= weightDecrement;
                innerCounter++;
            }
            maxWeight -= weightDecrement;
            outerCounter++;
        }
    }

    /*public double getHeuristicValue(Board board) {
        //System.out.println(board);
        double topLeftSum = 0;
        double bottomLeftSum = 0;
        double topRightSum = 0;
        double bottomRightSum = 0;
        Cell[][] cells = board.getCells();
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                int cellValue = cells[i][j].getValue();
                double scaledCellValue = cellValue == 0 ? 0 : Math.log(cells[i][j].getValue())/Math.log(2);
                topLeftSum += cellValue * topLeftGradient[i][j];
                bottomLeftSum += cellValue * bottomLeftGradient[i][j];
                topRightSum += cellValue * topRightGradient[i][j];
                bottomRightSum += cellValue * bottomRightGradient[i][j];
            }
        }
        double gradientScore = Math.max(Math.max(topLeftSum, bottomLeftSum), Math.max(topRightSum, bottomRightSum));
        return gradientScore + board.getEmptyCells().size() * 10;
    }*/

    public double getHeuristicValue(Board board) {
        if (!board.hasMovesLeft()) {
            return 0;
        }
        //System.out.println(board);
        double topLeftSum = 0;
        double bottomLeftSum = 0;
        double topRightSum = 0;
        double bottomRightSum = 0;
        Cell[][] cells = board.getCells();
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                int cellValue = cells[i][j].getValue();
                double scaledCellValue = cellValue;// == 0 ? 0 : Math.log(cells[i][j].getValue())/Math.log(2);
                topLeftSum += cellValue * topLeftGradient[i][j];
                bottomLeftSum += cellValue * bottomLeftGradient[i][j];
                topRightSum += cellValue * topRightGradient[i][j];
                bottomRightSum += cellValue * bottomRightGradient[i][j];
            }
        }
        double gradientScore = Math.max(Math.max(topLeftSum, bottomLeftSum), Math.max(topRightSum, bottomRightSum));
        //return gradientScore;// works kinda decent with alphabeta
        return gradientScore + Math.log(gradientScore)*board.getEmptyCells().size();// * 20;
    }
}

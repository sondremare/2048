package search;

import game.Board;
import game.Cell;
import game.Position;

public class Heuristic {
    //TODO, figure out if I need to remember last two moves for better gradient heuristic
    public double[][] topLeftHorizontal;
    public double[][] topLeftVertical;
    public double[][] topRightHorizontal;
    public double[][] topRightVertical;
    public double[][] bottomLeftHorizontal;
    public double[][] bottomLeftVertical;
    public double[][] bottomRightHorizontal;
    public double[][] bottomRightVertical;

    public Heuristic() {
        this.topLeftHorizontal = new double[][]{
            {16, 15, 14, 13},
            {9, 10, 11, 12},
            {8, 7, 6, 5},
            {1, 2, 3, 4}
        };
        this.topLeftVertical = new double[][]{
            {16, 9, 8, 1},
            {15, 10, 7, 2},
            {14, 11, 6, 3},
            {13, 12, 5, 4}
        };
        this.topRightHorizontal = new double[Board.SIZE][Board.SIZE];
        this.topRightVertical = new double[Board.SIZE][Board.SIZE];
        this.bottomLeftHorizontal = new double[Board.SIZE][Board.SIZE];
        this.bottomLeftVertical = new double[Board.SIZE][Board.SIZE];
        this.bottomRightHorizontal = new double[Board.SIZE][Board.SIZE];
        this.bottomRightVertical = new double[Board.SIZE][Board.SIZE];
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                int oppositeI = Math.abs(Board.SIZE - 1) - i;
                int oppositeJ = Math.abs(Board.SIZE - 1) - j;
                topRightHorizontal[i][oppositeJ] = topLeftHorizontal[i][j];
                topRightVertical[i][oppositeJ] = topLeftVertical[i][j];

                bottomLeftHorizontal[oppositeI][j] = topLeftHorizontal[i][j];
                bottomLeftVertical[oppositeI][j] = topLeftVertical[i][j];

                bottomRightHorizontal[oppositeI][oppositeJ] = topLeftHorizontal[i][j];
                bottomRightVertical[oppositeI][oppositeJ] = topLeftVertical[i][j];
            }
        }
        /*int outerCounter = 0;
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
        }*/
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
        if (board.hasWon()) {
            return Double.MAX_VALUE;
        }
        //System.out.println(board);
        double topLeftHorizontalSum = 0;
        double topLeftVerticalSum = 0;
        double topRightHorizontalSum = 0;
        double topRightVerticalSum = 0;
        double bottomLeftHorizontalSum = 0;
        double bottomLeftVerticalSum = 0;
        double bottomRightHorizontalSum = 0;
        double bottomRightVerticalSum = 0;

        int possibleMergeValues = 0;
        Cell[][] cells = board.getCells();
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                int cellValue = cells[i][j].getValue();
                double scaledCellValue = cellValue; //== 0 ? 0 : (Math.log(cellValue)/Math.log(2));
                topLeftHorizontalSum += scaledCellValue * topLeftHorizontal[i][j];
                topLeftVerticalSum += scaledCellValue * topLeftVertical[i][j];
                topRightHorizontalSum += scaledCellValue * topRightHorizontal[i][j];
                topRightVerticalSum += scaledCellValue * topRightVertical[i][j];
                bottomLeftHorizontalSum += scaledCellValue * bottomLeftVertical[i][j];
                bottomLeftVerticalSum += scaledCellValue * bottomLeftVertical[i][j];
                bottomRightHorizontalSum += scaledCellValue * bottomRightHorizontal[i][j];
                bottomRightVerticalSum += scaledCellValue * bottomRightVertical[i][j];

                //Calculating heuristic for possible merges
                if (cellValue != Board.EMPTY) {
                    Position horizontalNeighbor = new Position(i+1, j);
                    Position verticalNeighbor = new Position(i, j+1);
                    if (Board.withinBounds(horizontalNeighbor) && cells[horizontalNeighbor.getX()][horizontalNeighbor.getY()].getValue() == cellValue) {
                        possibleMergeValues += cellValue;//Math.log(cellValue)/Math.log(2.0);
                    }
                    if (Board.withinBounds(verticalNeighbor) && cells[verticalNeighbor.getX()][verticalNeighbor.getY()].getValue() == cellValue) {
                        possibleMergeValues += cellValue;//Math.log(cellValue)/Math.log(2.0);
                    }
                }


            }
        }
        double gradientScore = Math.max(Math.max(Math.max(topLeftHorizontalSum, topLeftVerticalSum), Math.max(topRightHorizontalSum, topRightVerticalSum)), Math.max(Math.max(bottomLeftHorizontalSum, bottomLeftVerticalSum),Math.max(bottomRightHorizontalSum, bottomRightVerticalSum)));
        double emptyCellsScore = /*Math.log(gradientScore)**/board.getEmptyCells().size();
        //System.out.println("GradientScore: "+gradientScore);
        //System.out.println("Empty cells Score: "+emptyCellsScore);
       // System.out.println("Merge Score: "+possibleMergeValues);
        //System.out.println("*****************************");

        double gradientWeight = 1;
        double emptyCellWeight = 10;
        double possibleMergeWeight = 4;

        return gradientScore*gradientWeight + emptyCellsScore*emptyCellWeight + possibleMergeValues*possibleMergeWeight;// * 20;
    }
}

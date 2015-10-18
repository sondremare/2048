package search;

import game.Board;
import game.Cell;
import game.Position;
import javafx.util.Pair;

import java.util.ArrayList;

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

    public ArrayList<Position> topLeftHorizontalTraversal;
    public ArrayList<Position> topLeftVerticalTraversal;
    public ArrayList<Position> topRightHorizontalTraversal;
    public ArrayList<Position> topRightVerticalTraversal;
    public ArrayList<Position> bottomLeftHorizontalTraversal;
    public ArrayList<Position> bottomLeftVerticalTraversal;
    public ArrayList<Position> bottomRightHorizontalTraversal;
    public ArrayList<Position> bottomRightVerticalTraversal;


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

        topLeftHorizontalTraversal = createTraversal(true, false, false);
        topLeftVerticalTraversal = createTraversal(false, false, false);
        topRightHorizontalTraversal = createTraversal(true, false, true);
        topRightVerticalTraversal = createTraversal(false, true, false);
        bottomLeftHorizontalTraversal = createTraversal(true, true, false);
        bottomLeftVerticalTraversal = createTraversal(false, false, true);
        bottomRightHorizontalTraversal = createTraversal(true, true, true);
        bottomRightVerticalTraversal = createTraversal(false, true, true);

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

    public ArrayList<Position> createTraversal(boolean isHorizontal, boolean areRowsReversed, boolean areColumnsReversed) {
        int columnRemainder = areColumnsReversed == areRowsReversed ? 0 : 1;
        ArrayList<Position> traversal = new ArrayList<>();
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                int row = areRowsReversed ? Math.abs((Board.SIZE - 1) - i) : i;
                int col;
                if (row % 2 == columnRemainder) {
                    col = j;
                    //traversal.add(new Pair(row,j));
                } else {
                    //traversal.add(new Pair(row, Math.abs((Board.SIZE - 1)) - j));
                    col = Math.abs((Board.SIZE - 1 ) - j);
                }
                if (isHorizontal) {
                    traversal.add(new Position(row, col));
                } else {
                    traversal.add(new Position(col, row));
                }

            }
        }
        return traversal;
    }

    public double getHeuristicValue(Board board) {
        if (!board.hasMovesLeft()) {
            return 0;
        }
        if (board.hasWon()) {
            return Double.MAX_VALUE;
        }
        double topLeftHorizontalSum = 0;
        double topLeftVerticalSum = 0;
        double topRightHorizontalSum = 0;
        double topRightVerticalSum = 0;
        double bottomLeftHorizontalSum = 0;
        double bottomLeftVerticalSum = 0;
        double bottomRightHorizontalSum = 0;
        double bottomRightVerticalSum = 0;

        double clusterPenalty = 0;

        int possibleMergeValues = 0;
        Cell[][] cells = board.getCells();
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                int cellValue = cells[i][j].getValue();
                double scaledCellValue = cellValue;// == 0 ? 0 : (Math.log(cellValue)/Math.log(2));
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
                    Position horizontalNeighborPosition = new Position(i + 1, j);
                    Position verticalNeighborPosition = new Position(i, j + 1);
                    if (Board.withinBounds(horizontalNeighborPosition)) {
                        Cell horizontalNeighbor = cells[horizontalNeighborPosition.getX()][horizontalNeighborPosition.getY()];
                        if (horizontalNeighbor.getValue() == cellValue) {
                            possibleMergeValues++;
                        } else if (horizontalNeighbor.getValue() != Board.EMPTY) {
                            clusterPenalty += Math.abs(horizontalNeighbor.getValue() - cellValue);
                        }
                    }

                    if (Board.withinBounds(verticalNeighborPosition)) {
                        Cell verticalNeighbor = cells[verticalNeighborPosition.getX()][verticalNeighborPosition.getY()];
                        if (verticalNeighbor.getValue() == cellValue) {
                            possibleMergeValues++;
                        } else if (verticalNeighbor.getValue() != Board.EMPTY) {
                            clusterPenalty += Math.abs(verticalNeighbor.getValue() - cellValue);
                        }
                    }
                }


            }
        }
        //System.out.println(board);
        double gradientScore = Math.max(Math.max(Math.max(topLeftHorizontalSum, topLeftVerticalSum), Math.max(topRightHorizontalSum, topRightVerticalSum)), Math.max(Math.max(bottomLeftHorizontalSum, bottomLeftVerticalSum),Math.max(bottomRightHorizontalSum, bottomRightVerticalSum)));

        double penalty = 0;
        ArrayList<Position> traversal;
        if (gradientScore == topLeftHorizontalSum) {
            traversal = topLeftHorizontalTraversal;
        } else if (gradientScore == topLeftVerticalSum) {
            traversal = topLeftVerticalTraversal;
        } else if (gradientScore == topRightHorizontalSum) {
            traversal = topRightHorizontalTraversal;
        } else if (gradientScore == topRightVerticalSum) {
            traversal = topRightVerticalTraversal;
        } else if (gradientScore == bottomLeftHorizontalSum) {
            traversal = bottomLeftHorizontalTraversal;
        } else if (gradientScore == bottomLeftVerticalSum) {
            traversal = bottomLeftVerticalTraversal;
        } else if (gradientScore == bottomRightHorizontalSum) {
            traversal = bottomRightHorizontalTraversal;
        } else {
            traversal = bottomRightVerticalTraversal;
        }
        int previousValue = Integer.MAX_VALUE;
        for (Position pos : traversal) {
            int cellValue = cells[pos.getX()][pos.getY()].getValue();
            if (cellValue > previousValue) {
                penalty += cellValue - previousValue;
            }
            previousValue = cellValue;
        }
        double emptyCellsScore = /*Math.log(gradientScore)**/board.getEmptyCells().size();


        double gradientWeight = 1;
        double emptyCellWeight = 0.5 * (1.0 / 16.0) * gradientScore;
        double possibleMergeWeight = 4;
        double clusterWeight = 0;
        double penaltyWeight = Math.log(gradientScore) / Math.log(2);

       // System.out.println("GradientScore: "+ gradientScore + " ("+ (gradientScore * gradientWeight) + ")");
        //System.out.println("ClusterPenalty: "+ clusterPenalty + " ("+ (clusterPenalty * clusterWeight) + ")");
       // System.out.println("Empty cells: "+ emptyCellsScore + " ("+ (emptyCellsScore * emptyCellWeight) + ")");
        //System.out.println("Possible merges: "+ possibleMergeValues + " ("+ (possibleMergeValues * possibleMergeWeight) + ")");
        //double heuristicValue = gradientScore*gradientWeight + emptyCellsScore*emptyCellWeight + possibleMergeValues*possibleMergeWeight - clusterPenalty*clusterWeight;
        //System.out.println("clusterPenalty: "+clusterPenalty);
        //System.out.println("GradientScore: "+gradientScore);
        //System.out.println("EmptyCellScore: "+emptyCellsScore*emptyCellWeight + " ("+emptyCellsScore+")");
        //System.out.println("DescendingPenalty: "+penalty);
        double heuristicValue = gradientScore + (emptyCellsScore*emptyCellWeight) - (penalty * penaltyWeight);
        //System.out.println("Total Score: "+heuristicValue);
        //System.out.println("********************************");
        return heuristicValue;
    }
}

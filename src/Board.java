import javafx.geometry.Pos;

import java.util.ArrayList;

public class Board {

    public static int SIZE = 4;
    public static int EMPTY = 0;
    public Cell[][] cells;
    private int[] loopOrder;
    private int[] reverseLoopOrder;

    public Board() {
        cells = new Cell[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j] = new Cell(i,j,0);
            }
        }
        loopOrder = new int[SIZE];
        reverseLoopOrder = new int[SIZE];
        for (int j = 0; j < SIZE; j++) {
            loopOrder[j] = j;
            reverseLoopOrder[j] = Math.abs((SIZE-1)-j);
        }
        addRandomCell();
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void addRandomCell() {
        ArrayList<Cell> emptyCells = getEmptyCells();
        int index = (int)(Math.random()*emptyCells.size());
        Cell chosenCell = emptyCells.get((int)(Math.random()*emptyCells.size()));
        int value = 2;
        if (Math.random() > 0.9) {
            value = 4;
        }
        chosenCell.setValue(value);
    }

    public boolean move(Direction direction) {
        boolean movedOrMerged = false;
        int[][] loopOrders = getLoopOrder(direction);

        for (int i : loopOrders[0]) {
            for (int j : loopOrders[1]) {
                Cell cell = cells[i][j];
                cell.setRecentlyMerged(false);
                if (cell.getValue() != EMPTY) {
                    Cell neighbor = moveAndFindClosestNeighbor(cell, direction);
                    if (neighbor != null) {
                        if (neighbor.getValue() == cell.getValue() && !neighbor.isRecentlyMerged()) {
                            neighbor.setValue(neighbor.getValue()*2);
                            neighbor.setRecentlyMerged(true);
                            cells[i][j] = new Cell(i, j, 0);
                            movedOrMerged = true;
                        } else {
                            if (cellMoved(cell, i, j)) {
                                cells[i][j] = new Cell(i, j, 0);
                                cells[cell.getX()][cell.getY()] = cell;
                                movedOrMerged = true;
                            }
                        }
                    } else {
                        if (cellMoved(cell, i, j)) {
                            cells[i][j] = new Cell(i, j, 0);
                            cells[cell.getX()][cell.getY()] = cell;
                            movedOrMerged = true;
                        }
                    }
                }

            }
        }
        if (movedOrMerged) {
            addRandomCell();
        }
        return movedOrMerged;
    }

    private boolean cellMoved(Cell cell, int x, int y) {
        return (x != cell.getX() || y != cell.getY());
    }

    public int[][] getLoopOrder(Direction direction) {
        int[][] loopOrders = new int[2][SIZE];
        loopOrders[0] = loopOrder;
        loopOrders[1] = loopOrder;
        if (direction == Direction.DOWN) {
            loopOrders[1] = reverseLoopOrder;
        } else if (direction == Direction.RIGHT) {
            loopOrders[0] = reverseLoopOrder;
        }
        return loopOrders;
    }

    public ArrayList<Cell> getEmptyCells() {
        ArrayList<Cell> emptyCells = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Cell cell = cells[i][j];
                if (cell.getValue() == EMPTY) {
                    emptyCells.add(cell);
                }
            }
        }
        return emptyCells;
    }

    public Cell moveAndFindClosestNeighbor(Cell cell, Direction direction) {
        Position directionVector = Position.getDirectionVector(direction);
        Position currentPosition = cell.getPosition();
        Position nextPos = new Position(currentPosition.getX() + directionVector.getX(), currentPosition.getY() + directionVector.getY());
        while(withinBounds(nextPos)) {
            if (!cellAvailable(nextPos)) {
                return cells[nextPos.getX()][nextPos.getY()];
            }
            currentPosition = nextPos;
            nextPos = new Position(nextPos.getX() + directionVector.getX(), nextPos.getY() + directionVector.getY());
            cell.setPosition(currentPosition);
        }
        return null;
    }

    private boolean withinBounds(Position position) {
        return position.getX() >= 0 && position.getX() < SIZE &&
                position.getY() >= 0 && position.getY() < SIZE;
    }

    private boolean cellAvailable(Position position) {
        return cells[position.getX()][position.getY()].getValue() == EMPTY;
    }

    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                res += cells[j][i].getValue() + "|";
            }
            res += "\n";
        }
        return res;
    }
}

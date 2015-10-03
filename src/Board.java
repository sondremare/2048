import java.util.Iterator;

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
        /*reverseLoopOrder = new int[SIZE];
        for (int k = SIZE - 1; k >= 0; k--) {
            reverseLoopOrder[k] = k;
        }*/
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void addRandomCell(int value) {
        int randomX = (int)(Math.random()* SIZE);
        int randomY = (int)(Math.random()*SIZE);
        Cell cell = new Cell(randomX, randomY, value);
        cells[randomX][randomY] = cell;
    }

    public void move(Direction direction) {
        int[][] loopOrders = getLoopOrder(direction);

        for (int i : loopOrders[0]) {
            for (int j : loopOrders[1]) {
                Cell cell = cells[i][j];
                Cell neighbor = findClosestNeighbor(cell, direction);
                if (!cell.equals(neighbor)) {
                    cells[i][j] = new Cell(i, j, 0);
                    cell.setX(neighbor.getX()); //TODO refactor as Position
                    cell.setY(neighbor.getY());
                    cells[cell.getX()][cell.getY()] = cell;
                }
            }
        }
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

    public Cell findClosestNeighbor(Cell cell, Direction direction) {
        Cell previous;
        Vector directionVector = Vector.getDirectionVector(direction);
        do {
            previous = cell;
            cell = new Cell(previous.getX() + directionVector.getX(), previous.getY() + directionVector.getY());
        }
        while(this.withinBounds(cell) && this.cellAvailable(cell));

        return previous;
    }

    private boolean withinBounds(Cell cell) {
        return cell.getX() >= 0 && cell.getX() < SIZE &&
                cell.getY() >= 0 && cell.getY() < SIZE;
    }

    private boolean cellAvailable(Cell cell) {
        return cells[cell.getX()][cell.getY()].getValue() == EMPTY;
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

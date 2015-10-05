public class Cell {
    private Position position;
    private int value;
    private boolean recentlyMerged = false;

    public Cell(int x, int y) {
        this.position = new Position(x, y);
        this.value = 0;
    }

    public Cell(int x, int y, int value) {
        this.position = new Position(x, y);
        this.value = value;
    }

    public int getX() {
        return position.getX();
    }

    public void setX(int x) {
        setX(x);
    }

    public int getY() {
        return position.getY();
    }

    public void setY(int y) {
        setY(y);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Position getPosition() {
        return position;
    }

    public int[] distance(Cell cell) {
        int[] distance = new int[2];
        distance[0] = Math.abs(this.getX() - cell.getX()) - 1;
        distance[1] = Math.abs(this.getY() - cell.getY()) - 1;
        return distance;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isRecentlyMerged() {
        return recentlyMerged;
    }

    public void setRecentlyMerged(boolean recentlyMerged) {
        this.recentlyMerged = recentlyMerged;
    }
 }

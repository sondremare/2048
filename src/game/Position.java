package game;

public class Position {
    public static Position LEFT_VECTOR = new Position(-1, 0);
    public static Position UP_VECTOR = new Position(0, -1);
    public static Position RIGHT_VECTOR = new Position(1, 0);
    public static Position DOWN_VECTOR = new Position(0, 1);

    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static Position getDirectionVector(Direction direction) {
        switch (direction) {
            case LEFT:
                return LEFT_VECTOR;
            case UP:
                return UP_VECTOR;
            case RIGHT:
                return RIGHT_VECTOR;
            case DOWN:
                return DOWN_VECTOR;
        }
        return null;
    }
}

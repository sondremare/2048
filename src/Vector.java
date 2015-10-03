public class Vector {
    private int x;
    private int y;

    public Vector(int x, int y) {
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

    //TODO REFACTOR THIS SHIT, no need to create new ones all the time

    public static Vector getDirectionVector(Direction direction) {
        switch (direction) {
            case LEFT:
                return new Vector(-1, 0);
            case UP:
                return new Vector(0, -1);
            case RIGHT:
                return new Vector(1, 0);
            case DOWN:
                return new Vector(0, 1);
        }
        return null;
    }
}

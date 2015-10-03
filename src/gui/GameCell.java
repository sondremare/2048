package gui;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameCell extends StackPane{
    public static int CELL_WIDTH = 100;
    public static int CELL_HEIGHT = 100;

    private Rectangle rectangle;
    private Text text;

    public GameCell(int value) {
        super();
        text = new Text();
        text.setFill(Color.WHITE);
        rectangle = new Rectangle(CELL_WIDTH, CELL_HEIGHT, getColor(value));
        setValue(value);
        this.getChildren().addAll(rectangle, text);

    }

    public void setValue(int value) {
        String textString = "";
        if (value > 0) {
            textString += value;
        }
        text.setText(textString);
        setFontSize(value);
        rectangle.setFill(getColor(value));
    }

    public Color getColor(int value) {
        switch (value) {
            case 0:
                return Color.DARKGREY;
            case 2:
                return Color.LIGHTBLUE;
            case 4:
                return Color.BLUE;
            case 8:
                return Color.NAVY;
            case 16:
                return Color.SALMON;
            case 32:
                return Color.ORANGE;
            case 64:
                return Color.RED;
            case 128:
                return Color.LIGHTGREEN;
            case 256:
                return Color.GREEN;
            case 512:
                return Color.DARKGREEN;
            case 1024:
                return Color.LIGHTYELLOW;
            case 2048:
                return Color.YELLOW;
            case 4096:
                return Color.BROWN;
            case 8192:
                return Color.PURPLE;
            case 16384:
                return Color.PINK;
            case 32768:
                return Color.CYAN;
            case 65536:
                return Color.ALICEBLUE;
            default:
                return Color.BLACK;
        }
    }

    public void setFontSize(int value) {
        if (value > 8192) {
            text.setFont(Font.font(24));
        } else {
            text.setFont(Font.font(36));
        }
    }
}

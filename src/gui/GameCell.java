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
                return Color.LIGHTGREY;
            case 2:
                return Color.KHAKI;
            case 4:
                return Color.GOLD;
            case 8:
                return Color.GOLDENROD;
            case 16:
                return Color.ORANGE;
            case 32:
                return Color.DARKORANGE;
            case 64:
                return Color.RED;
            case 128:
                return Color.LIGHTSKYBLUE;
            case 256:
                return Color.DEEPSKYBLUE;
            case 512:
                return Color.BLUE;
            case 1024:
                return Color.DARKBLUE;
            case 2048:
                return Color.MEDIUMORCHID;
            case 4096:
                return Color.DARKORCHID;
            case 8192:
                return Color.INDIGO;
            case 16384:
                return Color.DARKGREY;
            case 32768:
                return Color.GREY;
            case 65536:
                return Color.BLACK;
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

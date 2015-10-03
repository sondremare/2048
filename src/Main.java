import gui.GameCell;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application{

    private ArrayList<GameCell> referenceList = new ArrayList<>();
    private GridPane gridPane;
    private Board board;

    @Override
    public void start(Stage primaryStage) throws Exception {
        gridPane = new GridPane();
        gridPane.setPrefSize(600, 600);
        board = new Board();
        board.addRandomCell(2);
        board.addRandomCell(4);
        board.addRandomCell(16);
        board.addRandomCell(2);
        board.addRandomCell(3);
        board.addRandomCell(4);

        updateGUI();

        Scene scene = new Scene(gridPane);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case LEFT:
                        System.out.println("LEFT");
                        board.move(Direction.LEFT);
                        break;
                    case UP:
                        System.out.println("UP");
                        board.move(Direction.UP);
                        break;
                    case RIGHT:
                        System.out.println("RIGHT");
                        board.move(Direction.RIGHT);
                        break;
                    case DOWN:
                        System.out.println("DOWN");
                        board.move(Direction.DOWN);
                        break;
                    default:
                }
                updateGUI();
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public void updateGUI() {
        if (referenceList.size() != 0) {
            for (GameCell gameCell : referenceList) {
                gridPane.getChildren().remove(gameCell);
            }
        }
        Cell[][] cells = board.getCells();
        for (int i = 0; i < cells.length; i++) {
            for (int j = cells[i].length-1; j >= 0; j--) {
                Cell cell = cells[i][j];
                if (cell.getValue() != Board.EMPTY) {
                }
                GameCell gameCell = new GameCell(cell.getValue());
                referenceList.add(gameCell);
                gridPane.add(gameCell, i, j);


            }
        }
        System.out.println(board);

    }

    public static void main(String[] args) {
        launch(args);
    }
}

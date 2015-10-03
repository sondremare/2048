import gui.GameCell;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application{

    private ArrayList<GameCell> referenceList = new ArrayList<>();
    private GridPane gamePane;
    private Board board;

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane mainPane = new GridPane();
        mainPane.setPrefSize(700,600);
        GridPane controlPane = new GridPane();
        controlPane.setPrefSize(100, 600);

        Button button = new Button("Add tile");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (board != null) {
                    board.addRandomCell();
                    updateGUI();
                }
            }
        });
        controlPane.add(button, 0, 0);

        gamePane = new GridPane();
        gamePane.setPrefSize(600, 600);
        board = new Board();

        mainPane.add(controlPane, 0, 0);
        mainPane.add(gamePane, 1, 0);

        updateGUI();

        Scene scene = new Scene(mainPane);
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
                gamePane.getChildren().remove(gameCell);
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
                gamePane.add(gameCell, i, j);


            }
        }
        System.out.println(board);

    }

    public static void main(String[] args) {
        launch(args);
    }
}

package game;

import gui.GameCell;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import search.Heuristic;
import search.alphabeta.AlphaBeta;
import search.expectimax.Expectimax;

import java.util.ArrayList;

public class Main extends Application{

    private static ArrayList<GameCell> referenceList = new ArrayList<>();
    private static GridPane gamePane;
    private static Game game;
    private static boolean gameOver = false;
    private static double PLAY_SPEED = 50;
    private boolean isPlaying = false;
    private static Timeline loop;

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane mainPane = new GridPane();
        mainPane.setPrefSize(700, 600);
        GridPane controlPane = new GridPane();
        controlPane.setPrefSize(100, 600);

        /*Button button = new Button("Add tile");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (board != null) {
                    board.addRandomCell();
                    updateGUI();
                }
            }
        });
        controlPane.add(button, 0, 0);*/

        Button playStepButton = new Button("Step");
        playStepButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (game != null) {
                    if (!gameOver) {
                        playStep();
                    }

                }
            }
        });

        Button playButton = new Button("Play");
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (game != null) {
                    if (isPlaying) {
                        loop.stop();
                        isPlaying = false;
                        playButton.setText("Play");
                    } else {
                        playButton.setText("Pause");
                        loop = new Timeline(new KeyFrame(Duration.millis(PLAY_SPEED), new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                if (!gameOver) {
                                    playStep();
                                }
                            }
                        }));
                        loop.setCycleCount(Timeline.INDEFINITE);
                        loop.play();
                        isPlaying = true;
                    }
                }
            }
        });

        controlPane.add(playStepButton, 0, 0);
        controlPane.add(playButton, 0, 1);

        gamePane = new GridPane();
        gamePane.setPrefSize(600, 600);
        Heuristic heuristic = new Heuristic();
        //game = new Game(new AlphaBeta(heuristic));
        game = new Game(new Expectimax(heuristic));

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
                        game.getBoard().move(Direction.LEFT);
                        break;
                    case UP:
                        System.out.println("UP");
                        game.getBoard().move(Direction.UP);
                        break;
                    case RIGHT:
                        System.out.println("RIGHT");
                        game.getBoard().move(Direction.RIGHT);
                        break;
                    case DOWN:
                        System.out.println("DOWN");
                        game.getBoard().move(Direction.DOWN);
                        break;
                    default:
                }
                updateGUI();
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void playStep() {
        if (game.playStep()) {
            updateGUI();
        } else {
            showMessage("Game over");
            gameOver = true;
        }
    }


    public static void updateGUI() {
        if (referenceList.size() != 0) {
            for (GameCell gameCell : referenceList) {
                gamePane.getChildren().remove(gameCell);
            }
        }
        Cell[][] cells = game.getBoard().getCells();
        for (int i = 0; i < cells.length; i++) {
            for (int j = cells[i].length-1; j >= 0; j--) {
                Cell cell = cells[i][j];
                GameCell gameCell = new GameCell(cell.getValue());
                referenceList.add(gameCell);
                gamePane.add(gameCell, i, j);
            }
        }
        //System.out.println(game.getBoard());

    }

    public static void showMessage(String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("2048");
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

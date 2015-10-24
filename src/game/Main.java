package game;

import gui.GameCell;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import search.AdversarialSearch;
import search.Heuristic;
import search.alphabeta.AlphaBeta;
import search.expectimax.Expectimax;

import java.util.ArrayList;

public class Main extends Application{

    private static ArrayList<GameCell> referenceList = new ArrayList<>();
    private static GridPane gamePane;
    private static Game game;
    private static AdversarialSearch search;
    private static boolean gameOver = false;
    private static double PLAY_SPEED = 500;
    private boolean isPlaying = false;
    private static Timeline loop;
    private static RadioButton alphaBetaRadioButton;
    private static RadioButton expectiMaxRadiuButton;
    private static TextField maxDepthInput;
    private static TextField sleepTimeInput;

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane mainPane = new GridPane();
        mainPane.setPrefSize(800, 600);
        GridPane controlPane = new GridPane();
        controlPane.setPrefSize(200, 600);

        ToggleGroup searchType = new ToggleGroup();
        alphaBetaRadioButton = new RadioButton("Alpha-Beta pruning");
        expectiMaxRadiuButton = new RadioButton("ExpectiMax");
        alphaBetaRadioButton.setToggleGroup(searchType);
        expectiMaxRadiuButton.setToggleGroup(searchType);
        expectiMaxRadiuButton.setSelected(true);
        searchType.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                initGame();
            }
        });

        Label maxDepthLabel = new Label("Max depth: ");
        maxDepthInput = new TextField("5");
        maxDepthInput.setMaxWidth(50);
        maxDepthInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                initGame();
            }
        });

        Label sleepTimeLabel = new Label("Sleep (ms)");
        sleepTimeInput = new TextField(String.valueOf(PLAY_SPEED));
        sleepTimeInput.setMaxWidth(50);
        sleepTimeInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                PLAY_SPEED = Double.parseDouble(newValue);
            }
        });

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

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isPlaying) {
                    loop.stop();
                    isPlaying = false;
                    playButton.setText("Play");
                }
                gameOver = false;
                initGame();
            }
        });

        controlPane.add(alphaBetaRadioButton, 0, 0);
        controlPane.add(expectiMaxRadiuButton, 0, 1);
        controlPane.add(maxDepthLabel, 0, 2);
        controlPane.add(maxDepthInput, 1, 2);
        controlPane.add(sleepTimeLabel, 0, 3);
        controlPane.add(sleepTimeInput, 1, 3);
        controlPane.add(playStepButton, 0, 4);
        controlPane.add(playButton, 0, 5);
        controlPane.add(resetButton, 0, 6);

        gamePane = new GridPane();
        gamePane.setPrefSize(600, 600);
        mainPane.add(controlPane, 0, 0);
        mainPane.add(gamePane, 1, 0);

        initGame();

        Scene scene = new Scene(mainPane);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case LEFT:
                        System.out.println("LEFT");
                        game.getBoard().move(Direction.LEFT, true);
                        break;
                    case UP:
                        System.out.println("UP");
                        game.getBoard().move(Direction.UP, true);
                        break;
                    case RIGHT:
                        System.out.println("RIGHT");
                        game.getBoard().move(Direction.RIGHT, true);
                        break;
                    case DOWN:
                        System.out.println("DOWN");
                        game.getBoard().move(Direction.DOWN, true);
                        break;
                    default:
                }
                updateGUI();
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void initGame() {
        Heuristic heuristic = new Heuristic();
        if (alphaBetaRadioButton.isSelected()) {
            search = new AlphaBeta(heuristic);
        } else if (expectiMaxRadiuButton.isSelected()) {
            search = new Expectimax(heuristic);
        }
        int maxDepth = Integer.parseInt(maxDepthInput.getText());
        game = new Game(search, maxDepth);
        updateGUI();
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

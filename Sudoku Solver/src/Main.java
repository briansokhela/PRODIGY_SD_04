import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int SIZE = 9;
    private TextField[][] grid = new TextField[SIZE][SIZE];

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sudoku Solver");

        GridPane gridPane = createGridPane();

        Button solveButton = new Button("Solve");
        solveButton.setOnAction(e -> solveSudoku());

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(e -> resetGrid());

        VBox vbox = new VBox(10, gridPane, solveButton, resetButton);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        // Create column labels
        for (int col = 0; col < SIZE; col++) {
            Label label = new Label(String.valueOf(col + 1));
            gridPane.add(label, col + 1, 0);
        }

        // Create row labels
        for (int row = 0; row < SIZE; row++) {
            Label label = new Label(String.valueOf(row + 1));
            gridPane.add(label, 0, row + 1);
        }

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                TextField textField = new TextField();
                textField.setPrefWidth(40);
                textField.setPrefHeight(40);
                textField.setStyle("-fx-font-size: 18; -fx-alignment: center;");

                // Apply thicker borders to differentiate 3x3 sub-grids
                if (row % 3 == 0) {
                    textField.setStyle(textField.getStyle() + "-fx-border-width: 2px 0 0 0; -fx-border-color: black;");
                } 
                if (col % 3 == 0) {
                    textField.setStyle(textField.getStyle() + "-fx-border-width: 0 0 0 2px; -fx-border-color: black;");
                }
                
                if (row % 3 == 0 && col % 3 == 0) {
                   textField.setStyle(textField.getStyle() + "-fx-border-width: 2px 0 0 2px; -fx-border-color: black;");
                }

                grid[row][col] = textField;
                gridPane.add(textField, col + 1, row + 1);
            }
        }

        return gridPane;
    }

    private void solveSudoku() {
        int[][] board = new int[SIZE][SIZE];

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String text = grid[row][col].getText();
                board[row][col] = text.isEmpty() ? 0 : Integer.parseInt(text);
            }
        }

        if (solve(board)) {
            displaySolution(board);
        } else {
            System.out.println("No solution exists");
        }
    }

    private boolean solve(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (solve(board)) {
                                return true;
                            }
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num ||
                board[row - row % 3 + i / 3][col - col % 3 + i % 3] == num) {
                return false;
            }
        }
        return true;
    }

    private void displaySolution(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                grid[row][col].setText(String.valueOf(board[row][col]));
            }
        }
    }

    private void resetGrid() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                grid[row][col].clear();
            }
        }
    }
}

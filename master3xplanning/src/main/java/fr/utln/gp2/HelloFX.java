package fr.utln.gp2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloFX extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Hello, JavaFX with Maven!");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 400, 300);
        
        primaryStage.setTitle("JavaFX + Maven");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
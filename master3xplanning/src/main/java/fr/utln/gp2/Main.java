package fr.utln.gp2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Charger une image depuis un fichier local
        String imagePath = "src/main/resources/G.jpg";  // Remplace par le chemin réel
        Image image = new Image("file:" + imagePath);  // Assure-toi que le chemin est correct

        // Créer un ImageView pour afficher l'image
        ImageView imageView = new ImageView(image);

        // Redimensionner l'image si nécessaire
        imageView.setFitWidth(300);  // Largeur souhaitée
        imageView.setFitHeight(200); // Hauteur souhaitée

        // Création d'un bouton
        Button button = new Button("Galu !");
        button.setOnAction(e -> System.out.println("sinsky !"));

        // Ajout du bouton dans une StackPane (layout)
        // StackPane root = new StackPane(button);
        HBox root = new HBox(10);
        root.getChildren().addAll(imageView, button);
        Scene scene = new Scene(root, 400, 300);

        // Configuration de la fenêtre
        primaryStage.setTitle("JavaFX App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

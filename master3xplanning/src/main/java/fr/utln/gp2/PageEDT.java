package java.fr.utln.gp2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.GridPane;
import javafx.scene.text.FontWeight;

public class PageEDT extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // Création d'une grille d'emploi du temps
        GridPane grid = new GridPane();
        grid.setVgap(10); // Espacement vertical
        grid.setHgap(10); // Espacement horizontal

        // Jours de la semaine
        String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"};
        
        // Horaires de l'emploi du temps
        String[] horaires = {"8h-10h", "10h-12h", "12h-14h", "14h-16h", "16h-18h"};

        // Ajouter les jours en haut (en-tête des colonnes)
        for (int i = 0; i < jours.length; i++) {
            Label jourLabel = new Label(jours[i]);
            jourLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            jourLabel.setAlignment(Pos.CENTER);
            grid.add(jourLabel, i + 1, 0); // Ajout dans la première ligne, colonne i+1
        }

        // Ajouter les horaires sur la première colonne
        for (int i = 0; i < horaires.length; i++) {
            Label horaireLabel = new Label(horaires[i]);
            horaireLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            horaireLabel.setAlignment(Pos.CENTER);
            grid.add(horaireLabel, 0, i + 1); // Ajout dans la première colonne, ligne i+1
        }

        // Ajouter des créneaux vides dans le reste de la grille
        for (int i = 0; i < horaires.length; i++) {
            for (int j = 0; j < jours.length; j++) {
                Label cellule = new Label("Libre");
                cellule.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-alignment: center;");
                grid.add(cellule, j + 1, i + 1); // Remplir la grille à partir de la cellule (1,1)
            }
        }

        // Création de la scène et ajout du GridPane
        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setTitle("Emploi du Temps");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
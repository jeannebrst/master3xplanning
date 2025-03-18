package fr.utln.gp2;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.geometry.HPos;
import javafx.geometry.VPos;

public class PageEDT extends Application {


    @Override
    public void start(Stage primaryStage) {

		String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"};
		String[] horaires = {"8h","9h","10h","11h","12h","13h","14h","15h","16h","17h","18h"};
        // Création d'une grille 6x6
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(true);  // Affiche les lignes de la grille
        
        // Définir les contraintes de ligne et de colonne pour avoir la même taille
        for (int i = 0; i < 12; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100 / 10);  // 100% divisé par 6 pour avoir une taille égale
            grid.getRowConstraints().add(row);


        }
		for (int i =0; i <6; i++){
			ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100 / 6.0);  // 100% divisé par 6 pour avoir une taille égale
            grid.getColumnConstraints().add(column);
		}

        // Ajouter des labels dans chaque cellule de la grille
        for (int i = 0; i < 5; i++) {
			Label jour = new Label(jours[i]);
			
			//Label horaire = new Label(horaires[i]);
			
			GridPane.setHalignment(jour, HPos.CENTER);
			GridPane.setValignment(jour, VPos.CENTER);
			//GridPane.setHalignment(horaire, HPos.CENTER);  
			//GridPane.setValignment(horaire, VPos.CENTER);
            grid.add(jour, i+1, 0);  // Ajoute la cellule à la position (i, j)
			//grid.add(horaire, 0, i+1);
		}
		for (int i=0; i<horaires.length;i++){
			Label horaire = new Label(horaires[i]);

			GridPane.setHalignment(horaire, HPos.CENTER);  
			GridPane.setValignment(horaire, VPos.CENTER);
			grid.add(horaire, 0, i+1);
		}
        

        // Création de la scène
        Scene scene = new Scene(grid, 600, 600);  
        primaryStage.setTitle("Page d'acceuil");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
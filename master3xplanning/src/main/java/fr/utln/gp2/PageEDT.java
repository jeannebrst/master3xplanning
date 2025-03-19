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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;


public class PageEDT extends Application {

    public GridPane grid = new GridPane();

    @Override
    public void start(Stage primaryStage) {

		String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"};
		String[] horaires = {"8h","9h","10h","11h","12h","13h","14h","15h","16h","17h","18h"};
        
        
        grid.setGridLinesVisible(true);  // Affiche les lignes de la grille
        
        // Définir les contraintes de ligne et de colonne pour avoir la même taille
        for (int i = 0; i < 12; i++) {
            if (i ==0){

            }
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100 / 10);  
            grid.getRowConstraints().add(row);


        }
		for (int i =0; i <6; i++){
			ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100 / 6.0);  
            grid.getColumnConstraints().add(column);
		}

        // Ajouter des labels dans chaque cellule de la grille
        for (int i = 0; i < 5; i++) {
			Label jour = new Label(jours[i]);
			
			GridPane.setHalignment(jour, HPos.CENTER);
			GridPane.setValignment(jour, VPos.CENTER);

            grid.add(jour, i+1, 0);  
			
		}

		for (int i=0; i<horaires.length;i++){
			Label horaire = new Label(horaires[i]);

			GridPane.setHalignment(horaire, HPos.CENTER);  
			GridPane.setValignment(horaire, VPos.CENTER);
			grid.add(horaire, 0, i+1);
		}
        

        ajouterCours("Maths","112","T.Champion",1,1,3,Color.PINK);
        ajouterCours("Maths","112","T.Champion",2,1,3,Color.PINK);
        ajouterCours("Maths","112","T.Champion",3,4,3,Color.PINK);
        ajouterCours("Maths","112","T.Champion",4,2,3,Color.BLUE);

        
        // Création de la scène
        Scene scene = new Scene(grid, 600, 600);  
        primaryStage.setTitle("Page d'accueil");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void ajouterCours(String name,String Salle,String Prof, int jour, int heure, int duree, Color couleur){

        Label nom = new Label(name+"\n"+Prof+"\n"+Salle);
        nom.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nom.setTextFill(Color.WHITE);
        

        // Crée un StackPane pour centrer le label dans la cellule
        StackPane cell = new StackPane();
        cell.getChildren().add(nom);
        cell.setAlignment(Pos.CENTER);  
        cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);  
        cell.setBackground(new Background(new BackgroundFill(couleur, new CornerRadii(0), new Insets(1))));
        // Ajouter la cellule dans la grille
        grid.add(cell, jour, heure);
        GridPane.setRowSpan(cell, duree);
        GridPane.setColumnSpan(cell, 1);  

        // Centrer le StackPane dans la cellule de la grille
        GridPane.setHalignment(cell, HPos.CENTER);  
        GridPane.setValignment(cell, VPos.CENTER);  
    }
}
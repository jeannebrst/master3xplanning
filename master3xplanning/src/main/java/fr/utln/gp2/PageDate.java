package fr.utln.gp2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;

import java.util.Map;
import java.util.HashMap;
import java.lang.Integer;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.time.temporal.IsoFields;
import java.util.Locale;



public class PageDate extends Application {

	GridPane grilleEdt = new GridPane();
	LocalDate lundi;
	Label semaine;
	int numSemaine;
	private Map<Integer, StackPane> coursMap = new HashMap<>();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage){
		lundi = LocalDate.now().with(DayOfWeek.MONDAY);
		semaine = new Label("");
		modifLabelSemaine();

		genereEDT();

		ajouterCours("Maths","112","T.Champion",1,1,3,Color.PINK,1);
		ajouterCours("Maths","112","T.Champion",2,1,3,Color.PINK,2);
		ajouterCours("Maths","112","T.Champion",3,4,3,Color.PINK,3);
		ajouterCours("Maths","112","T.Champion",4,2,3,Color.BLUE,4);
		supprimerCours(2);

		Button btnPreviousWeek = new Button("<");
		Button btnNextWeek = new Button(">");

		btnPreviousWeek.setOnAction(e -> {
			lundi = lundi.minusWeeks(1);
			majEDT();
		});

		btnNextWeek.setOnAction(e -> {
			lundi = lundi.plusWeeks(1);
			majEDT();
		});

		// Créer un layout pour les boutons et la grille
		GridPane root = new GridPane();
		root.add(btnPreviousWeek, 0, 1);
		root.add(semaine,1,1);
		root.add(btnNextWeek, 2, 1);
		root.add(grilleEdt, 0, 2, 3, 1);
		
		// Création de la scène
		Scene scene = new Scene(root, 1200, 800);
		primaryStage.setTitle("Page d'accueil");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void genereEDT(){
		String[] horaires = {"8h","9h","10h","11h","12h","13h","14h","15h","16h","17h","18h"};

		grilleEdt.setGridLinesVisible(true);  // Affiche les lignes de la grille
		
		// Définir les contraintes de ligne et de colonne pour avoir la même taille
		for (int i = 0; i < 12; i++) {
			if (i ==0){

			}
			RowConstraints row = new RowConstraints();
			row.setPercentHeight(100 / 10f);  
			grilleEdt.getRowConstraints().add(row);
		}

		for (int i =0; i <6; i++){
			ColumnConstraints column = new ColumnConstraints();
			column.setPercentWidth(100 / 6f);  
			grilleEdt.getColumnConstraints().add(column);
		}

		// Ajouter des labels dans chaque cellule de la grille
		for (int i = 0; i < 5; i++) {
			LocalDate jourDate = lundi.plusDays(i);
			String jourNom = jourDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE);
			String moisNom = lundi.getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH);
			Label jourLabel = new Label(jourNom + " " + jourDate.getDayOfMonth() + " " + moisNom);
			
			GridPane.setHalignment(jourLabel, HPos.CENTER);
			GridPane.setValignment(jourLabel, VPos.CENTER);

			grilleEdt.add(jourLabel, i+1, 0);
		}

		for (int i=0; i<horaires.length;i++){
			Label horaire = new Label(horaires[i]);

			GridPane.setHalignment(horaire, HPos.CENTER);  
			GridPane.setValignment(horaire, VPos.CENTER);
			grilleEdt.add(horaire, 0, i+1);
		}
	}

	private void majEDT(){
		modifLabelSemaine();

		for(int i=0; i<5; i++){
			LocalDate jourDate = lundi.plusDays(i);
			String jourNom = jourDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE);
			String moisNom = lundi.getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH);
			
			((Label) grilleEdt.getChildren().get(i+1)).setText(jourNom + " " + jourDate.getDayOfMonth() + " " + moisNom);
		}
	}

	public void ajouterCours(String name,String Salle,String Prof, int jour, int heure, int duree, Color couleur,int id){

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
		grilleEdt.add(cell, jour, heure);
		GridPane.setRowSpan(cell, duree);
		GridPane.setColumnSpan(cell, 1);  

		// Centrer le StackPane dans la cellule de la grille
		GridPane.setHalignment(cell, HPos.CENTER);  
		GridPane.setValignment(cell, VPos.CENTER);  
		coursMap.put(id,cell);
	}

	private void modifLabelSemaine(){
		numSemaine = lundi.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
		semaine.setText("Semaine n°" + numSemaine);
	}
	public void supprimerCours(Integer id) {
		StackPane cours = coursMap.get(id);  
		if (cours != null) {
			grilleEdt.getChildren().remove(cours);  
			coursMap.remove(id); 
		}
}
}
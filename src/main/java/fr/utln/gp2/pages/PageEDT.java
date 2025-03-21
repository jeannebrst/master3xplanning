package fr.utln.gp2.pages;

import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;	
import java.net.URI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.lang.Integer;
import java.net.URI;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.time.temporal.IsoFields;


public class PageEDT extends Application {

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

		VBox pageComplete = new VBox(10);

		HBox boiteBtn = new HBox(10);

		genereEDT();

		ajouterCours("Maths","112","T.Champion",1,1,3,Color.PINK,1);
		ajouterCours("Maths","112","T.Champion",2,1,3,Color.PINK,2);
		ajouterCours("Maths","112","T.Champion",3,4,3,Color.PINK,3);
		ajouterCours("Maths","112","T.Champion",4,2,3,Color.BLUE,4);
		supprimerCours(2);

		Button btnPreviousWeek = new Button("<");
		Button btnNextWeek = new Button(">");
		Button cours = new Button("Cours");
		Button infos = new Button("Informations Personelles");

		btnPreviousWeek.setOnAction(e -> {
			lundi = lundi.minusWeeks(1);
			majEDT();
		});

		btnNextWeek.setOnAction(e -> {
			lundi = lundi.plusWeeks(1);
			majEDT();
		});

		infos.setOnAction(e -> {
			String id ="58bb7cb0-834d-ee1a-353e-6ed3609ba018";
			getPersonneInfo(id);

		});
		
		boiteBtn.getChildren().addAll(cours,infos);

		StackPane cellule_bouton = new StackPane();
		cellule_bouton.setAlignment(Pos.CENTER);  
		cellule_bouton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);  
		
		HBox boite = new HBox(10);
		boite.setAlignment(Pos.CENTER);  // Aligner le contenu de la HBox au centre
		boite.getChildren().addAll(btnPreviousWeek, semaine, btnNextWeek);
		cellule_bouton.getChildren().add(boite);
		
		// Ajouter la cellule dans la grille
		grilleEdt.add(cellule_bouton, 0, 0);
		
		// Centrer la cellule dans la grille
		GridPane.setHalignment(cellule_bouton, HPos.CENTER);
		GridPane.setValignment(cellule_bouton, VPos.CENTER);

		pageComplete.getChildren().addAll(boiteBtn,grilleEdt);
		VBox.setVgrow(grilleEdt, Priority.ALWAYS);

		// Créer un layout pour les boutons et la grille
		StackPane root = new StackPane();
		root.getChildren().add(pageComplete);
		
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

	public void getPersonneInfo(String id) {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("http://localhost:8080/personnes/" + id))
			.GET()
			.header("Content-Type", "application/json")
			.build();

		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			.thenApply(HttpResponse::body)
			.thenAccept(response -> {
				System.out.println("Réponse : " + response);
				afficherPersonne(response);
			})
			.exceptionally(e -> {
				e.printStackTrace();
				return null;
			});
}
	public void afficherPersonne(String jsonResponse) {
    try {
        // Convertir la réponse JSON en objet JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        // Extraire les champs nécessaires
        String nom = jsonNode.get("nom").asText();
        String prenom = jsonNode.get("prenom").asText();
        String mail = jsonNode.get("mail").asText();
        String role = jsonNode.get("role").asText();

        System.out.println(nom+"  "+prenom +"  "+ mail +"  "+role);

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
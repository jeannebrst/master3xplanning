package fr.utln.gp2.pages;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.utln.gp2.entites.Note;
import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Promotion;
import fr.utln.gp2.entites.Personne.Role;
import fr.utln.gp2.utils.Outils;
import fr.utln.gp2.utils.PromotionId;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class PageNotes{
	private Stage stage;

	private List<Promotion> promos;
	private Map<String, String> nomToLogin;
	private List<Note> notesEleve = new ArrayList<>();

	private Promotion promoChoisie;
	private Personne eleveChoisi;
	private SimpleDateFormat format;

	public PageNotes(List<Promotion> promos){
		stage = new Stage();
		this.promos = promos;
	}

	public void show(){
		stage.setTitle("Notes");
		stage.setMaximized(false);
		stage.setMinWidth(1920/2.);
		stage.setMinHeight(1080/2.);

		stage.setScene(generePage());
		stage.show();
	}

	private Scene generePage(){
		HBox boitePage = new HBox(50);
		VBox boiteSelection = new VBox();

		format = new SimpleDateFormat("dd/MM");

		ComboBox<String> promoComboBox = new ComboBox<>();
		for (Promotion promo : promos){
			promoComboBox.getItems().add(promo.getPromoId().toString());
		}
		promoComboBox.setValue("Promotions");

		ComboBox<String> eleveComboBox = new ComboBox<>();
		eleveComboBox.setDisable(true); // Désactivé au début
		eleveComboBox.setValue("Elève");

		boiteSelection.getChildren().addAll(promoComboBox, eleveComboBox);

		VBox boiteNotes = new VBox();
		
		ScrollPane scrollNotes = new ScrollPane(boiteNotes); //Ca marche ap !!
		scrollNotes.setFitToWidth(true);
		scrollNotes.setPrefHeight(500);
		scrollNotes.setMaxHeight(500);
		scrollNotes.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

		boitePage.getChildren().addAll(boiteSelection, boiteNotes);

		promoComboBox.setOnAction(event -> {
			int indicePromoChoisie = promoComboBox.getSelectionModel().getSelectedIndex();
			PromotionId promoIdChoisie = promos.get(indicePromoChoisie).getPromoId();

			if (promoIdChoisie != null){
				promoChoisie = Outils.getPromoById(promoIdChoisie).join();
				
				nomToLogin = promoChoisie.getPersonnes().stream()
					.filter(personne -> personne.getRole() == Role.ETUDIANT)
					.collect(Collectors.toMap(
						personne -> personne.getPrenom() + " " + personne.getNom(),
						Personne::getLogin
					));

				List<String> noms = nomToLogin.keySet().stream().collect(Collectors.toList());
				
				eleveComboBox.getItems().setAll(noms);
				eleveComboBox.setDisable(false);
			}
		});

		eleveComboBox.setOnAction(event -> {
			String eleveChoisi = eleveComboBox.getValue();
			String loginEleve = nomToLogin.get(eleveChoisi);
			
			Optional<Personne> personneOpt = promoChoisie.getPersonnes().stream()
				.filter(p -> p.getLogin().equals(loginEleve))
				.findFirst();

			personneOpt.ifPresent(eleve -> {
				notesEleve = eleve.getNotes();

				boiteNotes.getChildren().clear();
				if (notesEleve != null && !notesEleve.isEmpty()){
					Button boutonSuppr = new Button("Ajouter note");
					boutonSuppr.setOnAction(e -> {
						//Faire une page ajout note
					});
					boiteNotes.getChildren().addAll(boutonSuppr);
				}
				notesEleve.forEach(n -> {
					afficheNotes(n, boiteNotes);
				});

			});
		});

		return new Scene(boitePage);
	}

	public Stage getStage() {
		return stage;
	}

	private void afficheNotes(Note n, VBox boiteNotes){
		HBox noteHBox = new HBox(30); // Contient tout
		VBox infoNote = new VBox(5); // Nom de l'UE + Date
		VBox valeurNote = new VBox();
		VBox boutons = new VBox(); //Modif et suppr
		Region spacer = new Region();
	
		Label ueLabel = new Label(n.getUe().getNom());
		ueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		ueLabel.setTextFill(Color.BLACK);
	
		String dateFormatee = format.format(n.getDate());
		Label dateLabel = new Label(dateFormatee);
		dateLabel.setFont(Font.font("Arial", 15));

		infoNote.setAlignment(Pos.CENTER_LEFT);
		infoNote.getChildren().addAll(ueLabel, dateLabel);
	
		Float valeur = n.getNote();
		Label noteLabel = new Label((valeur % 1 == 0) ? String.format("%.0f", valeur) : Float.toString(valeur));
		noteLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		noteLabel.setTextFill(Color.WHITE);
	
		StackPane cell = new StackPane();
		cell.setMinHeight(50);
		cell.setMinWidth(50);
		cell.setAlignment(Pos.CENTER); 
	
	
		Color couleurFond;
		if (valeur >= 15) {
			couleurFond = Color.GREEN;
		} else if (valeur >= 10) {
			couleurFond = Color.LIGHTGREEN;
		} else if (valeur >= 5) {
			couleurFond = Color.ORANGE;
		} else {
			couleurFond = Color.RED;
		}
	
		cell.setBackground(new Background(new BackgroundFill(couleurFond, new CornerRadii(10), new Insets(1))));
		cell.getChildren().add(noteLabel);

		valeurNote.setAlignment(Pos.CENTER_RIGHT);
		valeurNote.getChildren().add(cell);

		HBox.setHgrow(spacer, Priority.ALWAYS);

		Button boutonModif = new Button("Modif");
		boutonModif.setOnAction(e -> {
			//Page modif
		});
		Button boutonSuppr = new Button("Suppr");
		boutonSuppr.setOnAction(e -> {
			deleteNote(n);
		});
	
		noteHBox.getChildren().addAll(infoNote, spacer, valeurNote,boutonSuppr);
		noteHBox.setPadding(new Insets(5));
		boiteNotes.getChildren().add(noteHBox);
	}

	private void deleteNote(Note n){
		//A remplir
	}
}
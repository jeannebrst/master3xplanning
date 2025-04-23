package fr.utln.gp2.pages;

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
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PageNotes{
	private Stage stage;

	private List<Promotion> promos;
	private Map<String, String> nomToLogin;
	private List<Note> notesEleve = new ArrayList<>();

	private Promotion promoChoisie;
	private Personne eleveChoisi;

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
		ComboBox<String> promoComboBox = new ComboBox<>();
		for (Promotion promo : promos){
			promoComboBox.getItems().add(promo.getPromoId().toString());
		}
		promoComboBox.setValue("Promotions");

		ComboBox<String> eleveComboBox = new ComboBox<>();
		eleveComboBox.setDisable(true); // Désactivé au début
		eleveComboBox.setValue("Elève");

		promoComboBox.setOnAction(event -> {
			int indicePromoChoisie = promoComboBox.getSelectionModel().getSelectedIndex();
			PromotionId promoIdChoisie = promos.get(indicePromoChoisie).getPromoId();

			if (promoIdChoisie != null){
				promoChoisie = Outils.getPromoById(promoIdChoisie).join();
				System.out.println(promoChoisie);
				
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
				//A afficher comme tu veux ou tu veux !!
				System.out.println(notesEleve);
			});
		});

		
		VBox layout = new VBox(10, promoComboBox, eleveComboBox);
		return new Scene(layout);
	}

	public Stage getStage() {
		return stage;
	}
}
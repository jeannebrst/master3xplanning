package fr.utln.gp2.pages;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Promotion;
import fr.utln.gp2.entites.Personne.Role;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PageNotes{
	private Stage stage;

	private List<Promotion> promos;
	private Map<String, List<Personne>> etudiantsPromos;

	private Promotion promoChoisie;
	private Personne eleveChoisi;

	public PageNotes(List<Promotion> promos){
		stage = new Stage();
		this.promos = promos;

		etudiantsPromos = this.promos.stream()
			.collect(Collectors.toMap(
				promo -> promo.getPromoId().toString(),
				promo -> promo.getPersonnes().stream()
							.filter(p -> p.getRole().equals(Role.ETUDIANT))
							.collect(Collectors.toList())
			));

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
			promoChoisie = promos.get(indicePromoChoisie);
			if (promoChoisie != null){
				List<String> logins = etudiantsPromos.get(promoChoisie.getPromoId().toString())
					.stream()
					.map(Personne::getLogin)
					.collect(Collectors.toList());
				eleveComboBox.getItems().setAll(logins);
				eleveComboBox.setDisable(false);
			}
		});

		eleveComboBox.setOnAction(event -> {
			int indiceEleveChoisi = eleveComboBox.getSelectionModel().getSelectedIndex();
			System.out.println(indiceEleveChoisi);
			eleveChoisi = etudiantsPromos.get(promoChoisie.getPromoId().toString()).get(indiceEleveChoisi);
		
			System.out.println(eleveChoisi.getNotes());
		});

		
		VBox layout = new VBox(10, promoComboBox, eleveComboBox);
		return new Scene(layout);
	}

	public Stage getStage() {
		return stage;
	}
}
package fr.utln.gp2.pages;

import java.util.List;

import fr.utln.gp2.entites.Cours;
import fr.utln.gp2.entites.Cours.TypeC;
import fr.utln.gp2.entites.UE;
import fr.utln.gp2.utils.Outils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PageModifCours {

    private Stage stage = new Stage();
    private List<UE> ues;
	private Cours c;

    public PageModifCours(Cours c){
		this.c = c;
    }

    public void show(){
        stage.setTitle("Page de modification");
		stage.setMaximized(false);

		ues = Outils.getAllUE().join();
		stage.setScene(generePage());
		stage.show();
    }

    private Scene generePage(){
		ComboBox<String> ueComboBox = new ComboBox<>();
		for (UE ue : ues){
			ueComboBox.getItems().add(ue.getNom());
		}
		ueComboBox.setValue(c.getUe().getNom());

		ComboBox<String> profComboBox = new ComboBox<>();

		// Initialiser les profs en fonction de l’UE actuelle
		UE ueInitiale = ues.stream()
			.filter(ue -> ue.getNom().equals(c.getUe().getNom()))
			.findFirst()
			.orElse(null);

		if (ueInitiale != null) {
			profComboBox.getItems().addAll(ueInitiale.getIntervenantsLogin());
			profComboBox.setValue(c.getIntervenantLogin());
		}

		ueComboBox.setOnAction(event -> {
			int indiceUeChoisie = ueComboBox.getSelectionModel().getSelectedIndex();
			if (indiceUeChoisie >= 0) {
				UE ueChoisie = ues.get(indiceUeChoisie);
				System.out.println(ueChoisie);
				if (ueChoisie != null && ueChoisie.getIntervenantsLogin() != null) {
					profComboBox.getItems().setAll(ueChoisie.getIntervenantsLogin());
				}
			}
		});

        ComboBox<String> typeMenuDeroulant = new ComboBox<>();
        typeMenuDeroulant.getItems().addAll(TypeC.CM.toString(),TypeC.TD.toString(),TypeC.TP.toString());
        typeMenuDeroulant.setValue(c.getType().toString());

        ComboBox<String> dureeMenuDeroulant = new ComboBox<>();
        dureeMenuDeroulant.getItems().addAll("1","2","3","4");
        dureeMenuDeroulant.setValue(String.valueOf(c.getDuree()));

		Button validerButton = new Button("Valider");
		validerButton.setOnAction(event -> {
			int indiceUeChoisie = ueComboBox.getSelectionModel().getSelectedIndex();
			UE ueChoisie = ues.get(indiceUeChoisie);
			String profChoisi = profComboBox.getValue();
            int dureeChosie = Integer.parseInt(dureeMenuDeroulant.getValue());
            TypeC typeChoisi = Cours.stringToTypeC(typeMenuDeroulant.getValue());
			if (ueChoisie != null && profChoisi != null){
				//ajouteUnCours(ueChoisie, profChoisi, dureeChosie, typeChoisi);
                //System.out.println(Outils.getCoursByPromo(promo.getPromoId()).join());
                stage.close();
			}
            
		});
		
		VBox layout = new VBox(10, ueComboBox, profComboBox,typeMenuDeroulant,dureeMenuDeroulant, validerButton);
		Pane pane = new Pane();
		pane.getChildren().add(layout);
		return new Scene(pane);
	}

	public Stage getStage(){
		return this.stage;
	}
}

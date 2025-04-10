package fr.utln.gp2.pages;

import java.util.List;

import fr.utln.gp2.entites.Cours;
import fr.utln.gp2.entites.Cours.TypeC;
import fr.utln.gp2.entites.Salle;
import fr.utln.gp2.entites.UE;
import fr.utln.gp2.utils.Outils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class PageModifCours {

    private Stage stage;
    private List<UE> ues;
	//private Long coursId;
	private List<Salle> salles;
	private Cours cours;


    public PageModifCours(Cours c){
		stage = new Stage();
		this.cours = c;
    }

    public void show(){
        stage.setTitle("Page de modification");
		stage.setMaximized(false);
		//cours = Outils.getCoursById(coursId).join();
		ues = Outils.getAllUE().join();
		salles = Outils.getAllSalle().join();
		stage.setScene(generePage());
		stage.setMinWidth(400);
		stage.setMinHeight(350);
		stage.show();
    }

    private Scene generePage(){
			// Création des ComboBox et des Labels
			ComboBox<String> ueComboBox = new ComboBox<>();
			for (UE ue : ues){
				ueComboBox.getItems().add(ue.getNom());
			}
			ueComboBox.setValue(cours.getUe().getNom());
		
			ComboBox<String> profComboBox = new ComboBox<>();
			UE ueInitiale = ues.stream()
				.filter(ue -> ue.getNom().equals(cours.getUe().getNom()))
				.findFirst()
				.orElse(null);
		
			if (ueInitiale != null) {
				profComboBox.getItems().addAll(ueInitiale.getIntervenantsLogin());
				profComboBox.setValue(cours.getIntervenantLogin());
			}
		
			// Liste des labels et des ComboBox
			Label ueLabel = new Label("UE:");
			Label profLabel = new Label("Intervenant:");
			Label typeLabel = new Label("Type de Cours:");
			Label dureeLabel = new Label("Durée:");
			Label heureDebutLabel = new Label("Heure de début:");
			Label salleLabel = new Label("Salle:");
		
			ComboBox<String> typeMenuDeroulant = new ComboBox<>();
			typeMenuDeroulant.getItems().addAll(TypeC.CM.toString(), TypeC.TD.toString(), TypeC.TP.toString());
			typeMenuDeroulant.setValue(cours.getType().toString());
		
			ComboBox<String> dureeMenuDeroulant = new ComboBox<>();
			dureeMenuDeroulant.getItems().addAll("1", "2", "3", "4");
			dureeMenuDeroulant.setValue(String.valueOf(cours.getDuree()));
		
			ComboBox<String> heureDebutMenuDeroulant = new ComboBox<>();
			heureDebutMenuDeroulant.getItems().addAll("8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18");
			heureDebutMenuDeroulant.setValue(String.valueOf(cours.getHeureDebut()));
		
			ComboBox<String> salleMenuDeroulant = new ComboBox<>();
			for (Salle s : salles) {
				salleMenuDeroulant.getItems().add(s.getNom());
			}
			salleMenuDeroulant.setValue(cours.getSalle().getNom());
		
			// Bouton de validation
			Button validerButton = new Button("Valider");
			validerButton.setOnAction(event -> {
				int indiceUeChoisie = ueComboBox.getSelectionModel().getSelectedIndex();
				UE ueChoisie = ues.get(indiceUeChoisie);
				String profChoisi = profComboBox.getValue();
				int dureeChosie = Integer.parseInt(dureeMenuDeroulant.getValue());
				TypeC typeChoisi = Cours.stringToTypeC(typeMenuDeroulant.getValue());
				int heureChoisie = Integer.parseInt(heureDebutMenuDeroulant.getValue());
				Salle salleChoisie = salles.get(salleMenuDeroulant.getSelectionModel().getSelectedIndex());
				if (ueChoisie != null && profChoisi != null) {
					Cours c = cours;
					modifierCours(c, ueChoisie, profChoisi, dureeChosie, typeChoisi, salleChoisie, heureChoisie);
					System.out.println("Id de la salle après modif " + c.getSalle());
					Outils.modifierCours(c);
					stage.close();
				}
			});
		
			HBox layout = new HBox(10);
			layout.getChildren().addAll(
				new VBox(20, ueLabel, profLabel,dureeLabel,heureDebutLabel,typeLabel,salleLabel),
				new VBox(10, ueComboBox, profComboBox,dureeMenuDeroulant,heureDebutMenuDeroulant,typeMenuDeroulant,salleMenuDeroulant,validerButton)
				
			);
			layout.setPadding(new Insets(20));
			
		
		
			return new Scene(layout, 700, 500);  // Tu peux ajuster la taille de la scène si nécessaire
		
		
	}

	private void modifierCours(Cours c,UE ue, String intervenant,int duree,TypeC type,Salle salle,int heure){
		c.setUe(ue);
		c.setIntervenantLogin(intervenant);
		c.setDuree(duree);
		c.setSalle(salle);
		c.setType(type);
		c.setHeureDebut(heure);
	}
	public Stage getStage() {
        return stage;
    }
}

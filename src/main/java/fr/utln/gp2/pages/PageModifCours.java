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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PageModifCours {

    private Stage stage;
    private List<UE> ues;
	private Long coursId;
	private List<Salle> salles;
	private Cours cours;

    public PageModifCours(Long id){
		stage = new Stage();
		this.coursId=id;
    }

    public void show(){
        stage.setTitle("Page de modification");
		stage.setMaximized(false);
		cours = Outils.getCoursById(coursId).join();
		ues = Outils.getAllUE().join();
		salles = Outils.getAllSalle().join();
		stage.setScene(generePage());
		stage.show();
    }

    private Scene generePage(){
		ComboBox<String> ueComboBox = new ComboBox<>();
		for (UE ue : ues){
			ueComboBox.getItems().add(ue.getNom());
		}
        ueComboBox.setValue(cours.getUe().getNom());
		
		ComboBox<String> profComboBox = new ComboBox<>();
        profComboBox.setValue(cours.getIntervenantLogin());

		ueComboBox.setOnAction(event -> {
			int indiceUeChoisie = ueComboBox.getSelectionModel().getSelectedIndex();
			UE ueChoisie = ues.get(indiceUeChoisie);
			if (ueChoisie != null) {
				profComboBox.getItems().setAll(ueChoisie.getIntervenantsLogin());
			}
		});

        ComboBox<String> typeMenuDeroulant = new ComboBox<>();
        typeMenuDeroulant.getItems().addAll(TypeC.CM.toString(),TypeC.TD.toString(),TypeC.TP.toString());
        typeMenuDeroulant.setValue(cours.getType().toString());

        ComboBox<String> dureeMenuDeroulant = new ComboBox<>();
        dureeMenuDeroulant.getItems().addAll("1","2","3","4");
        dureeMenuDeroulant.setValue(String.valueOf(cours.getDuree()));

		ComboBox<String> salleMenuDeroulant = new ComboBox<>();
        for(Salle s : salles){
        salleMenuDeroulant.getItems().add(s.getNom());
        }
        salleMenuDeroulant.setValue(cours.getSalle().getNom());

		Button validerButton = new Button("Valider");
		validerButton.setOnAction(event -> {
			int indiceUeChoisie = ueComboBox.getSelectionModel().getSelectedIndex();
			UE ueChoisie = ues.get(indiceUeChoisie);
			String profChoisi = profComboBox.getValue();
            int dureeChosie = Integer.parseInt(dureeMenuDeroulant.getValue());
            TypeC typeChoisi = Cours.stringToTypeC(typeMenuDeroulant.getValue());
			Salle salleChoisie = salles.get(salleMenuDeroulant.getSelectionModel().getSelectedIndex());
			if (ueChoisie != null && profChoisi != null){
				Cours c = Outils.getCoursById(coursId).join();
				modifierCours(c, ueChoisie, profChoisi, dureeChosie, typeChoisi, salleChoisie);
				System.out.println("Id de la salle apres modif "+c.getSalle().getSalle_id());
				Outils.modifierCours(c);
                stage.close();
			}
            
		});


		VBox layout = new VBox(10, ueComboBox, profComboBox,typeMenuDeroulant,dureeMenuDeroulant,salleMenuDeroulant, validerButton);
		Pane pane = new Pane();
		pane.getChildren().add(layout);
		return new Scene(pane);
	}

	private void modifierCours(Cours c,UE ue, String intervenant,int duree,TypeC type,Salle salle){
		c.setUe(ue);
		c.setIntervenantLogin(intervenant);
		c.setDuree(duree);
		c.setSalle(salle);
		c.setType(type);
	}
	public Stage getStage() {
        return stage;
    }
}

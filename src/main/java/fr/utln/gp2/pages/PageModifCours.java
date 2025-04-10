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

    private Stage stage;
    private List<UE> ues;

    public PageModifCours(Cours c){
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
        ueComboBox.setValue("UE");
		
		ComboBox<String> profComboBox = new ComboBox<>();
		profComboBox.setDisable(true); //Désactivé au début
        profComboBox.setValue("Professeur");

		ueComboBox.setOnAction(event -> {
			int indiceUeChoisie = ueComboBox.getSelectionModel().getSelectedIndex();
			UE ueChoisie = ues.get(indiceUeChoisie);
			if (ueChoisie != null) {
				profComboBox.getItems().setAll(ueChoisie.getIntervenantsLogin());
				profComboBox.setDisable(false);
			}
		});

        ComboBox<String> typeMenuDeroulant = new ComboBox<>();
        typeMenuDeroulant.getItems().addAll(TypeC.CM.toString(),TypeC.TD.toString(),TypeC.TP.toString());
        typeMenuDeroulant.setValue("Type de Cours");

        ComboBox<String> dureeMenuDeroulant = new ComboBox<>();
        dureeMenuDeroulant.getItems().addAll("1","2","3","4");
        dureeMenuDeroulant.setValue("Durée du cours");

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

}

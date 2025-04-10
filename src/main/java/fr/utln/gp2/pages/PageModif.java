package fr.utln.gp2.pages;

import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Promotion;
import fr.utln.gp2.entites.Salle;
import fr.utln.gp2.entites.UE;
import fr.utln.gp2.entites.Cours.TypeC;
import fr.utln.gp2.utils.Outils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.Date;

import fr.utln.gp2.entites.Cours;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.DayOfWeek;
import java.time.temporal.IsoFields;

public class PageModif{

	private Stage stage;
	private Personne p;
	private List<Cours> cours;
	private List<UE> ues;
	private int numSemaine;
    private int heureDebut;
    private int jour;
    private Promotion promo;
    private List<Salle> salles;

	public PageModif(Personne p, List<Cours> cours,int numSemaine,int heureDebut,int jour,Promotion promo){
		stage = new Stage();
		this.p = p;
		this.cours = cours;
		this.numSemaine=numSemaine;
		this.heureDebut=heureDebut;
		this.jour=jour;
		this.promo = promo;
	}

	public void show(){
		stage.setTitle("Page de modification");
		stage.setMaximized(false);

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
		ueComboBox.setValue("UE");
		
		ComboBox<String> profComboBox = new ComboBox<>();
		profComboBox.setDisable(true); // Désactivé au début
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

        ComboBox<String> salleMenuDeroulant = new ComboBox<>();
        for(Salle s : salles){
        salleMenuDeroulant.getItems().add(s.getNom());
        }
        salleMenuDeroulant.setValue("Salle");

		Button validerButton = new Button("Valider");
		validerButton.setOnAction(event -> {
			int indiceUeChoisie = ueComboBox.getSelectionModel().getSelectedIndex();
			UE ueChoisie = ues.get(indiceUeChoisie);
			String profChoisi = profComboBox.getValue();
            int dureeChosie = Integer.parseInt(dureeMenuDeroulant.getValue());
            TypeC typeChoisi = Cours.stringToTypeC(typeMenuDeroulant.getValue());
            Salle salleChoisie = salles.get(salleMenuDeroulant.getSelectionModel().getSelectedIndex());
			if (ueChoisie != null && profChoisi != null){
				ajouteUnCours(ueChoisie, profChoisi, dureeChosie, typeChoisi,salleChoisie);
                //System.out.println(Outils.getCoursByPromo(promo.getPromoId()).join());
                stage.close();
			}
			
		});
		
		VBox layout = new VBox(10, ueComboBox, profComboBox,typeMenuDeroulant,dureeMenuDeroulant,salleMenuDeroulant, validerButton);
		Pane pane = new Pane();
		pane.getChildren().add(layout);
		return new Scene(pane);
	}

	private void ajouteUnCours(UE ue, String intervenant,int duree,TypeC type,Salle salle){
        DayOfWeek jourDate = DayOfWeek.of(jour);
        LocalDate date = LocalDate
            .of(2025, 1, 1)
            .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, numSemaine)
            .with(DayOfWeek.MONDAY) // Commencer par le lundi
            .with(jourDate);  
            Cours c = new Cours(ue,Arrays.asList(promo),intervenant,heureDebut,duree,Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()),type,salle);
            Outils.persistence(c);



		// Map<Integer, List<Cours>> coursIntervenant = Outils.getCoursByIntervenant(intervenant).join();
		// for (Cours c : cours){

		// }

		// for (Cours c : coursIntervenant.get(numSemaine)){
			
		// }

		// Cours c = new Cours(null, null, null, numSemaine, numSemaine, null, null);
		// Outils.persistence(c);
	}

    public Stage getStage() {
        return stage;
    }
}
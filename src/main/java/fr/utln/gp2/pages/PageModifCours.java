package fr.utln.gp2.pages;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fr.utln.gp2.entites.Cours;
import fr.utln.gp2.entites.Cours.TypeC;
import fr.utln.gp2.entites.Promotion;
import fr.utln.gp2.entites.Salle;
import fr.utln.gp2.entites.UE;
import fr.utln.gp2.utils.Outils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class PageModifCours {

    private Stage stage = new Stage();
    private List<UE> ues;

	//private Long coursId;
	private List<Salle> salles;
	private Cours cours;
	private LocalDate dateChoisie;
	private Promotion promo;

    public PageModifCours(Cours c, Promotion promo){
		stage = new Stage();
		this.cours = c;
		this.promo = promo;
    }

    public void show(){
        stage.setTitle("Page de modification");
		stage.setMaximized(false);
		//cours = Outils.getCoursById(coursId).join();
		ues = Outils.getAllUE().join();
		salles = Outils.getAllSalle().join();
		stage.setScene(generePage());
		stage.setMinWidth(600);
		stage.setMinHeight(500);
		stage.show();
    }

    private Scene generePage(){
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

			Label dateLabel = new Label("Date du cours");
			DatePicker dateChoix = new DatePicker();
			dateChoix.setValue(LocalDate.now());

			dateChoix.setOnAction(event -> {
				dateChoisie = dateChoix.getValue();
				System.out.println("Date sélectionnée : " + dateChoisie);
			});

			Label messageErreur = new Label(); // Label pour afficher les messages d'erreur
    		messageErreur.setStyle("-fx-text-fill: red;");
		
			Button validerButton = new Button("Valider");
			validerButton.setOnAction(event -> {
				try{
				int indiceUeChoisie = ueComboBox.getSelectionModel().getSelectedIndex();
				UE ueChoisie = ues.get(indiceUeChoisie);
				String profChoisi = profComboBox.getValue();
				int dureeChosie = Integer.parseInt(dureeMenuDeroulant.getValue());
				TypeC typeChoisi = Cours.stringToTypeC(typeMenuDeroulant.getValue());
				int heureChoisie = Integer.parseInt(heureDebutMenuDeroulant.getValue());
				Salle salleChoisie = salles.get(salleMenuDeroulant.getSelectionModel().getSelectedIndex());
				
				if (ueChoisie != null && profChoisi != null) {
					Cours c = cours;
					modifierCours(c, ueChoisie, profChoisi, dureeChosie, typeChoisi, salleChoisie, heureChoisie,dateChoisie);
					System.out.println("Id de la salle après modif " + c.getSalle());
					Outils.modifierCours(c);
					stage.close();
				}}
				catch(Exception e){messageErreur.setText("Erreur : " + e.getMessage());}
			});
		
			HBox layout = new HBox(10);
			layout.getChildren().addAll(
				new VBox(20, ueLabel, profLabel,dureeLabel,heureDebutLabel,typeLabel,salleLabel,dateLabel),
				new VBox(10, ueComboBox, profComboBox,dureeMenuDeroulant,heureDebutMenuDeroulant,typeMenuDeroulant,salleMenuDeroulant,dateChoix,validerButton,messageErreur)
				
			);
			layout.setPadding(new Insets(20));
			
		
		
			return new Scene(layout, 700, 500);
	}

	private void modifierCours(Cours cours,UE ue, String intervenant,int duree,TypeC type,Salle salle,int heure,LocalDate jour){
		cours.setUe(ue);

		//Vérification intervenant libre sur ce créneau
		int numSemaine = jour.get(WeekFields.of(Locale.FRANCE).weekOfWeekBasedYear());
		int heureFin = heure + duree;
		Map<Integer, List<Cours>> coursIntervenant = Outils.getCoursByIntervenant(intervenant).join();
		if (!coursIntervenant.isEmpty()) {
			for (Cours c : coursIntervenant.get(numSemaine)) {
				Date dateIteration = c.getJour();
				LocalDate localDate = dateIteration.toInstant()
						.atZone(ZoneId.systemDefault())
						.toLocalDate();
				if (localDate.equals(jour)) {
					int heureFinExistant = c.getHeureDebut() + c.getDuree();
					if (c.getHeureDebut() < heure && heure < heureFinExistant) {
						throw new IllegalArgumentException("L'intervenant enseigne déjà sur ce créneau");
					} else if (c.getHeureDebut() < heureFin && heureFin < heureFinExistant) {
						throw new IllegalArgumentException("L'intervenant enseigne déjà sur ce créneau");
					} else if (heure <= c.getHeureDebut() && heureFin >= heureFinExistant) {
						throw new IllegalArgumentException("L'intervenant enseigne déjà sur ce créneau");
					}
				}
			}
		}
		cours.setIntervenantLogin(intervenant);

		//Vérification que la promo n'ait pas déjà un cours sur cette horaire (tous types de chevauchements)
		Map<Integer, List<Cours>> coursExistants = Outils.getCoursByPromo(promo.getPromoId()).join();
		if (!coursExistants.isEmpty()) {
			for (Cours c : coursExistants.get(numSemaine)) {
				Date dateIteration = c.getJour();
				LocalDate localDate = dateIteration.toInstant()
						.atZone(ZoneId.systemDefault())
						.toLocalDate();
				if (localDate.equals(jour)) {
					int heureFinExistant = c.getHeureDebut() + c.getDuree();
					if (c.getHeureDebut() < heure && heure < heureFinExistant) {
						throw new IllegalArgumentException("Le début du cours choisi entre en conflit avec un autre cours");
					} else if (c.getHeureDebut() < heureFin && heureFin < heureFinExistant) {
						throw new IllegalArgumentException("La fin du cours choisi entre en conflit avec un autre cours");
					} else if (heure <= c.getHeureDebut() && heureFin >= heureFinExistant) {
						throw new IllegalArgumentException("Le cours choisi chevauche un autre cours");
					}
				}
			}
		}
		cours.setJour(Date.from(dateChoisie.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		cours.setDuree(duree);
		cours.setHeureDebut(heure);

		//Vérification salle libre sur ce créneau
		Date dateChoisieEnDate = Date.from(jour.atStartOfDay(ZoneId.systemDefault()).toInstant());
		List<Salle> sallesOccupees = Outils.getSalleByHeure(numSemaine, dateChoisieEnDate, heure,duree);
		System.out.println(sallesOccupees);
		if (!sallesOccupees.isEmpty()) {
			for (Salle s : sallesOccupees) {
				if (salle.getSalleId().equals(s.getSalleId())) {
					throw new IllegalArgumentException("Salle déjà occupée sur ce créneau");
				}
			}
		}
		cours.setSalle(salle);

		cours.setType(type);
	}
	public Stage getStage() {
        return stage;
    }

}

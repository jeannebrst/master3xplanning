	package fr.utln.gp2.pages;

	import fr.utln.gp2.entites.Personne;
	import fr.utln.gp2.entites.Promotion;
	import fr.utln.gp2.entites.Salle;
	import fr.utln.gp2.entites.UE;
	import fr.utln.gp2.entites.Cours.TypeC;
	import fr.utln.gp2.utils.Outils;

	import java.util.*;

	import fr.utln.gp2.entites.Cours;
	import javafx.scene.Scene;
	import javafx.scene.control.Button;
	import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
	import javafx.scene.layout.VBox;
	import javafx.stage.Stage;
	import java.time.LocalDate;
	import java.time.ZoneId;
	import java.time.DayOfWeek;
	import java.time.temporal.IsoFields;

	public class PageCreationCours {

		private Stage stage;
		private Personne p;
		private List<Cours> cours;
		private List<UE> ues;
		private int numSemaine;
		private int heureDebut;
		private int jour;
		private Promotion promo;
		private List<Salle> salles;

		public PageCreationCours(Personne p, List<Cours> cours, int numSemaine, int heureDebut, int jour, Promotion promo){
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
			stage.setMinWidth(400);
			stage.setMinHeight(350);
			stage.show();
		}

		private Scene generePage(){
			ComboBox<String> ueComboBox = new ComboBox<>();
			for (UE ue : ues){
				ueComboBox.getItems().add(ue.getNom());
			}
			ueComboBox.setValue("UE");
			
			ComboBox<String> profComboBox = new ComboBox<>();
			profComboBox.setDisable(true);
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
				Salle salleChoisie = salles.get(salleMenuDeroulant.getSelectionModel().getSelectedIndex());
				if (ueChoisie != null && profChoisi != null){
					ajouteUnCours(ueChoisie, profChoisi, dureeChosie, typeChoisi,salleChoisie);
					stage.close();
				}
			}
			catch(Exception e){messageErreur.setText("Erreur : " + e.getMessage());
		}
			});
			
			VBox layout = new VBox(10, ueComboBox, profComboBox,typeMenuDeroulant,dureeMenuDeroulant,salleMenuDeroulant, validerButton,messageErreur);
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

			//Vérification que la promo n'ait pas déjà un cours sur cette horaire (tous types de chevauchements)
			int heureFin = heureDebut + duree;
			Map<Integer, List<Cours>> coursExistants = Outils.getCoursByPromo(promo.getPromoId()).join();
			if (!coursExistants.isEmpty() && coursExistants != null) {
				for (Cours c : coursExistants.getOrDefault(numSemaine, Collections.emptyList())) {
					Date dateIteration = c.getJour();
					LocalDate localDate = dateIteration.toInstant()
							.atZone(ZoneId.systemDefault())
							.toLocalDate();
					if (localDate.equals(date)) {
						int heureFinExistant = c.getHeureDebut() + c.getDuree();
						if (c.getHeureDebut() < heureDebut && heureDebut < heureFinExistant) {
							throw new IllegalArgumentException("Le début du cours choisi entre en conflit avec un autre cours");
						} else if (c.getHeureDebut() < heureFin && heureFin < heureFinExistant) {
							throw new IllegalArgumentException("La fin du cours choisi entre en conflit avec un autre cours");
						} else if (heureDebut <= c.getHeureDebut() && heureFin >= heureFinExistant) {
							throw new IllegalArgumentException("Le cours choisi chevauche un autre cours");
						}
					}
				}
			}

			//Vérification salle libre sur ce créneau
			Date dateChoisie = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
			List<Salle> sallesOccupees = Outils.getSalleByHeure(numSemaine, dateChoisie, heureDebut,duree);
			System.out.println(sallesOccupees);
			if (!sallesOccupees.isEmpty()) {
				for (Salle s : sallesOccupees) {
					if (salle.getSalleId().equals(s.getSalleId())) {
						throw new IllegalArgumentException("Salle déjà occupée sur ce créneau");
					}
				}
			}

			//Vérification intervenant libre sur ce créneau
			Map<Integer, List<Cours>> coursIntervenant = Outils.getCoursByIntervenant(intervenant).join();
			System.out.println("\n\n\nTEST 1 RETOUR LISTE" + coursIntervenant+"\n\n\n");
			if (!coursIntervenant.isEmpty() && coursIntervenant != null) {
				System.out.println("\n\n\nTEST 2 RETOUR LISTE" + coursIntervenant+"\n\n\n");
				for (Cours c : coursIntervenant.getOrDefault(numSemaine, Collections.emptyList())) {
					System.out.println("\n\n\nTEST 3 RETOUR LISTE" + coursIntervenant+"\n\n\n");
					Date dateIteration = c.getJour();
					LocalDate localDate = dateIteration.toInstant()
							.atZone(ZoneId.systemDefault())
							.toLocalDate();
					if (localDate.equals(date)) {
						System.out.println("\n\n\nTEST 4 RETOUR LISTE" + coursIntervenant+"\n\n\n");
						int heureFinExistant = c.getHeureDebut() + c.getDuree();
						if (c.getHeureDebut() < heureDebut && heureDebut < heureFinExistant) {
							throw new IllegalArgumentException("L'intervenant enseigne déjà sur ce créneau");
						} else if (c.getHeureDebut() < heureFin && heureFin < heureFinExistant) {
							throw new IllegalArgumentException("L'intervenant enseigne déjà sur ce créneau");
						} else if (heureDebut <= c.getHeureDebut() && heureFin >= heureFinExistant) {
							throw new IllegalArgumentException("L'intervenant enseigne déjà sur ce créneau");
						}
						System.out.println("\n\n\nTEST 5 RETOUR LISTE" + coursIntervenant+"\n\n\n");
					}
				}
			}
			System.out.println("\n\n\nTEST 6 RETOUR LISTE" + coursIntervenant+"\n\n\n");
			Cours coursValide = new Cours(ue,Arrays.asList(promo),intervenant,heureDebut,duree,Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()),type,salle);
			Outils.persistence(coursValide);
		}

		public Stage getStage() {
			return stage;
		}
	}
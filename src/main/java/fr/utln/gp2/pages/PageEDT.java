package fr.utln.gp2.pages;

import java.net.http.HttpClient;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import fr.utln.gp2.entites.Absence;
import fr.utln.gp2.entites.Cours;
import fr.utln.gp2.entites.Note;
import fr.utln.gp2.entites.Cours.TypeC;
import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Personne.Role;
import fr.utln.gp2.entites.Promotion;
import fr.utln.gp2.utils.Outils;
import fr.utln.gp2.utils.PromotionId;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import lombok.val;


public class PageEDT {
	private static final HttpClient client = HttpClient.newHttpClient();

	private GridPane grilleEdt = new GridPane();
	private LocalDate lundi;
	private Label semaine;
	private int numSemaine;
	// private Label texteEdition = new Label("MODE EDITION ACTIVEEE !!!!");
	private Scene sceneEDT; 
	private Scene sceneInfos; 
	private Stage stage;
	private Boolean modeEdition = false;
	private Map<TypeC,Color> couleurCours = new EnumMap<>(Map.of(TypeC.CM,Color.DEEPPINK,TypeC.TD,Color.DEEPSKYBLUE,TypeC.TP,Color.ORANGE));
	private Map<Integer, List<Cours>> coursMap = new HashMap<>();
	private List<StackPane> coursCells = new ArrayList<>();
	private ComboBox<String> menuPromo = new ComboBox<>();
	
	private Personne p;
	
	public PageEDT(String login) {
		p = Outils.getPersonneInfo(login).join();
		// System.out.println("Personne récup : " + p + "\n");

		if (p.getRole().equals(Role.GESTIONNAIRE)){
			p.setPromos(Outils.getAllPromo().join());
		}

		stage = new Stage();
		
	}
	
	public void show(){
		lundi = LocalDate.now().with(DayOfWeek.MONDAY);
		semaine = new Label("");
		modifLabelSemaine();
		genereEDT();
		sceneEDT = new Scene(genereSceneEDT());
		sceneInfos = new Scene(genereSceneInfos());
		if(p.getRole().equals(Role.PROFESSEUR)){
			coursMap = Outils.getCoursByIntervenant(p.getLogin()).join();
		}
		else{
			getCoursOfPromo(0);
		}
		majEDT();
	
		stage.setTitle("Page d'accueil");
		stage.setScene(sceneEDT);
		stage.setMaximized(false);
		stage.show();
	}

	private void getCoursOfPromo(int indice){
		Promotion promo = p.getPromos().get(indice);
		coursMap = Outils.getCoursByPromo(promo.getPromoId()).join();
		System.out.println("" + coursMap + "\n");
	}

	private HBox genereBoutonHaut(){
		HBox boiteBtn = new HBox(10);
		Button cours = new Button("Cours");
		Button infos = new Button("Informations Personnelles");
		Button notes = new Button("Notes");
		Button deconnexion = new Button("Déconnexion");

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		infos.setOnAction(e -> {
			stage.setScene(sceneInfos);
		});

		cours.setOnAction(e -> {
			stage.setScene(sceneEDT);
		});

		notes.setOnAction(e -> {
			ouvrirPageNotes();
		});

		deconnexion.setOnAction(e -> {
			deconnect();
		});

		boiteBtn.getChildren().addAll(cours,infos);
		if(p.getRole().equals(Role.PROFESSEUR)){
			boiteBtn.getChildren().add(notes);
		}
		boiteBtn.getChildren().addAll(spacer,deconnexion);
		
		return boiteBtn;
	}

	private StackPane genereSceneEDT(){
		HBox boiteMenuBtnEdition = new HBox(10);
		if(p.getRole().equals(Role.GESTIONNAIRE)){
			boiteMenuBtnEdition.getChildren().add(genereBoutonGestionnaire());
		}
		
		for (Promotion p : p.getPromos() ){
			PromotionId pTemporaire = p.getPromoId();
			menuPromo.getItems().add(pTemporaire.getType().toString()+" "+pTemporaire.getAnnee()+" "+ pTemporaire.getCategorie());
		}
		menuPromo.setValue(p.getPromos().get(0).getPromoId().getType().toString()+" "+p.getPromos().get(0).getPromoId().getAnnee()+" "+p.getPromos().get(0).getPromoId().getCategorie());
		menuPromo.setOnAction(e -> {
			getCoursOfPromo(menuPromo.getSelectionModel().getSelectedIndex());
			majEDT();
		});

		boiteMenuBtnEdition.getChildren().add(menuPromo);
		VBox pageComplete = new VBox(10);
		Button btnPreviousWeek = new Button("<");
		Button btnNextWeek = new Button(">");

		btnPreviousWeek.setOnAction(e -> {
			lundi = lundi.minusWeeks(1);
			majEDT();
		});

		btnNextWeek.setOnAction(e -> {
			lundi = lundi.plusWeeks(1);
			majEDT();
		});


		HBox semainesBox = new HBox(5);
		for (int i = 1; i <= 52; i++) {
			int semaineNum = i;
			Button weekButton = new Button(String.valueOf(i));
			weekButton.setOnAction(event -> {
				lundi = getLundiSemaine(semaineNum);
				majEDT();
			});

			semainesBox.getChildren().add(weekButton);
		}

		StackPane cellBouton = new StackPane();
		cellBouton.setAlignment(Pos.CENTER);  
		cellBouton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);  
		
		HBox boite = new HBox(10);
		boite.setAlignment(Pos.CENTER);
		boite.getChildren().addAll(btnPreviousWeek, semaine, btnNextWeek);
		cellBouton.getChildren().add(boite);
		
		grilleEdt.add(cellBouton, 0, 0);
		
		GridPane.setHalignment(cellBouton, HPos.CENTER);
		GridPane.setValignment(cellBouton, VPos.CENTER);
		
		HBox boiteBtn = genereBoutonHaut(); 



		pageComplete.getChildren().add(boiteBtn);
		if (!p.getRole().equals(Role.PROFESSEUR)){
			pageComplete.getChildren().add(boiteMenuBtnEdition);
		}
		
		pageComplete.getChildren().addAll(grilleEdt,semainesBox);
		VBox.setVgrow(grilleEdt, Priority.ALWAYS);

		StackPane scene1 = new StackPane();
		// scene1.setMinSize(480, 270);
		//scene1.setPrefSize(1920, 1080);
		// scene1.setMaxSize(1920, 1080);
		
		scene1.getChildren().add(pageComplete);
		return scene1;
	}

	private void genereEDT(){
		String[] horaires = {"8h","9h","10h","11h","12h","13h","14h","15h","16h","17h","18h"};

		grilleEdt.setGridLinesVisible(true);
		
		for (int i = 0; i < 12; i++) {
			RowConstraints row = new RowConstraints();
			row.setPercentHeight(100 / 10f);  
			grilleEdt.getRowConstraints().add(row);
		}

		for (int i =0; i <6; i++){
			ColumnConstraints column = new ColumnConstraints();
			column.setPercentWidth(100 / 6f);  
			grilleEdt.getColumnConstraints().add(column);
		}

		for (int i = 0; i < 5; i++) {
			LocalDate jourDate = lundi.plusDays(i);
			String jourNom = jourDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE);
			String moisNom = jourDate.getMonth().getDisplayName(TextStyle.FULL, Locale.FRANCE);
			Label jourLabel = new Label(jourNom + " " + jourDate.getDayOfMonth() + " " + moisNom);
			StackPane jourCell = new StackPane();
			jourCell.setAlignment(Pos.CENTER);
			jourCell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			jourCell.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(0), new Insets(1))));
			
			jourCell.getChildren().add(jourLabel);
			GridPane.setHalignment(jourCell, HPos.CENTER);
			GridPane.setValignment(jourCell, VPos.CENTER);
			
			grilleEdt.add(jourCell, i+1, 0);
		}

		for (int i=0; i<horaires.length;i++){
			Label horaire = new Label(horaires[i]);
			StackPane horaireCellule = new StackPane();
			horaireCellule.setAlignment(Pos.CENTER);
			horaireCellule.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			horaireCellule.setBackground(new Background(new BackgroundFill(Color.LIGHTGOLDENRODYELLOW, new CornerRadii(0), new Insets(1))));

			horaireCellule.getChildren().add(horaire);

			GridPane.setHalignment(horaireCellule, HPos.CENTER);  
			GridPane.setValignment(horaireCellule, VPos.CENTER);
			grilleEdt.add(horaireCellule, 0, i+1);
		}

		for (int i=1; i<12; i++){
			for (int j=1; j<6; j++){
				int num = i*6 + j;
				int indiceHeure = i;
				int indiceJour=j;
				StackPane cell = new StackPane();
				cell.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE,new CornerRadii(0), new Insets(1))));
				cell.setOnMouseClicked(event -> {if(modeEdition){ouvrirPageModif(indiceHeure,indiceJour);}});
				grilleEdt.add(cell, j, i);
			}
		}
	}

	private void majEDT(){
		viderEdt();
		modifLabelSemaine();

		for(int i=0; i<5; i++){
			LocalDate jourDate = lundi.plusDays(i);
			String jourNom = jourDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE);
			String moisNom = jourDate.getMonth().getDisplayName(TextStyle.FULL, Locale.FRANCE);
			
			StackPane jourCell = (StackPane) grilleEdt.getChildren().get(i + 1);
			Label jourLabel = (Label) jourCell.getChildren().get(0);
			jourLabel.setText(jourNom + " " + jourDate.getDayOfMonth() + " " + moisNom);
		}

		ajouteAllCours();
	}

	private void ajouterCours(Cours c){
		HBox boiteVbox = new HBox(50);
		VBox boiteLabelUE_SALLE = new VBox(1+c.getDuree()*5);
		VBox boiteLabelProf_TYpe = new VBox(1+c.getDuree()*5);
		Label nom = new Label(c.getUe().getNom());
		String chaine = c.getIntervenantLogin().toUpperCase().charAt(0)+"."+c.getIntervenantLogin().toUpperCase().charAt(1)+c.getIntervenantLogin().substring(2);
		Label prof = new Label(chaine);
		Label salle = new Label(c.getSalle().getNom());
		Label typeCours = new Label(c.getType().toString());

		Label[] labels = {nom,prof,salle,typeCours};
		for (Label l : labels){
			l.setFont(Font.font("Arial", FontWeight.BOLD, 14));
			l.setTextFill(Color.WHITE);
		}
		boiteLabelUE_SALLE.setAlignment(Pos.CENTER);
		boiteLabelUE_SALLE.getChildren().addAll(nom,salle);

		boiteLabelProf_TYpe.setAlignment(Pos.CENTER);
		boiteLabelProf_TYpe.getChildren().addAll(prof,typeCours);

		boiteVbox.setAlignment(Pos.CENTER);
		boiteVbox.getChildren().addAll(boiteLabelUE_SALLE,boiteLabelProf_TYpe);

		LocalDate localDate = c.getJour().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int jourSemaine = localDate.get(WeekFields.of(Locale.FRANCE).dayOfWeek());

		ContextMenu menuModifSupp = new ContextMenu();

		MenuItem modifier = new MenuItem("Modifier");

		modifier.setOnAction(event -> {
			if(modeEdition){ouvrirPageModifCours(c);}
		});

		MenuItem supprimer = new MenuItem("Supprimer");
		supprimer.setOnAction(event -> {
			long coursID = c.getCoursId();
			Outils.supprimerCoursById(coursID);
			getCoursOfPromo(menuPromo.getSelectionModel().getSelectedIndex());
			majEDT();

		});

		menuModifSupp.getItems().addAll(modifier, supprimer);


		StackPane cell = new StackPane();
		cell.getChildren().add(boiteVbox);
		cell.setOnMouseClicked(event -> {});
		//cell.getChildren().add(label);
		cell.setAlignment(Pos.CENTER);  
		cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);  
		cell.setBackground(new Background(new BackgroundFill(couleurCours.get(c.getType()), new CornerRadii(0), new Insets(1))));

		cell.setOnContextMenuRequested(event -> {if(modeEdition){menuModifSupp.show(cell, event.getScreenX(), event.getScreenY());};});
		
		grilleEdt.add(cell, jourSemaine, c.getHeureDebut() - 7);//-7 car la grid commence à 8h
		GridPane.setRowSpan(cell, c.getDuree());
		GridPane.setColumnSpan(cell, 1);  

		GridPane.setHalignment(cell, HPos.CENTER);
		GridPane.setValignment(cell, VPos.CENTER);
		coursCells.add(cell);
	}

	private void ajouteAllCours(){
		if (coursMap.get(numSemaine) != null){
			for (Cours c : coursMap.get(numSemaine)){
				ajouterCours(c);
			}
		}
	}

	private void modifLabelSemaine(){
		numSemaine = lundi.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
		semaine.setText("Semaine n°" + numSemaine);
	}

	private void viderEdt(){
		for(StackPane sp : coursCells){
			grilleEdt.getChildren().remove(sp);
		}
	}

	public LocalDate getLundiSemaine(int semaineNum) {
		return LocalDate.of(LocalDate.now().getYear(), 1, 1)
				.with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, semaineNum)
				.with(java.time.DayOfWeek.MONDAY);
	}


	public Pane genereSceneInfos(){
		VBox boiteInfo = new VBox();
		HBox boitePage = new HBox(50);
		VBox boiteNotes = new VBox();
		VBox boiteAbsences = new VBox();
		HBox boiteBtn = new HBox(genereBoutonHaut());
		Pane sceneInfos = new Pane();

		ScrollPane scrollNotes = new ScrollPane(boiteNotes);
		scrollNotes.setFitToWidth(true);
		scrollNotes.setPrefHeight(500);
		scrollNotes.setMaxHeight(500);
		scrollNotes.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

		VBox boitesPhotoNom = new VBox(15);
		boitesPhotoNom.setAlignment(Pos.CENTER);


		VBox boiteInfos = new VBox(30);
		boiteInfos.setAlignment(Pos.CENTER_LEFT);
		

		Label labelNom = new Label(p.getNom().toUpperCase() + " " + p.getPrenom());
		labelNom.setFont(Font.font("Arial", FontWeight.BOLD,40));
		labelNom.setTextFill(Color.BLACK);
		

		Image imageRole = new Image("file:src/main/resources/" + p.getRole().toString() + ".jpg");
		ImageView imageview = new ImageView(imageRole);
		imageview.setFitWidth(400);
		imageview.setFitHeight(400);


		Label login = new Label("Login : "+p.getLogin());
		Label email = new Label("Email : "+p.getMail());

		boitesPhotoNom.getChildren().addAll(imageview,labelNom);
		boiteInfos.getChildren().addAll(login,email);
		boiteInfos.setLayoutX(200);
		boiteInfos.setLayoutY(500);

		Label[] labels = {labelNom,login,email,new Label()};
		if (!p.getRole().equals(Role.GESTIONNAIRE)){
			String chainePromo = "";
			for (Promotion promo : p.getPromos() ){
				chainePromo+=promo.getPromoId().toString() + " | ";
			}

			Label promoLabel = new Label("Promotion(s) : " + chainePromo);
			labels[3]=promoLabel;
			boiteInfos.getChildren().add(promoLabel);
		}

		for(Label l : labels){
			l.setFont(Font.font("Arial", FontWeight.BOLD, 40));
			l.setTextFill(Color.BLACK);
		}

		boitesPhotoNom.setLayoutX(800);
		boitesPhotoNom.setLayoutY(50);

		SimpleDateFormat format = new SimpleDateFormat("dd/MM");
		if(p.getRole().equals(Role.ETUDIANT)){
		boiteNotes.setLayoutY(200);
		Label notes = new Label("Notes");
		notes.setFont(Font.font("Arial", FontWeight.BOLD, 25));
		notes.setTextFill(Color.BLACK);
		boiteNotes.getChildren().addAll(notes);
		//Affichage bordure pour debug
		boiteNotes.setBorder(new Border(new BorderStroke(Color.LIGHTBLUE, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(5))));
		boiteNotes.setPadding(new Insets(10, 15, 10, 15)); 
		boiteNotes.setMinHeight(500);
		boiteNotes.setMinWidth(150);
		//Notes de l'étudiant triées par date (plus récent au plus ancien)
		List<Note> notesTriees = p.getNotes().stream()
			.sorted(Comparator.comparing(Note::getDate).reversed())
			.collect(Collectors.toList());

		

		notesTriees.forEach(n -> {
			HBox noteHBox = new HBox(30); // Contient tout
			VBox infoNote = new VBox(5); // Nom de l'UE + Date
			VBox valeurNote = new VBox();
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
		
			noteHBox.getChildren().addAll(infoNote, spacer, valeurNote);
			noteHBox.setPadding(new Insets(5));
			boiteNotes.getChildren().add(noteHBox);
		});}

		// Pour trier les absences mais plus tard
		// List<Absence> absencesTriees = p.getAbsences().stream()
		// .sorted(Comparator.comparing((Absence abs) -> abs.getCours().getDate(), Comparator.reverseOrder()))
		// .collect(Collectors.toList());

		Label absenceLabel = new Label("Absences");
		boiteAbsences.getChildren().addAll(absenceLabel);
		p.getAbsences().forEach(abs -> {
			VBox absenceVBox = new VBox(); //Nom de l'ue + Date
			Cours coursAbsence = abs.getCours();
			Label ueLabel = new Label(coursAbsence.getUe().getNom());
			String dateFormatee = format.format(coursAbsence.getJour());
			Label dateLabel = new Label(dateFormatee);

			absenceVBox.getChildren().addAll(ueLabel, dateLabel);
			boiteAbsences.getChildren().addAll(absenceVBox);
		});

		boiteInfo.getChildren().addAll(boiteBtn,boitesPhotoNom,boiteInfos);
		boitePage.getChildren().addAll(boiteInfo,scrollNotes);
		sceneInfos.getChildren().addAll(boitePage);
		
		return sceneInfos;
	}

	public Button genereBoutonGestionnaire(){
		Button boutonModif = new Button("Modifier l'EDT");
		boutonModif.setOnAction(e -> {
			modeEdition=!modeEdition;
			if(modeEdition){
				stage.setTitle("MODE EDITION ACTIVEEE !!!!");
			}
			else{
				stage.setTitle("Page d'accueil");
			}
		});
		return boutonModif;
	}

	private void ouvrirPageModif(int i,int j){
		int promoIndex = menuPromo.getSelectionModel().getSelectedIndex();
		Promotion promo = p.getPromos().get(promoIndex);
		Platform.runLater(()-> {
			PageCreationCours pageModif = new PageCreationCours(p,coursMap.get(numSemaine),numSemaine,i+7,j,promo);
			pageModif.show();
			pageModif.getStage().setOnHidden(e -> {
				getCoursOfPromo(promoIndex);
				majEDT();
			});
		});

	}

	private void ouvrirPageModifCours(Cours c){
		int promoIndex = menuPromo.getSelectionModel().getSelectedIndex();
		Promotion promo = p.getPromos().get(promoIndex);
		Platform.runLater(()-> {
			PageModifCours pageModifCours = new PageModifCours(c,promo);
			pageModifCours.show();
			pageModifCours.getStage().setOnHidden(e -> {
				getCoursOfPromo(promoIndex);
				majEDT();
			});
		});

	}

	private void ouvrirPageNotes(){
		Platform.runLater(()-> {
			PageNotes pageNotes = new PageNotes(p.getPromos());
			pageNotes.show();
		});

	}

	private void deconnect() {
		Platform.runLater(() -> {
			stage.close(); //Ferme la fenêtre actuelle

			//Ouvre la page EDT
			PageLogin pageLogin = new PageLogin();
			pageLogin.start(stage);
		});
	}

}
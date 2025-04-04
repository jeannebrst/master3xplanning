package fr.utln.gp2.pages;

import java.net.http.HttpClient;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fr.utln.gp2.entites.Cours;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class PageEDT {
	private static final HttpClient client = HttpClient.newHttpClient();

	private GridPane grilleEdt = new GridPane();
	private LocalDate lundi;
	private Label semaine;
	private int numSemaine;
	private Label texteEdition = new Label("MODE EDITION ACTIVEEE !!!!");
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
		
		infos.setOnAction(e -> {
			stage.setScene(sceneInfos);
			

		});
		cours.setOnAction(e -> {
			stage.setScene(sceneEDT);
			

		});
		boiteBtn.getChildren().addAll(cours,infos);
		
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
		});//Metre la fonction de Sh<3wn
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
		boite.setAlignment(Pos.CENTER);  // Aligner le contenu de la HBox au centre
		boite.getChildren().addAll(btnPreviousWeek, semaine, btnNextWeek);
		cellBouton.getChildren().add(boite);
		
		// Ajouter la cellule dans la grille
		grilleEdt.add(cellBouton, 0, 0);
		
		// Centrer la cellule dans la grille
		GridPane.setHalignment(cellBouton, HPos.CENTER);
		GridPane.setValignment(cellBouton, VPos.CENTER);
		
		HBox boiteBtn = genereBoutonHaut(); 



		pageComplete.getChildren().add(boiteBtn);
		if (!p.getRole().equals(Role.PROFESSEUR)){
			pageComplete.getChildren().add(boiteMenuBtnEdition);
		}
		
		pageComplete.getChildren().addAll(grilleEdt,semainesBox);
		VBox.setVgrow(grilleEdt, Priority.ALWAYS);

		// Créer un layout pour les boutons et la grille
		StackPane scene1 = new StackPane();
		// scene1.setMinSize(480, 270);
		//scene1.setPrefSize(1920, 1080);
		// scene1.setMaxSize(1920, 1080);
		
		scene1.getChildren().add(pageComplete);
		return scene1;
	}

	private void genereEDT(){
		String[] horaires = {"8h","9h","10h","11h","12h","13h","14h","15h","16h","17h","18h"};

		grilleEdt.setGridLinesVisible(true);  // Affiche les lignes de la grille
		
		// Définir les contraintes de ligne et de colonne pour avoir la même taille
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

		// Ajouter des labels dans chaque cellule de la grille
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
					
				;
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
			Label jourLabel = (Label) jourCell.getChildren().get(0); // Le Label est le premier enfant du StackPane
			jourLabel.setText(jourNom + " " + jourDate.getDayOfMonth() + " " + moisNom);
		}

		ajouteAllCours();
	}

	private void ajouterCours(Cours c){
		HBox boiteVbox = new HBox(50);
		VBox boiteLabelUE_SALLE = new VBox(1+c.getDuree()*5);
		VBox boiteLabelProf_TYpe = new VBox(1+c.getDuree()*5);
		Label nom = new Label(c.getUe().getNom());
		Label prof = new Label(c.getIntervenantLogin());
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
			if(modeEdition){ouvrirPageModif(jourSemaine, c.getHeureDebut() - 7);}
		});

		MenuItem supprimer = new MenuItem("Supprimer");
		supprimer.setOnAction(event -> {
			long coursID = c.getCoursId();
			//supprimerCours(coursID);
		});

		menuModifSupp.getItems().addAll(modifier, supprimer);


		// Crée un StackPane pour centrer le label dans la cellule
		StackPane cell = new StackPane();
		cell.getChildren().add(boiteVbox);
		cell.setOnMouseClicked(event -> {});
		//cell.getChildren().add(label);
		cell.setAlignment(Pos.CENTER);  
		cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);  
		cell.setBackground(new Background(new BackgroundFill(couleurCours.get(c.getType()), new CornerRadii(0), new Insets(1))));
		// Ajouter la cellule dans la grille

		cell.setOnContextMenuRequested(event -> {if(modeEdition){menuModifSupp.show(cell, event.getScreenX(), event.getScreenY());};});
        
		grilleEdt.add(cell, jourSemaine, c.getHeureDebut() - 7);//-7 car la grid commence à 8h
		GridPane.setRowSpan(cell, c.getDuree());
		GridPane.setColumnSpan(cell, 1);  

		// Centrer le StackPane dans la cellule de la grille
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
		HBox boiteBtn = new HBox(genereBoutonHaut()); 
		Pane sceneInfos = new Pane();
		VBox boitesPhotoNom = new VBox(15);
		boitesPhotoNom.setAlignment(Pos.CENTER);
		VBox boiteInfos = new VBox(30);
		boiteInfos.setAlignment(Pos.CENTER_LEFT);
		
		Label labelNom = new Label(p.getNom().toUpperCase()+" "+p.getPrenom());
		labelNom.setFont(Font.font("Arial",FontWeight.BOLD,25));
		labelNom.setTextFill(Color.BLACK);
		

		Image imageRole = new Image("file:src/main/resources/"+p.getRole().toString()+".jpg");
		ImageView imageview = new ImageView(imageRole);
		imageview.setFitWidth(200);
		imageview.setFitHeight(200);


		Label login = new Label("Login : "+p.getLogin());
		Label email = new Label("Email : "+p.getMail());

		boitesPhotoNom.getChildren().addAll(imageview,labelNom);
		boiteInfos.getChildren().addAll(login,email);
		boiteInfos.setLayoutX(100);
		boiteInfos.setLayoutY(500);
		Label[] labels = {labelNom,login,email,new Label()};
		if (p.getRole().equals(Role.GESTIONNAIRE)){

		}
		else{
		String chainePromo = "";
		for (Promotion promo : p.getPromos() ){
			chainePromo+=promo.getPromoId().toString()+" | ";
		}
		Label promoLabel = new Label("Promotion(s) : "+chainePromo);
		
		labels[3]=promoLabel;
		
		boiteInfos.getChildren().add(promoLabel);
		}
		for(Label l : labels){
			l.setFont(Font.font("Arial", FontWeight.BOLD, 25));
    		l.setTextFill(Color.BLACK);
		}

		boitesPhotoNom.setLayoutX(800);
		boitesPhotoNom.setLayoutY(50);
		sceneInfos.getChildren().addAll(boiteBtn,boitesPhotoNom,boiteInfos);
		
		
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

	private void supprimerCours(long id){

	}

	private void ouvrirPageModif(int i,int j){
		int promoIndex = menuPromo.getSelectionModel().getSelectedIndex();
		Promotion promo = p.getPromos().get(promoIndex);
		Platform.runLater(()-> {
			PageModif pageModif = new PageModif(p,coursMap.get(numSemaine),numSemaine,i+7,j,promo);
			pageModif.show();
			pageModif.getStage().setOnHidden(e -> {
				getCoursOfPromo(promoIndex);
				majEDT();
			});
		});

	}

}
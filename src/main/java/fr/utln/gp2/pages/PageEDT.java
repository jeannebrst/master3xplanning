package fr.utln.gp2.pages;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.Calendar;

import com.arjuna.ats.internal.arjuna.objectstore.jdbc.drivers.postgres_driver;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.utln.gp2.entites.Cours;
import fr.utln.gp2.entites.Cours.TypeC;
import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Promotion;
import fr.utln.gp2.utils.Outils;


public class PageEDT {
	private static final HttpClient client = HttpClient.newHttpClient();

	private GridPane grilleEdt = new GridPane();
	private LocalDate lundi;
	private Label semaine;
	private int numSemaine;

	private Scene sceneEDT; 
	private Scene sceneInfos; 
	private Stage stage;

	private Map<TypeC,Color> couleurCours = new EnumMap<>(Map.of(TypeC.CM,Color.RED,TypeC.TD,Color.BLUE,TypeC.TP,Color.GREEN));
	private Map<Integer, List<Cours>> coursMap = new HashMap<>();
	private List<StackPane> coursCells = new ArrayList<>();
	
	private Personne p;
	
	public PageEDT(String login) {
		p = getPersonneInfo(login).join();
		System.out.println("Personne récup : " + p + "\n");
		stage = new Stage();
	}
	
	public void show(){
		lundi = LocalDate.now().with(DayOfWeek.MONDAY);
		semaine = new Label("");
		modifLabelSemaine();
		genereEDT();
		sceneEDT = new Scene(genereSceneEDT());
		sceneInfos = new Scene(genereSceneInfos(p.getNom(), p.getPrenom(), p.getMail(), p.getRole().toString()));
		getCoursOfPromo(0);

		stage.setTitle("Page d'accueil");
		stage.setScene(sceneEDT);
		stage.setMaximized(true);
		stage.show();
	}

	private void getCoursOfPromo(int indice){
		Promotion promo = p.getPromos().get(indice);
		coursMap = Outils.getCoursByPromo(promo.getPromoId()).join();
		System.out.println("" + coursMap + "\n");
		majEDT();
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
		ComboBox<String> menuPromo = new ComboBox<>();
		for (Promotion p : p.getPromos()){
			menuPromo.getItems().add(p.getPromoId().toString());
		}
		menuPromo.setValue("Promotion :");
		//menuPromo.setOnAction(e -> );//Metre la fonction de Sh<3wn
		
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
		
		HBox boiteBtn = new HBox(genereBoutonHaut()); 
		
		pageComplete.getChildren().addAll(menuPromo,boiteBtn,grilleEdt,semainesBox);
		VBox.setVgrow(grilleEdt, Priority.ALWAYS);

		// Créer un layout pour les boutons et la grille
		StackPane scene1 = new StackPane();
		scene1.setMinSize(1920, 1080);
		scene1.setPrefSize(1920, 1080);
		scene1.setMaxSize(1920, 1080);
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
			
			GridPane.setHalignment(jourLabel, HPos.CENTER);
			GridPane.setValignment(jourLabel, VPos.CENTER);

			grilleEdt.add(jourLabel, i+1, 0);
		}

		for (int i=0; i<horaires.length;i++){
			Label horaire = new Label(horaires[i]);

			GridPane.setHalignment(horaire, HPos.CENTER);  
			GridPane.setValignment(horaire, VPos.CENTER);
			grilleEdt.add(horaire, 0, i+1);
		}
	}

	private void majEDT(){
		viderEdt();
		modifLabelSemaine();

		for(int i=0; i<5; i++){
			LocalDate jourDate = lundi.plusDays(i);
			String jourNom = jourDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE);
			String moisNom = jourDate.getMonth().getDisplayName(TextStyle.FULL, Locale.FRANCE);
			
			((Label) grilleEdt.getChildren().get(i+1)).setText(jourNom + " " + jourDate.getDayOfMonth() + " " + moisNom);
		}

		ajouteAllCours();
	}

	private void ajouterCours(Cours c){
		Label nom = new Label("Nom UE.."+"\n"+c.getIntervenantLogin()+"\n"+"Salle.."+"\n"+c.getType().toString());
		nom.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		nom.setTextFill(Color.WHITE);

		// Crée un StackPane pour centrer le label dans la cellule
		StackPane cell = new StackPane();
		cell.getChildren().add(nom);
		cell.setAlignment(Pos.CENTER);  
		cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);  
		cell.setBackground(new Background(new BackgroundFill(couleurCours.get(c.getType()), new CornerRadii(0), new Insets(1))));
		// Ajouter la cellule dans la grille

        LocalDate localDate = c.getJour().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int jourSemaine = localDate.get(WeekFields.of(Locale.FRANCE).dayOfWeek());
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

	public CompletableFuture<Personne> getPersonneInfo(String login) {
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("http://localhost:8080/api/v1/personnes/" + login))
			.GET()
			.header("Content-Type", "application/json")
			.build();

		// Retourner un CompletableFuture
		return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			.thenApply(HttpResponse::body)  // Récupère la réponse sous forme de chaîne
			.thenApply(response -> {
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					// Convertit la réponse JSON en objet Personne
					return objectMapper.readValue(response, Personne.class);
				} catch (Exception e) {
					System.err.println("Erreur lors de la conversion JSON: " + e.getMessage()+ "\n");
					return null;
				}
			})
			.exceptionally(e -> {
				e.printStackTrace();
				return null;
			});
	}


	public LocalDate getLundiSemaine(int semaineNum) {
		return LocalDate.of(LocalDate.now().getYear(), 1, 1)
				.with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, semaineNum)
				.with(java.time.DayOfWeek.MONDAY);
	}


	public Pane genereSceneInfos(String nom,String prenom,String mail, String role){
		HBox boiteBtn = new HBox(genereBoutonHaut()); 
		Pane sceneInfos = new Pane();
		VBox boiteInfos = new VBox(20);
	
		Label labelNom = new Label(nom.toUpperCase()+" "+prenom);
		labelNom.setFont(Font.font("Arial",FontWeight.BOLD,25));
		labelNom.setTextFill(Color.BLACK);
		labelNom.setLayoutX(300);
		labelNom.setLayoutY(20);

		Image imageRole = new Image("file:src/main/resources/"+role+".jpg");
		ImageView imageview = new ImageView(imageRole);
		imageview.setFitWidth(200);
		imageview.setFitHeight(200);
		imageview.setLayoutX(20);
		imageview.setLayoutY(30);


		Label labelInfos = new Label("Login : "+p.getLogin()+"\nEmail : "+mail+"\nPromo : "+p.getPromos().get(0).getPromoId());
		labelInfos.setFont(Font.font("Arial",FontWeight.BOLD,25));
		labelInfos.setTextFill(Color.BLACK);
		boiteInfos.getChildren().add(labelInfos);
		boiteInfos.setLayoutX(50);
		boiteInfos.setLayoutY(300);
		sceneInfos.getChildren().addAll(boiteBtn,imageview,labelNom,boiteInfos);
		return sceneInfos;
	}
}
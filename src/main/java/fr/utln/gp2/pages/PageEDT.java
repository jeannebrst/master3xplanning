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
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.time.temporal.IsoFields;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.Calendar; 

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
	private Map<Long, StackPane> coursMap = new HashMap<>();
	private Stage stage;
	private String login;
	private String nom;
	private String prenom;
	private String email;
	private List<Promotion> promo;
	private String role;
	private List<Cours> EDT = new ArrayList<>();
	private Map<TypeC,Color> couleurCours = new EnumMap<>(Map.of(TypeC.CM,Color.RED,TypeC.TD,Color.BLUE,TypeC.TP,Color.GREEN));
	private Scene sceneEDT; 
	private Scene sceneInfos; 
	

	public PageEDT(String login) {
		this.login = login;
		stage = new Stage();
	}

	
	public void show(){
	
		getPersonneInfo().thenCompose(personne -> {
			if (personne == null) return CompletableFuture.completedFuture(null);

			nom = personne.getNom();
			prenom = personne.getPrenom();
			email = personne.getMail();
			role = personne.getRole().toString();
			promo = personne.getPromos();

			// Liste des futures pour récupérer les cours de chaque promo
			List<CompletableFuture<List<Cours>>> futures = promo.stream()
				.map(p -> Outils.getCoursByPromo(p.getPromoId()))
				.collect(Collectors.toList());

			// Attendre que toutes les requêtes se terminent et collecter les résultats
			return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
				.thenApply(v -> futures.stream()
					.map(CompletableFuture::join)  // Récupérer chaque liste de cours
					.flatMap(List::stream)         // Aplatir en une seule liste
					.collect(Collectors.toList())
				);
		}).thenAccept(coursList -> {
			if (coursList != null) {
				EDT.addAll(coursList);
				afficherCours(EDT);  // Maintenant, on affiche après avoir récupéré tous les cours
			}
		});
		

		lundi = LocalDate.now().with(DayOfWeek.MONDAY);
		semaine = new Label("");
		modifLabelSemaine();
		genereEDT();
		sceneEDT =new Scene(genereSceneEDT());
		sceneInfos =new Scene(genereSceneInfos(nom, prenom, email, role));

		stage.setTitle("Page d'accueil");
		stage.setScene(sceneEDT);
		stage.setMaximized(true);
		stage.show();
		
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
		
		pageComplete.getChildren().addAll(boiteBtn,grilleEdt,semainesBox);
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
		modifLabelSemaine();

		for(int i=0; i<5; i++){
			LocalDate jourDate = lundi.plusDays(i);
			String jourNom = jourDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE);
			String moisNom = jourDate.getMonth().getDisplayName(TextStyle.FULL, Locale.FRANCE);
			
			((Label) grilleEdt.getChildren().get(i+1)).setText(jourNom + " " + jourDate.getDayOfMonth() + " " + moisNom);
		}
	}

	public void ajouterCours(String name,String salle,String prof, int jour, int heure, int duree, Color couleur,Long id){

		Label nom = new Label(name+"\n"+prof+"\n"+salle);
		nom.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		nom.setTextFill(Color.WHITE);

		// Crée un StackPane pour centrer le label dans la cellule
		StackPane cell = new StackPane();
		cell.getChildren().add(nom);
		cell.setAlignment(Pos.CENTER);  
		cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);  
		cell.setBackground(new Background(new BackgroundFill(couleur, new CornerRadii(0), new Insets(1))));
		// Ajouter la cellule dans la grille
		grilleEdt.add(cell, jour, heure);
		GridPane.setRowSpan(cell, duree);
		GridPane.setColumnSpan(cell, 1);  

		// Centrer le StackPane dans la cellule de la grille
		GridPane.setHalignment(cell, HPos.CENTER);  
		GridPane.setValignment(cell, VPos.CENTER);  
		coursMap.put(id,cell);
	}

	private void modifLabelSemaine(){
		numSemaine = lundi.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
		semaine.setText("Semaine n°" + numSemaine);
	}
	
	public void supprimerCours(Long id) {
		StackPane cours = coursMap.get(id);  
		if (cours != null) {
			grilleEdt.getChildren().remove(cours);  
			coursMap.remove(id); 
		}
	}

public CompletableFuture<Personne> getPersonneInfo() {
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
                System.err.println("Erreur lors de la conversion JSON: " + e.getMessage());
                return null;  // En cas d'erreur, retourner null
            }
        })
        .exceptionally(e -> {
            e.printStackTrace();
            return null;  // En cas d'erreur de la requête HTTP, retourner null
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


		Label labelInfos = new Label("Login : "+login+"\nEmail : "+mail+"\nPromo : "+promo.get(0).getPromoId());
		labelInfos.setFont(Font.font("Arial",FontWeight.BOLD,25));
		labelInfos.setTextFill(Color.BLACK);
		boiteInfos.getChildren().add(labelInfos);
		boiteInfos.setLayoutX(50);
		boiteInfos.setLayoutY(300);
		sceneInfos.getChildren().addAll(boiteBtn,imageview,labelNom,boiteInfos);
		return sceneInfos;
	}


	public void afficherCours(List<Cours> edt){
		for(Cours c : edt){
			System.out.println(c.getJour().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfWeek().getValue());
			ajouterCours("Maths","112",c.getIntervenantLogin(),c.getJour().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfWeek().getValue(),c.getHeureDebut()-7,c.getDuree(),couleurCours.get(c.getType()),c.getCoursId());
		}
	}
}
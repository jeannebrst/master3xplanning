package fr.utln.gp2.pages;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PageLogin extends Application {
	@Override 
	public void start (Stage primaryStage){

		StackPane root = new StackPane();


		Label erreur = new Label("L'utilisateur ou le mot de passe n'est pas reconnu");
		erreur.setFont(Font.font("Arial",FontWeight.BOLD,25));
		erreur.setTextFill(Color.RED);

		erreur.setVisible(false);

		//Création des labels 
		Label titre = new Label("Bienvenue sur MasterPlanning !");

		Label service = new Label("Service d'authentification");
		titre.setFont(Font.font("Arial",FontWeight.BOLD,30));
		titre.setTextFill(Color.WHITE);
		service.setFont(Font.font("Arial",FontWeight.BOLD,25));
		service.setTextFill(Color.WHITE);
		//Conteneur de l'app
		StackPane conteneurFond = new StackPane();

		//Création du champ login
		TextField champLogin = new TextField();
		champLogin.setPromptText("Utilisateur");
		champLogin.setStyle("-fx-border-color: blue; -fx-font-size: 14px;");
		champLogin.setMaxWidth(300);
		champLogin.setMaxHeight(25);

		//Création du champ mdp
		PasswordField champMdp = new PasswordField();
		champMdp.setPromptText("Mot de passe");
		champMdp.setMaxWidth(300);
		champMdp.setMaxHeight(25);

		// Création d'un bouton
		Button bouton = new Button("Se connecter");
		bouton.setOnAction(e -> {
			if (champMdp.getText().equals("123")){
				openPageAccueil(primaryStage);
			}
			else {
			titre.setVisible(false);
			erreur.setVisible(true);
			champLogin.clear();
			champMdp.clear();
		}


		}
	);
		
		//Création de l'image en fond
		Image fond = new Image("file:src/main/resources/fond2.jpg");
		ImageView imageview = new ImageView(fond);
		imageview.setFitWidth(800);
		imageview.setFitHeight(600);

		
		

		//Vbox pour les elements
		VBox boite = new VBox(20);
		boite.setAlignment(Pos.CENTER);
		boite.getChildren().addAll(titre,erreur,service,champLogin,champMdp, bouton);
		conteneurFond.getChildren().addAll(imageview,boite);


		
		//root.getChildren().addAll(conteneurTitre,conteneurFond);

		root.getChildren().add(conteneurFond);
		
		
		
		
		Scene scene1 = new Scene(root, 800, 600);
		
		

		// Configuration de la fenêtre
		primaryStage.setTitle("Page de login");
		primaryStage.setScene(scene1);


		// primaryStage.setWidth(1920);
		// primaryStage.setHeight(1080);
		primaryStage.setMaximized(false);
		primaryStage.show();
	}

	

	public void openPageAccueil(Stage stage) {
		Platform.runLater(() -> {
			stage.close(); // Ferme la fenêtre actuelle
			
			// Ouvre la page EDT
			PageEDT pageEDT = new PageEDT();
			pageEDT.show();
		});
	}
	
	
}
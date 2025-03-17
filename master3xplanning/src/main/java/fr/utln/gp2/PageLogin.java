package fr.utln.gp2;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PageLogin extends Application {
	@Override 
	public void start (Stage primaryStage){

		//Création des labels 
		Label titre = new Label("Bienvenue sur MasterPlanning !");
		Label service = new Label("Service d'authentification");
		titre.setFont(Font.font("Arial",FontWeight.BOLD,30));
		service.setFont(Font.font("Arial",FontWeight.BOLD,25));

		VBox conteneurTitre = new VBox();
		//conteneurTitre.setAlignment(Pos.TOP_CENTER);
		conteneurTitre.getChildren().add(titre);
		conteneurTitre.setLayoutX(200);
		conteneurTitre.setLayoutY(50);
		//Création du champ login
		TextField champLogin = new TextField();
		champLogin.setPromptText("Utilisateur");
		champLogin.setStyle("-fx-border-color: blue; -fx-font-size: 14px;");
		champLogin.setMaxWidth(300);
		champLogin.setMaxHeight(25);

		//Création du champ mdp
		TextField champMdp = new TextField();
		champMdp.setPromptText("Mot de passe");
		champMdp.setMaxWidth(300);
		champMdp.setMaxHeight(25);

		// Création d'un bouton
		Button bouton = new Button("Se connecter");
		bouton.setOnAction(e -> {
			System.out.println("login : "+champLogin.getText()+"\nmdp : "+champMdp.getText());
			champLogin.clear();
			champMdp.clear();
		}
	);
		
		//Création de l'image en fond
		Image fond = new Image("file:src/main/resources/fond.jpg");
		ImageView imageview = new ImageView(fond);
		imageview.setFitWidth(800);
		imageview.setFitHeight(600);

		//Conteneur de l'app
		StackPane conteneurFond = new StackPane();
		

		//Vbox pour les elements
		VBox boite = new VBox(20);
		boite.setAlignment(Pos.CENTER);
		boite.getChildren().addAll(service,champLogin,champMdp, bouton);
		conteneurFond.getChildren().addAll(imageview,boite);


		Pane root = new Pane();
		//root.getChildren().addAll(conteneurTitre,conteneurFond);
		root.getChildren().add(conteneurFond);
		root.getChildren().add(conteneurTitre);
		
		Scene scene = new Scene(root, 800, 600);
		
		

		// Configuration de la fenêtre
		primaryStage.setTitle("Page de login");
		primaryStage.setScene(scene);


		// primaryStage.setWidth(1920);
		// primaryStage.setHeight(1080);
		primaryStage.setMaximized(false);
		primaryStage.show();
	}

	
	public static void main(String[] args) {
		launch(args);
		
	}
}

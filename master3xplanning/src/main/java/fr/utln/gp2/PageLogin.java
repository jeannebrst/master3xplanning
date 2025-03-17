package fr.utln.gp2;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import javafx.scene.control.TextField;
import javafx.geometry.Pos;

public class PageLogin extends Application {
	@Override 
	public void start (Stage primaryStage){

		TextField champLogin = new TextField();
		champLogin.setPromptText("Utilisateur");
		champLogin.setStyle("-fx-border-color: blue; -fx-font-size: 14px;");
		champLogin.setMaxWidth(200);
		champLogin.setMaxHeight(25);
		TextField champMdp = new TextField();
		champMdp.setPromptText("Mot de passe");
		champMdp.setMaxWidth(200);
		champMdp.setMaxHeight(25);
		// Création d'un bouton
		Button bouton = new Button("Se connecter");
		bouton.setOnAction(e -> System.out.println("login : "+champLogin.getText()+"\nmdp : "+champMdp.getText()));
		
		Image fond = new Image("file:src/main/resources/fond.jpg");
		ImageView imageview = new ImageView(fond);
		imageview.setFitWidth(800);
		imageview.setFitHeight(600);

		StackPane conteneurFond = new StackPane();
		conteneurFond.getChildren().add(imageview);

		VBox root = new VBox(10);
		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(champLogin,champMdp, bouton);
		Scene scene = new Scene(conteneurFond, 800, 600);
		conteneurFond.getChildren().add(root);
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

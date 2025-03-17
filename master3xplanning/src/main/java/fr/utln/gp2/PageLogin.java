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

public class PageLogin extends Application {
	@Override 
	public void start (Stage primaryStage){

		TextField champLogin = new TextField();
		champLogin.setPromptText("utilisateur");
		champLogin.setStyle("-fx-border-color: blue; -fx-font-size: 14px;");
		TextField champMdp = new TextField();
		champMdp.setPromptText("mot de passe");
		// Création d'un bouton
		Button bouton = new Button("Valider");
		bouton.setOnAction(e -> System.out.println("login : "+champLogin.getText()+"\nmdp : "+champMdp.getText()));
		
		// Ajout du bouton dans une StackPane (layout)
		// StackPane root = new StackPane(button);
		VBox root = new VBox(10);
		root.getChildren().addAll(champLogin,champMdp, bouton);
		Scene scene = new Scene(root, 400, 300);
		
		// Configuration de la fenêtre
		primaryStage.setTitle("Page de login");
		primaryStage.setScene(scene);
		// primaryStage.setWidth(1920);
		// primaryStage.setHeight(1080);
		primaryStage.setMaximized(true);
		primaryStage.show();
	}

	
	public static void main(String[] args) {
		launch(args);
		
	}
}

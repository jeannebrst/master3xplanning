package fr.utln.gp2.pages;

import fr.utln.gp2.utils.AuthDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import org.apache.commons.codec.digest.DigestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PageLogin extends Application {
	private static final HttpClient client = HttpClient.newHttpClient();

	@Override 
	public void start (Stage primaryStage){
		StackPane root = new StackPane();

		//Creation des labels 
		String font = "Arial";
		Label erreur = new Label("L'utilisateur ou le mot de passe n'est pas reconnu");
		erreur.setFont(Font.font(font,FontWeight.BOLD,25));
		erreur.setTextFill(Color.RED);
		erreur.setVisible(false);

		Label titre = new Label("Bienvenue sur MasterPlanning !");
		Label service = new Label("Service d'authentification");
		titre.setFont(Font.font(font,FontWeight.BOLD,30));
		titre.setTextFill(Color.WHITE);
		service.setFont(Font.font(font,FontWeight.BOLD,25));
		service.setTextFill(Color.WHITE);

		//Conteneur de l'app
		StackPane conteneurFond = new StackPane();

		//Creation champ login
		TextField champLogin = new TextField();
		champLogin.setPromptText("Utilisateur");
		champLogin.setStyle("-fx-border-color: blue; -fx-font-size: 14px;");
		champLogin.setMaxWidth(300);
		champLogin.setMaxHeight(25);

		//Creation champ mdp
		PasswordField champMdp = new PasswordField();
		champMdp.setPromptText("Mot de passe");
		champMdp.setMaxWidth(300);
		champMdp.setMaxHeight(25);

		TextField textField = new TextField();
		textField.setMaxWidth(300);
		textField.setMaxHeight(25);
		textField.setManaged(false);
		textField.setVisible(false);

		ToggleButton toggleButton = new ToggleButton("Voir MDP");
		toggleButton.setOnAction(e -> {
			if (toggleButton.isSelected()) {
				textField.setText(champMdp.getText());
				textField.setManaged(true);
				textField.setVisible(true);
				champMdp.setManaged(false);
				champMdp.setVisible(false);
			} else {
				champMdp.setText(textField.getText());
				champMdp.setManaged(true);
				champMdp.setVisible(true);
				textField.setManaged(false);
				textField.setVisible(false);
			}
		});

		champMdp.textProperty().bindBidirectional(textField.textProperty());

		//Creation bouton
		Button bouton = new Button("Se connecter");
		bouton.setOnAction(e -> {
			String hashMdp = DigestUtils.sha256Hex(champMdp.getText());
			AuthDTO auth = new AuthDTO(champLogin.getText(), hashMdp);

			try{
			String authString = new ObjectMapper().writeValueAsString(auth);
			HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:8080/api/v1/personnes/auth"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(authString))
				.build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println("Authentification : " + response.statusCode());
			if(response.statusCode() == 200){
				openPageAccueil(primaryStage);
			}
			else{
				erreur.setVisible(true);
				champLogin.clear();
				champMdp.clear();
			}
			}catch(IOException | InterruptedException err){
				System.out.println("Erreur http authentification" + err);
			}
		}
	);
		
		//Creation de l'image en fond
		Image fond = new Image("file:src/main/resources/fond2.jpg");
		ImageView imageview = new ImageView(fond);
		imageview.setFitWidth(800);
		imageview.setFitHeight(600);

		//Vbox pour les elements
		VBox boite = new VBox(20);
		boite.setAlignment(Pos.CENTER);
		HBox mdpChiffre = new HBox(10);
		mdpChiffre.getChildren().addAll(champMdp, toggleButton);
		HBox mdpClair = new HBox(10);
		mdpClair.getChildren().addAll(textField, toggleButton);
		mdpChiffre.setAlignment(Pos.CENTER);
		mdpClair.setAlignment(Pos.CENTER);
		boite.getChildren().addAll(titre,erreur,service,champLogin,mdpChiffre, mdpClair, bouton);
		conteneurFond.getChildren().addAll(imageview,boite);
		root.getChildren().add(conteneurFond);
		
		Scene scene1 = new Scene(root, 800, 600);

		primaryStage.setTitle("Page de login");
		primaryStage.setScene(scene1);


		// primaryStage.setWidth(1920);
		// primaryStage.setHeight(1080);
		primaryStage.setMaximized(false);
		primaryStage.show();
	}

	public void openPageAccueil(Stage stage) {
		Platform.runLater(() -> {
			stage.close(); //Ferme la fenêtre actuelle
			
			//Ouvre la page EDT
			PageEDT pageEDT = new PageEDT();
			pageEDT.show();
		});
	}
}
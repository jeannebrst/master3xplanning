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
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

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


		Text texte1 = new Text("Bienvenue ");
		texte1.setFont(Font.font(font,FontWeight.BOLD,30));
		texte1.setFill(Color.RED);
		Text texte6 = new Text("sur ");
		texte6.setFont(Font.font(font,FontWeight.BOLD,30));
		texte6.setFill(Color.ORANGE);
		Text texte2 = new Text("Master");
		texte2.setFont(Font.font(font,FontWeight.BOLD,30));
		texte2.setFill(Color.YELLOW);
		Text texte3 = new Text("Master");
		texte3.setFont(Font.font(font,FontWeight.BOLD,30));
		texte3.setFill(Color.GREEN);
		Text texte4 = new Text("Master");
		texte4.setFont(Font.font(font,FontWeight.BOLD,30));
		texte4.setFill(Color.INDIGO);
		Text texte5 = new Text("Planning");
		texte5.setFont(Font.font(font,FontWeight.BOLD,30));
		texte5.setFill(Color.PURPLE);
		TextFlow titre = new TextFlow(texte1,texte6,texte2,texte3,texte4,texte5);
		titre.setTextAlignment(TextAlignment.CENTER);
		Label service = new Label("Service d'authentification");
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

		TextField champMdpClair = new TextField();
		champMdpClair.setMaxWidth(300);
		champMdpClair.setMaxHeight(25);
		champMdpClair.setManaged(false);
		champMdpClair.setVisible(false);

		ToggleButton toggleButton = new ToggleButton("Voir MDP");
		toggleButton.setOnAction(e -> {
			boolean voirMdp = toggleButton.isSelected();
			champMdp.setManaged(!voirMdp);
			champMdp.setVisible(!voirMdp);
			champMdpClair.setManaged(voirMdp);
			champMdpClair.setVisible(voirMdp);
		});

		champMdp.textProperty().bindBidirectional(champMdpClair.textProperty());

		//Creation bouton
		Button bouton = new Button("Se connecter");
		bouton.setOnAction(e -> authentification(champLogin, champMdp, erreur, primaryStage));
		
		//Creation de l'image en fond
		Image fond = new Image("file:src/main/resources/fond2.jpg");
		ImageView imageview = new ImageView(fond);
		imageview.setFitWidth(1920);
		imageview.setFitHeight(1080);

		//Vbox pour les elements
		VBox boite = new VBox(20);
		boite.setAlignment(Pos.CENTER);
		HBox mdpChiffre = new HBox(10);
		mdpChiffre.getChildren().addAll(champMdp,champMdpClair, toggleButton);
		mdpChiffre.setAlignment(Pos.CENTER);
		boite.getChildren().addAll(titre,erreur,service,champLogin,mdpChiffre,bouton);
		conteneurFond.getChildren().addAll(imageview,boite);
		root.getChildren().add(conteneurFond);
		
		Scene scene1 = new Scene(root, 1920, 1080);

		primaryStage.setTitle("Page de login");
		primaryStage.setScene(scene1);

		primaryStage.setMaximized(false);
		primaryStage.show();
	}

	public void openPageAccueil(Stage stage,String login) {
		Platform.runLater(() -> {
			stage.close(); //Ferme la fenêtre actuelle
			
			//Ouvre la page EDT
			PageEDT pageEDT = new PageEDT(login);
			pageEDT.show();
		});
	}

	private void authentification(TextField champLogin, PasswordField champMdp, Label erreur, Stage primaryStage){
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
		System.out.println("Authentification : " + response.statusCode() + "\n");
		if(response.statusCode() == 200){
			openPageAccueil(primaryStage,champLogin.getText());
		}
		else{
			erreur.setVisible(true);
			champLogin.clear();
			champMdp.clear();
		}
		}catch(IOException | InterruptedException err){
			System.out.println("Erreur http authentification" + err + "\n");
		}
	}
}
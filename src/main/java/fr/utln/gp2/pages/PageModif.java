package fr.utln.gp2.pages;

import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.utils.Outils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.utln.gp2.entites.Cours;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PageModif {

    private Stage stage;
    private Personne p;
    private Map<Integer, List<Cours>> cours;
    private int numSemaine;

    public PageModif(Personne p, Map<Integer, List<Cours>> cours){
        stage = new Stage();
        this.p = p;
        this.cours = cours;
    }

    public void show(){
        stage.setTitle("Page de modification");
		stage.setMaximized(false);



        stage.setScene(generePage());
		stage.show();
    }

    private Scene generePage(){
        ComboBox<String> ueComboBox = new ComboBox<>();
        ueComboBox.getItems().addAll();

        // 📌 Liste des profs (associée aux UE)
        Map<String, List<String>> profsParUE = new HashMap<>();
        profsParUE.put("Mathématiques", List.of("Prof. Dupont", "Prof. Martin"));
        profsParUE.put("Informatique", List.of("Prof. Durand", "Prof. Petit"));
        profsParUE.put("Physique", List.of("Prof. Leroy", "Prof. Bernard"));

        // 📌 ComboBox pour les professeurs
        ComboBox<String> profComboBox = new ComboBox<>();
        profComboBox.setDisable(true); // Désactivé au début

        // 📌 Événement : Mettre à jour les profs en fonction de l'UE choisie
        ueComboBox.setOnAction(event -> {
            String ueChoisie = ueComboBox.getValue();
            if (ueChoisie != null) {
                profComboBox.getItems().setAll(profsParUE.get(ueChoisie));
                profComboBox.setDisable(false);
            }
        });

        // 📌 Bouton pour valider le choix
        Button validerButton = new Button("Valider");
        validerButton.setOnAction(event -> {
            String ueChoisie = ueComboBox.getValue();
            String profChoisi = profComboBox.getValue();
            if (ueChoisie != null && profChoisi != null) {
                System.out.println("UE sélectionnée : " + ueChoisie);
                System.out.println("Professeur sélectionné : " + profChoisi);
            }
        });

        // 📌 Mise en page
        VBox layout = new VBox(10, ueComboBox, profComboBox, validerButton);
        Pane pane = new Pane();
        pane.getChildren().add(layout);
        Scene scene = new Scene(pane);
        return scene;
    }

    private void ajouteUnCours(String intervenant){
        Map<Integer, List<Cours>> coursIntervenant = Outils.getCoursByIntervenant(intervenant).join();
        for (Cours c : coursIntervenant.get(numSemaine)){
            
        }
        Cours c = new Cours()
    }
}

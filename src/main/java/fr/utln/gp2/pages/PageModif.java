package fr.utln.gp2.pages;

import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.UE;
import fr.utln.gp2.entites.Cours.TypeC;
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

    public PageModif(Personne p, Map<Integer, List<Cours>> cours,int numSemaine){
        stage = new Stage();
        this.p = p;
        this.cours = cours;
        this.numSemaine=numSemaine;
    }

    public void show(){
        stage.setTitle("Page de modification");
		stage.setMaximized(false);
        stage.setScene(generePage());
		stage.show();
    }

    public Scene generePage(){


        ComboBox<String> ueComboBox = new ComboBox<>();
        for (UE ue : Outils.getAllUE().join()){
            ueComboBox.getItems().add(ue.getNom());
        }
        ueComboBox.setValue("UE");
        
        
        
        ComboBox<String> profComboBox = new ComboBox<>();
        profComboBox.setDisable(true); // Désactivé au début
        profComboBox.setValue("Professeur");

        ueComboBox.setOnAction(event -> {
            String ueChoisie = ueComboBox.getValue();
            if (ueChoisie != null) {
                profComboBox.getItems().setAll();
                profComboBox.setDisable(false);
            }
        });

        ComboBox<String> typeMenuDeroulant = new ComboBox<>();
        typeMenuDeroulant.getItems().addAll(TypeC.CM.toString(),TypeC.TD.toString(),TypeC.TP.toString());
        typeMenuDeroulant.setValue("Type de Cours");

        ComboBox<String> dureeMenuDeroulant = new ComboBox<>();
        dureeMenuDeroulant.getItems().addAll("1","2","3","4");
        dureeMenuDeroulant.setValue("Durée du cours");

        Button validerButton = new Button("Valider");
        validerButton.setOnAction(event -> {
            String ueChoisie = ueComboBox.getValue();
            String profChoisi = profComboBox.getValue();
            if (ueChoisie != null && profChoisi != null) {
                System.out.println("UE sélectionnée : " + ueChoisie);
                System.out.println("Professeur sélectionné : " + profChoisi);
            }
        });

        
        VBox layout = new VBox(10, ueComboBox, profComboBox,typeMenuDeroulant,dureeMenuDeroulant, validerButton);
        Pane pane = new Pane();
        pane.getChildren().add(layout);
        Scene scene = new Scene(pane);
        return scene;
    }
}

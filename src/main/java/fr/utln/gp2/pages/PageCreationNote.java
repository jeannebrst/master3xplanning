package fr.utln.gp2.pages;

import fr.utln.gp2.entites.Note;
import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.UE;
import fr.utln.gp2.utils.Outils;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import fr.utln.gp2.utils.NoteDTO;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class PageCreationNote {

    private final Stage stage = new Stage();
    private List<UE> ues;
    private Personne etudiant;

    public PageCreationNote(Personne etudiant) {
        this.etudiant = etudiant;
    }

    public void show() {
        
        ues = Outils.getAllUE().join();

        stage.setTitle("Ajout d'une Note");
        stage.setScene(generePage());
        stage.setMinWidth(400);
        stage.setMinHeight(350);
        stage.show();
    }

    private Scene generePage() {

        ComboBox<String> ueComboBox = new ComboBox<>();
        for (UE ue : ues) {
            ueComboBox.getItems().add(ue.getNom());
        }
        ueComboBox.setValue("UE");

        TextField noteField = new TextField();
        noteField.setPromptText("Note");

        DatePicker datePicker = new DatePicker(LocalDate.now());

        Label messageErreur = new Label();
        messageErreur.setStyle("-fx-text-fill: red;");

        Button validerButton = new Button("Valider");
        validerButton.setOnAction(event -> {
            try {
                UE ue = ues.get(ueComboBox.getSelectionModel().getSelectedIndex());
                float note = Float.parseFloat(noteField.getText());
                LocalDate localDate = datePicker.getValue();
                Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

                String login = etudiant.getLogin();
                

                NoteDTO nouvelleNote = new NoteDTO(login,ue.getNom(),note,date);
                Outils.persistence(nouvelleNote); // à adapter selon ton système de persistence

                stage.close();
            } catch (Exception e) {
                messageErreur.setText("Erreur : " + e.getMessage());
            }
        });

        VBox layout = new VBox(10, ueComboBox, noteField, datePicker, validerButton, messageErreur);
        Pane pane = new Pane();
        pane.getChildren().add(layout);
        return new Scene(pane);
    }
    public Stage getStage() {
        return stage;
    }
}

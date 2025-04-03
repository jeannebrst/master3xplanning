package fr.utln.gp2.pages;

import fr.utln.gp2.entites.Personne;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PageModif {
    
    private Stage stage;

    public PageModif(Personne p){
        stage = new Stage();
        show();
    }

    private void show(){
        stage.setTitle("Page de modification");
		stage.setMaximized(false);
		stage.show();
    }

    private void ajouteCours(){

    }
}

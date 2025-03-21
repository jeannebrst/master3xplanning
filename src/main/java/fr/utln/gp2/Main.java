package fr.utln.gp2;

import fr.utln.gp2.pages.PageEDT;
import fr.utln.gp2.pages.PageLogin;
import javafx.stage.Stage;
import javafx.application.Application;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Personne.Role;
import fr.utln.gp2.ressources.PersonneRessource;

import java.io.IOException;

public class Main{

    public static void main(String[] args) {
        Personne p1 = new Personne("MDP", "Pelerin", "Shawn", "mail", Role.ETUDIANT);
		Personne p2 = new Personne("virgule", "labit", "Quentin", "mail", Role.ETUDIANT);

		p1.creation();
		p2.creation(); 
        Application.launch(PageLogin.class, args);


    }


}

package fr.utln.gp2;

import fr.utln.gp2.pages.PageLogin;
import javafx.application.Application;
import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Personne.Role;

public class Main{

	public static void main(String[] args) {
		Personne p1 = new Personne("MDP", "Pelerin", "Shawn", "mail", Role.ETUDIANT);
		Personne p2 = new Personne("virgule", "labit", "Quentin", "mail", Role.ETUDIANT);

		p1.creation();
		p2.creation(); 
		Application.launch(PageLogin.class, args);
	}


}

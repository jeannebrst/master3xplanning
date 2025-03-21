package fr.utln.gp2;

import fr.utln.gp2.pages.PageLogin;
import fr.utln.gp2.utils.PromotionId;
import fr.utln.gp2.utils.PromotionId.Type;
import javafx.application.Application;
import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Promotion;
import fr.utln.gp2.entites.Personne.Role;

public class Main{
	public static void main(String[] args) {
		PromotionId m1infoId = new PromotionId(Type.MASTER, 1, "Informatique");
		Promotion m1info = new Promotion(m1infoId, null, "spelerin", null);
		Personne p1 = new Personne("MotDePasse", "Pelerin", "Shawn", Role.ETUDIANT);
		Personne p2 = new Personne("virgule", "labit", "Quentin", Role.ETUDIANT);
		Personne p3 = new Personne("toulon", "Pelerin", "Jeanne", Role.ETUDIANT);
		// p3.getPromos().add(m1info);

		p1.creation();
		p2.creation();
		p3.creation();

		Application.launch(PageLogin.class, args);
	}
}

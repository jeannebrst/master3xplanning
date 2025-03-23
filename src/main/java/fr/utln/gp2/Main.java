package fr.utln.gp2;

import fr.utln.gp2.pages.PageLogin;
import fr.utln.gp2.utils.Outils;
import fr.utln.gp2.utils.PromotionId.Type;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import fr.utln.gp2.entites.Cours;
import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Promotion;
import fr.utln.gp2.entites.Personne.Role;
import fr.utln.gp2.entites.Cours.TypeC;

public class Main{
	public static void main(String[] args) {
		Promotion m1info = new Promotion(Type.MASTER, 1, "Informatique", null, "spelerin", null);
		Cours c1 = new Cours(Arrays.asList(m1info), "spelerin", 14f, 2f, new Date(), TypeC.TP);
		Cours c2 = Cours.builder()
			.intervenantLogin("spelerin")
			.heureDebut(16f)
			.duree(2f)
			.jour(new Date())
			.type(TypeC.CM)
			.build();
		// c2.getPromos().add(m1info);

		Personne p1 = new Personne("MotDePasse", "Pelerin", "Shawn", Role.ETUDIANT);
		Personne p2 = new Personne("virgule", "labit", "Quentin", Role.ETUDIANT);
		p2.getPromos().add(m1info);
		Personne p3 = new Personne("toulon", "Pelerin", "Jeanne", Role.ETUDIANT, Arrays.asList(m1info));
		
		Outils.persistence(p1);
		Outils.persistence(m1info);
		Outils.persistence(p2);
		Outils.persistence(p3); 
		Outils.persistence(c1);
		Outils.persistence(c2);
		System.out.println(m1info.getCours().size());
		Application.launch(PageLogin.class, args);
	}
}

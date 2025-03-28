package fr.utln.gp2;

import fr.utln.gp2.entites.UE;
import fr.utln.gp2.pages.PageLogin;
import fr.utln.gp2.utils.Outils;
import fr.utln.gp2.utils.PromotionId.Type;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import fr.utln.gp2.entites.Cours;
import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Promotion;
import fr.utln.gp2.entites.Personne.Role;
import fr.utln.gp2.entites.Cours.TypeC;

public class Main{
	public static void main(String[] args){

		Personne p1 = new Personne("MotDePasse", "Pelerin", "Shawn", Role.ETUDIANT);
		Outils.persistence(p1);

		UE optimisation = UE.builder()
				.nom("Optimisation")
				.responsableLogin("spelerin")
				.nbHeures(45)
				.build();
		System.out.println(optimisation.getCours());


		Promotion m1info = new Promotion(Type.MASTER, 1, "Informatique", null, "spelerin", null);
		System.out.println(m1info.getPromoId());
		Outils.persistence(m1info);

		Cours c1 = new Cours(Arrays.asList(m1info), "spelerin", 14, 2, new Date(), TypeC.TP);
		optimisation.getCours().add(c1);
		Outils.persistence(optimisation);
//		Outils.persistence(c1);

		Cours c2 = Cours.builder()
			.promos(Arrays.asList(m1info))
			.ues(optimisation)
			.intervenantLogin("spelerin")
			.heureDebut(16)
			.duree(2)
			.jour(new Date())
			.type(TypeC.CM)
			.build();

		// c2.getPromos().add(m1info);
		System.out.println(m1info.getCours());
//		m1info.getCours().add(c2);
//		System.out.println(c2);
//		System.out.println(m1info);

		Personne p2 = new Personne("virgule", "labit", "Quentin", Role.ETUDIANT);
		p2.getPromos().add(m1info);
		Personne p3 = new Personne("toulon", "Pelerin", "Jeanne", Role.ETUDIANT, Arrays.asList(m1info));
		Personne p4 = new Personne("123", "Bah", "Tot", Role.PROFESSEUR, Arrays.asList(m1info));





		Outils.persistence(p2);
		Outils.persistence(p3);
		Outils.persistence(p4);
		// Outils.persistence(c2);
		CompletableFuture<List<Cours>> coursFuture = Outils.getCoursByPromo(m1info.getPromoId());
		List<Cours> coursList = coursFuture.join();
		System.out.println(coursList);
		Application.launch(PageLogin.class, args);
	}

}
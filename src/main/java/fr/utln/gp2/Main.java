package fr.utln.gp2;

import fr.utln.gp2.pages.PageLogin;
import fr.utln.gp2.utils.Outils;
import fr.utln.gp2.utils.PromotionId.Type;
import javafx.application.Application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import fr.utln.gp2.entites.Cours;
import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Promotion;
import fr.utln.gp2.entites.Personne.Role;
import fr.utln.gp2.entites.Cours.TypeC;

public class Main{
	public static void main(String[] args){
		Promotion m1info = new Promotion(Type.MASTER, 1, "Informatique", null, "spelerin", null);
		System.out.println(m1info.getPromoId());
		Cours c1 = new Cours(Arrays.asList(m1info), "spelerin", 14, 2, new Date(), TypeC.TP);
		Cours c2 = Cours.builder()
			.promos(Arrays.asList(m1info))
			.intervenantLogin("spelerin")
			.heureDebut(16)
			.duree(2)
			.jour(new Date())
			.type(TypeC.CM)
			.build();
		// c2.getPromos().add(m1info);
		System.out.println(m1info.getCours());
		m1info.getCours().add(c1);
		m1info.getCours().add(c2);
		System.out.println(c2);
		System.out.println(m1info);

		Personne p1 = new Personne("MotDePasse", "Pelerin", "Shawn", Role.ETUDIANT);
		Personne p2 = new Personne("virgule", "labit", "Quentin", Role.ETUDIANT);
		p2.getPromos().add(m1info);
		Personne p3 = new Personne("toulon", "Pelerin", "Jeanne", Role.ETUDIANT, Arrays.asList(m1info));
		Personne p4 = new Personne("123", "Bah", "Tot", Role.PROFESSEUR, Arrays.asList(m1info));


		Outils.persistence(p1);
		Outils.persistence(m1info);
		Outils.persistence(p2);
		Outils.persistence(p3); 
		Outils.persistence(p4);
		// Outils.persistence(c1);
		// Outils.persistence(c2);
		CompletableFuture<Map<Integer, List<Cours>>> futureCoursMap = Outils.getCoursByPromo(m1info.getPromoId());
		Map<Integer, List<Cours>> coursMap = futureCoursMap.exceptionally(e -> {
			System.err.println("Erreur lors de la récupération des cours : " + e.getMessage());
			return Map.of();
		}).join();
		System.out.println(coursMap);
		Application.launch(PageLogin.class, args);
	}
}
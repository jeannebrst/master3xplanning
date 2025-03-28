package fr.utln.gp2;

import fr.utln.gp2.pages.PageLogin;
import fr.utln.gp2.utils.Outils;
import fr.utln.gp2.utils.PromotionId.Type;
import javafx.application.Application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
		// List<Cours> cours = new ArrayList<>();
		
		Promotion m1info = new Promotion(Type.MASTER, 1, "Informatique", null, "spelerin", null);
		Promotion l1info = new Promotion(Type.LICENCE,1,"Informatique",null,"labit",null);

		Cours c1 = new Cours(Arrays.asList(m1info), "spelerin", 14, 2, Date.from(LocalDate.of(2025, 3, 24).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TP);
		Cours c2 = new Cours(Arrays.asList(m1info), "spelerin", 11, 1, Date.from(LocalDate.of(2025, 3, 25).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.CM);
		Cours c3 = new Cours(Arrays.asList(m1info), "spelerin", 13, 3, Date.from(LocalDate.of(2025, 3, 26).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TD);
		Cours c4 = new Cours(Arrays.asList(m1info), "spelerin", 14, 2, Date.from(LocalDate.of(2025, 3, 27).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TD);
		Cours c5 = new Cours(Arrays.asList(m1info), "spelerin", 14, 2, Date.from(LocalDate.of(2025, 3, 28).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TP);
		Cours c6 = new Cours(Arrays.asList(m1info), "spelerin", 14, 4, Date.from(LocalDate.of(2025, 3, 17).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TP);
		Cours c7 = new Cours(Arrays.asList(m1info), "spelerin", 10, 2, Date.from(LocalDate.of(2025, 3, 18).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.CM);
		Cours c8 = new Cours(Arrays.asList(m1info), "spelerin", 14, 2, Date.from(LocalDate.of(2025, 3, 20).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TD);
		Cours c9 = new Cours(Arrays.asList(m1info), "spelerin", 14, 2, Date.from(LocalDate.of(2025, 3, 21).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TD);
		// Cours c2 = Cours.builder()
		// 	.promos(Arrays.asList(m1info))
		// 	.intervenantLogin("spelerin")
		// 	.heureDebut(16)
		// 	.duree(2)
		// 	.jour(new Date())
		// 	.type(TypeC.CM)
		// 	.build();
			
		m1info.setCours(Arrays.asList(c1,c2,c3,c4,c5,c6,c7,c8,c9));

		Personne p1 = new Personne("MotDePasse", "Pelerin", "Shawn", Role.ETUDIANT);
		Personne p2 = new Personne("virgule", "labit", "Quentin", Role.ETUDIANT);
		p2.getPromos().add(m1info);
		Personne p3 = new Personne("toulon", "Pelerin", "Jeanne", Role.ETUDIANT, Arrays.asList(m1info));
		Personne p4 = new Personne("123", "Bah", "Tot", Role.PROFESSEUR, Arrays.asList(m1info));


		Outils.persistence(p1);
		Outils.persistence(m1info);
		Outils.persistence(p2);
		Outils.persistence(l1info);
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
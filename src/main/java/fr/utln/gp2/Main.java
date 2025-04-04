package fr.utln.gp2;

import fr.utln.gp2.entites.UE;
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
import fr.utln.gp2.entites.Salle;
import fr.utln.gp2.entites.Personne.Role;
import fr.utln.gp2.entites.Cours.TypeC;

public class Main{
	public static void main(String[] args){
		Promotion m1info = new Promotion(Type.MASTER, 1, "Informatique", null, "spelerin", null);
		Promotion l1info = new Promotion(Type.LICENCE,1,"Informatique",null,"qlavit",null);
		Promotion m2glotin = new Promotion(Type.MASTER,2,"Baleine",null,"hglotin",null);

		UE optimisation = UE.builder()
				.nom("Optimisation")
				.responsableLogin("spelerin")
				.intervenantsLogin(Arrays.asList("hglotin"))
				.nbHeures(45)
				.build();

		Cours c1 = new Cours(optimisation, Arrays.asList(m1info), "spelerin", 14, 2, Date.from(LocalDate.of(2025, 3, 24).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TP);
		Cours c2 = new Cours(optimisation, Arrays.asList(m1info), "hglotin", 11, 1, Date.from(LocalDate.of(2025, 3, 25).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.CM);
		Cours c3 = new Cours(optimisation, Arrays.asList(m1info), "spelerin", 13, 3, Date.from(LocalDate.of(2025, 3, 26).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TD);
		Cours c4 = new Cours(optimisation, Arrays.asList(m1info), "hglotin", 14, 2, Date.from(LocalDate.of(2025, 3, 27).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TD);
		Cours c5 = new Cours(optimisation, Arrays.asList(m1info), "spelerin", 14, 2, Date.from(LocalDate.of(2025, 3, 28).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TP);
		Cours c6 = new Cours(optimisation, Arrays.asList(m1info), "hglotin", 14, 4, Date.from(LocalDate.of(2025, 3, 17).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TP);
		Cours c7 = new Cours(optimisation, Arrays.asList(m1info), "spelerin", 10, 2, Date.from(LocalDate.of(2025, 3, 18).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.CM);
		Cours c8 = new Cours(optimisation, Arrays.asList(m1info), "spelerin", 14, 2, Date.from(LocalDate.of(2025, 3, 20).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TD);
		Cours c9 = new Cours(optimisation, Arrays.asList(l1info), "spelerin", 14, 2, Date.from(LocalDate.of(2025, 3, 21).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TD);

		// Salle s1 = new Salle("U001", null, 32, "Salle info tah les ouf");

		Personne p1 = new Personne("MotDePasse", "Pelerin", "Shawn", Role.ETUDIANT);
		Personne p2 = new Personne("virgule", "lavit", "Quentin", Role.ETUDIANT);
		p2.getPromos().add(m1info);
		Personne p3 = new Personne("toulon", "Pelerin", "Jeanne", Role.ETUDIANT, Arrays.asList(m1info,l1info));
		Personne p4 = new Personne("123", "Bah", "Tot", Role.PROFESSEUR, Arrays.asList(m1info));
		Personne p5 = new Personne("0","Via","Thierry",Role.GESTIONNAIRE);
		Personne p6 = new Personne("baleine","Glotin","Hervé",Role.PROFESSEUR);

		Outils.persistence(p1);
		Outils.persistence(optimisation);
		Outils.persistence(m1info);
		Outils.persistence(m2glotin);
		Outils.persistence(l1info);
		Outils.persistence(p2);
		Outils.persistence(p3); 
		Outils.persistence(p4);
		Outils.persistence(p5);
		Outils.persistence(p6);
		Outils.persistence(c1);
		Outils.persistence(c2);
		Outils.persistence(c3);
		Outils.persistence(c4);
		Outils.persistence(c5);
		Outils.persistence(c6);
		Outils.persistence(c7);
		Outils.persistence(c8);
		Outils.persistence(c9);
		// Outils.persistence(s1);

		Application.launch(PageLogin.class, args);
	}

}
package fr.utln.gp2;

import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Promotion;
import fr.utln.gp2.entites.Salle;
import fr.utln.gp2.entites.UE;
import fr.utln.gp2.entites.Personne.Role;
import fr.utln.gp2.pages.PageLogin;
import fr.utln.gp2.utils.AbsenceDTO;
import fr.utln.gp2.utils.NoteDTO;
import fr.utln.gp2.utils.Outils;
import fr.utln.gp2.utils.PromotionId.Type;
import fr.utln.gp2.utils.RetardDTO;
import javafx.application.Application;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;


import fr.utln.gp2.entites.Personne.Role;
import fr.utln.gp2.entites.Cours;
import fr.utln.gp2.entites.Cours.TypeC;

public class Main{
	public static void main(String[] args){

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------
		Personne prof1 = new Personne("mdp", "Martin", "Michel", Role.PROFESSEUR);
		Outils.persistence(prof1);
		Personne prof2 = new Personne("mdp", "Jacquet", "Aime", Role.PROFESSEUR);
		Outils.persistence(prof2);
		Personne prof3 = new Personne("mdp", "Sorel", "Julien", Role.PROFESSEUR);
		Outils.persistence(prof3);
		Personne prof4 = new Personne("mdp", "Rogers", "Nicolas", Role.PROFESSEUR);
		Outils.persistence(prof4);
		Personne prof5 = new Personne("mdp", "Perez", "Checo", Role.PROFESSEUR);
		Outils.persistence(prof5);
		Personne prof6 = new Personne("mdp", "Camus", "Adrien", Role.PROFESSEUR);
		Outils.persistence(prof6);
		Personne prof7 = new Personne("mdp", "Hardy", "Theo", Role.PROFESSEUR);
		Outils.persistence(prof7);
		Personne prof8 = new Personne("mdp", "Champion", "Thierry", Role.PROFESSEUR);
		Outils.persistence(prof8);
		Personne prof9 = new Personne("mdp","Bah","Tot",Role.PROFESSEUR);
		Outils.persistence(prof9);
		Personne gestionnaire1 = new Personne("mdp", "Via", "Thierry", Role.GESTIONNAIRE);
		Outils.persistence(gestionnaire1);
		Personne prof10 = new Personne("mdp","Martin","Yves",Role.PROFESSEUR);
		Outils.persistence(prof10);
		Personne prof11 = new Personne("mdp","Glotin","Herve",Role.PROFESSEUR);
		Outils.persistence(prof11);
		
		
		//-------------------------------------------------------------------------------------------------------------------------------------------------------------

		Promotion m2glotin = new Promotion(Type.MASTER,2,"Baleine",null,"hglotin",null);
		Outils.persistence(m2glotin);

		Promotion l1info = new Promotion(Type.LICENCE, 1, "Informatique", "tbah");
		Outils.persistence(l1info);
		Promotion m2info = new Promotion(Type.MASTER, 2, "Informatique", "tbah");
		Outils.persistence(m2info);
		Promotion l1proDroit = new Promotion(Type.DOCTORAT, 1, "ActivitesJuridiques", "tbah");
		Outils.persistence(l1proDroit);
		Promotion l3info = new Promotion(Type.LICENCE, 3, "Informatique", "tbah");
		Outils.persistence(l3info);
		Promotion l2math = new Promotion(Type.LICENCE, 2, "Mathematiques", "mmartin");
		Outils.persistence(l2math);
		Promotion l3phys = new Promotion(Type.LICENCE, 3, "Physique", "ajacquet");
		Outils.persistence(l3phys);
		Promotion m1eco = new Promotion(Type.MASTER, 1, "Economie", "jsorel");
		Outils.persistence(m1eco);
		Promotion m2ai = new Promotion(Type.MASTER, 2, "IntelligenceArtificielle", "nrogers");
		Outils.persistence(m2ai);
		Promotion l3chimie = new Promotion(Type.LICENCE, 3, "Chimie", "cperez");
		Outils.persistence(l3chimie);
		Promotion l1eco = new Promotion(Type.LICENCE, 1, "Economie", "acamus");
		Outils.persistence(l1eco);
		Promotion m2mathapp = new Promotion(Type.MASTER, 2, "MathsAppliquees", "thardy");
		Outils.persistence(m2mathapp);

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------

		UE electromagnetisme = UE.builder()
			.nom("Electromagnetisme")
			.responsableLogin("tbah")
			.intervenantsLogin(Arrays.asList("ajacquet"))
			.nbHeures(45)
			.build();
		Outils.persistence(electromagnetisme);

		UE optimisation = UE.builder()
			.nom("Optimisation")
			.responsableLogin("tchampio")
			.intervenantsLogin(Arrays.asList("ajacquet"))
			.nbHeures(45)
			.build();
		Outils.persistence(optimisation);

		UE droitCivil = UE.builder()
			.nom("Droit Civil")
			.responsableLogin("nrogers")
			.nbHeures(60)
			.build();
		Outils.persistence(droitCivil);

		UE droitConstitutionel = UE.builder()
			.nom("Droit Constitutionnel")
			.responsableLogin("tbah")
			.nbHeures(50)
			.build();
		Outils.persistence(droitConstitutionel);

		UE basesDeDonnees = UE.builder()
								.nom("Bases de Donnees")
								.responsableLogin("nrogers")
								.intervenantsLogin(Arrays.asList("ajacquet", "jsorel","nrogers"))
								.nbHeures(40)
								.build();

		Outils.persistence(basesDeDonnees);

		UE intelligenceArtificielle = UE.builder()
										.nom("Intelligence Artificielle")
										.responsableLogin("tbah")
										.intervenantsLogin(Arrays.asList("hglotin", "mmartin"))
										.nbHeures(55)
										.build();
		Outils.persistence(intelligenceArtificielle);

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------
		
		Salle s1 = new Salle("U001", null, 32, "Salle info tah les ouf");
		Salle s2 = new Salle("U002", null, 32, "Salle master2 tha les neuils");
		Salle s3 = new Salle("U003", null, 32, "Toilettes bro");
		Outils.persistence(s1);
		Outils.persistence(s2);
		Outils.persistence(s3);

		Outils.persistence(new Personne("prof", "Mallofre", "Prof", Role.PROFESSEUR));
		Promotion m1info = new Promotion(Type.MASTER, 1, "Informatique", "pmallofr");
		Outils.persistence(m1info);
		//-------------------------------------------------------------------------------------------------------------------------------------------------------------

		Cours c1 = new Cours(electromagnetisme, Arrays.asList(m1info, l3phys), "ajacquet", 12, 2, Date.from(LocalDate.of(2025, 4, 10).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.CM, s1);
		Cours c2 = new Cours(droitCivil, Arrays.asList(m1eco), "nrogers", 14, 3, Date.from(LocalDate.of(2025, 4, 9).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TP, s2);
		Cours c3 = new Cours(electromagnetisme, Arrays.asList(m1info), "ajacquet", 14, 2, Date.from(LocalDate.of(2025, 2, 28).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.CM, s1);
		Cours c4 = new Cours(droitCivil, Arrays.asList(m1info), "nrogers", 12, 3, Date.from(LocalDate.of(2025, 3, 7).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TP, s1);
		Cours c5 = new Cours(droitConstitutionel, Arrays.asList(m1info), "tbah", 16, 2, Date.from(LocalDate.of(2025, 3, 21).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.CM, s2);
		Cours c6 = new Cours(electromagnetisme, Arrays.asList(m1info), "ajacquet", 18, 2, Date.from(LocalDate.of(2025, 5, 15).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TP, s2);
		Cours c7 = new Cours(droitConstitutionel, Arrays.asList(l1info), "tbah", 16, 2, Date.from(LocalDate.of(2025, 3, 5).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.CM, s1);
		Cours c8 = new Cours(electromagnetisme, Arrays.asList(l1info), "ajacquet", 12, 2, Date.from(LocalDate.of(2025, 3, 12).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TP, s2);
		Cours c9 = new Cours(droitCivil, Arrays.asList(l1info), "nrogers", 18, 2, Date.from(LocalDate.of(2025, 4, 9).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.CM, s1);
		Cours c10 = new Cours(electromagnetisme, Arrays.asList(l1info), "ajacquet", 14, 2, Date.from(LocalDate.of(2025, 5, 10).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.CM, s1);
		Cours c11 = new Cours(electromagnetisme, Arrays.asList(l3phys), "ajacquet", 14, 2, Date.from(LocalDate.of(2025, 3, 3).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.CM, s1);
		Cours c12 = (new Cours(droitCivil, Arrays.asList(l3phys), "nrogers", 12, 2, Date.from(LocalDate.of(2025, 3, 24).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.CM, s2));
		Cours c13 = new Cours(droitConstitutionel, Arrays.asList(l3phys), "tbah", 14, 2, Date.from(LocalDate.of(2025, 4, 14).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TP, s1);
		Cours c14 = new Cours(electromagnetisme, Arrays.asList(l3phys), "ajacquet", 16, 2, Date.from(LocalDate.of(2025, 6, 2).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TP, s2);
		Cours c15 = new Cours(electromagnetisme,Arrays.asList(m1info,l1info,m2info,l1proDroit,l3info,l2math,l3phys,m1eco,m2ai,l3chimie,l1eco,m2mathapp),"tbah",15,2,Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()),TypeC.CM,s1);
		for(Cours c : List.of(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15)){
			Outils.persistence(c);
		}

		Outils.persistence(new Cours(basesDeDonnees, Arrays.asList(m1info), "nrogers", 16, 2, Date.from(LocalDate.of(2025, 4, 22).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.CM, s1));
		Outils.persistence(new Cours(basesDeDonnees, Arrays.asList(m1info), "nrogers", 14, 2, Date.from(LocalDate.of(2025, 4, 22).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TD, s1));
		Outils.persistence(new Cours(optimisation, Arrays.asList(m1info), "hglotin", 9, 3, Date.from(LocalDate.of(2025, 4, 24).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.CM, s1));
		Outils.persistence(new Cours(optimisation, Arrays.asList(m1info), "nrogers", 13, 4, Date.from(LocalDate.of(2025, 4, 25).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TP, s1));
		Outils.persistence(new Cours(optimisation, Arrays.asList(m1info), "tchampio", 10, 2, Date.from(LocalDate.of(2025, 4, 21).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TD, s1));
		Outils.persistence(new Cours(optimisation, Arrays.asList(m1info), "tchampio", 13, 3, Date.from(LocalDate.of(2025, 4, 25).atStartOfDay(ZoneId.systemDefault()).toInstant()), TypeC.TP, s1));
		//--------------------------------------------------------------------------------------------------------------------------------------------------------------
		


		Personne e1 = new Personne("toulon", "Pelerin", "Jeanne", Role.ETUDIANT, Arrays.asList(m1info,l1info,m2info,l1proDroit,l3info,l2math,l3phys,m1eco,m2ai,l3chimie,l1eco,m2mathapp));
		Personne e2 = new Personne("MotDePasse", "Pelerin", "Shawn", Role.ETUDIANT, Arrays.asList(m1info));
		Personne e3 = new Personne("virgule", "Lavit", "Quentin", Role.ETUDIANT,Arrays.asList(m1info));
		Personne e4 = new Personne("neuil", "Haouas", "Yacine", Role.ETUDIANT, Arrays.asList(m1info));
		Personne e5 = new Personne("tre5246", "Nguyen", "JB", Role.ETUDIANT, Arrays.asList(m1info));



		Outils.persistence(new Personne("eleve", "Mallofre", "Eleve", Role.ETUDIANT,Arrays.asList(m1info)));
		
		Outils.persistence(new Personne("gestionnaire", "Mallofre", "Gestionnaire", Role.GESTIONNAIRE));
		for(Personne e : List.of(e1, e2, e3, e4, e5)){
			Outils.persistence(e);
		}
		
		Outils.persistence(new NoteDTO(e4.getLogin(), electromagnetisme.getNom(), 20, new Date()));
		Outils.persistence(new NoteDTO(e4.getLogin(), electromagnetisme.getNom(), 14, Date.from(LocalDate.of(2025, 4, 6).atStartOfDay(ZoneId.systemDefault()).toInstant())));
		Outils.persistence(new NoteDTO(e4.getLogin(), electromagnetisme.getNom(), 9, Date.from(LocalDate.of(2025, 3, 12).atStartOfDay(ZoneId.systemDefault()).toInstant())));
		Outils.persistence(new NoteDTO(e4.getLogin(), droitCivil.getNom(), 2, new Date()));
		Outils.persistence(new NoteDTO(e4.getLogin(), electromagnetisme.getNom(), 19, new Date()));
		Outils.persistence(new NoteDTO(e4.getLogin(), electromagnetisme.getNom(), 7, Date.from(LocalDate.of(2025, 4, 6).atStartOfDay(ZoneId.systemDefault()).toInstant())));
		Outils.persistence(new NoteDTO(e4.getLogin(), electromagnetisme.getNom(), 8, Date.from(LocalDate.of(2025, 3, 12).atStartOfDay(ZoneId.systemDefault()).toInstant())));
		Outils.persistence(new NoteDTO(e4.getLogin(), droitCivil.getNom(), 6, new Date()));
		Outils.persistence(new NoteDTO(e4.getLogin(), electromagnetisme.getNom(), 20, new Date()));
		Outils.persistence(new NoteDTO(e4.getLogin(), electromagnetisme.getNom(), 14, Date.from(LocalDate.of(2025, 4, 6).atStartOfDay(ZoneId.systemDefault()).toInstant())));
		Outils.persistence(new NoteDTO(e4.getLogin(), electromagnetisme.getNom(), 9, Date.from(LocalDate.of(2025, 3, 12).atStartOfDay(ZoneId.systemDefault()).toInstant())));
		Outils.persistence(new NoteDTO(e4.getLogin(), droitCivil.getNom(), 2, new Date()));
		Outils.persistence(new NoteDTO(e4.getLogin(), electromagnetisme.getNom(), 19, new Date()));
		Outils.persistence(new NoteDTO(e4.getLogin(), electromagnetisme.getNom(), 7, Date.from(LocalDate.of(2025, 4, 6).atStartOfDay(ZoneId.systemDefault()).toInstant())));
		Outils.persistence(new NoteDTO(e4.getLogin(), electromagnetisme.getNom(), 8, Date.from(LocalDate.of(2025, 3, 12).atStartOfDay(ZoneId.systemDefault()).toInstant())));
		Outils.persistence(new NoteDTO(e4.getLogin(), droitCivil.getNom(), 6, new Date()));
		Outils.persistence(new RetardDTO(e4.getLogin(), 2L, 2));
		Outils.persistence(new RetardDTO(e4.getLogin(), 2L, 2));
		// Outils.persistence(new RetardDTO(p7.getLogin(), 2L, 2));
		// Outils.persistence(new RetardDTO(p7.getLogin(), 2L, 2));
		// Outils.persistence(new RetardDTO(p7.getLogin(), 2L, 2));
		
		Application.launch(PageLogin.class, args);
	}

//	public static Personne genererPersonne(Faker faker, Role role) {
//		String nom = faker.name().lastName();
//		String prenom = faker.name().firstName();
//		String mdp = "0";
//
//		return new Personne(mdp, nom, prenom, role);
//	}
}
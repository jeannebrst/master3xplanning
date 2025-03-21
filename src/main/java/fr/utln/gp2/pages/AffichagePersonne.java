package fr.utln.gp2.pages;

import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Personne.Role;

public class AffichagePersonne{
	public static void main(String[] args) {
		Personne p1 = new Personne("MDP", "Pelerin", "Shawn", "mail", Role.ETUDIANT);
		Personne p2 = new Personne("virgule", "Lavit", "Quentin", "mail", Role.ETUDIANT);

		p1.creation();
		p2.creation();
	}
}
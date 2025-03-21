package fr.utln.gp2.pages;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Personne.Role;
import fr.utln.gp2.ressources.PersonneRessource;

import java.io.IOException;

public class AffichagePersonne{
	public static void main(String[] args) {
		Personne p1 = new Personne("MDP", "Pelerin", "Shawn", "mail", Role.ETUDIANT);
		Personne p2 = new Personne("virgule", "Lavit", "Quentin", "mail", Role.ETUDIANT);

		p1.creation();
		p2.creation();
	}
}
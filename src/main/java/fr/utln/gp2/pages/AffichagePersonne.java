package fr.utln.gp2.pages;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utln.gp2.entites.Personne;

import java.io.IOException;

public class AffichagePersonne{
	private static final Logger logger = LoggerFactory.getLogger(Personne.class);
	public static void main(String[] args) {
		String login = "shawn";

		try{
			Personne shawn = new Personne(login);
			logger.info(shawn.getPrenom());
		} catch (RuntimeException e){
			logger.info("Erreur lors de la récupération de la personne : " + e.getMessage());
		}

	}
}
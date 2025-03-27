package fr.utln.gp2.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.arjuna.ats.internal.jdbc.drivers.modifiers.list;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.utln.gp2.entites.Cours;

public class Outils{
	private static final HttpClient client = HttpClient.newHttpClient();

	public static <T> void persistence(T obj){
		String classeNom = obj.getClass().getSimpleName().toLowerCase();
		if (classeNom.charAt(classeNom.length()-1) != 's'){
			classeNom += "s";
		}

		System.out.println("persistence :" + classeNom);

		try{
			String s = new ObjectMapper().writeValueAsString(obj);
			HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:8080/api/v1/" + classeNom))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(s))
				.build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println("Réponse : " + response.body());
		}catch(IOException | InterruptedException e){
			System.out.println("Erreur creation personne : " + e);
		}
	}

	public static void getCoursByPromo(PromotionId promoId){
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("http://localhost:8080/api/v1/cours/?promoId="+promoId.toString()))
			.GET()
			.header("Content-Type", "application/json")
			.build();

		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			.thenApply(HttpResponse::body)
			.thenAccept(response ->
				System.out.println("Réponse : " + response)
				
			)
			.exceptionally(e -> {
				e.printStackTrace();
				return null;
			});
	}
}
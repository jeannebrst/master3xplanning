package fr.utln.gp2.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.arjuna.ats.internal.jdbc.drivers.modifiers.list;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.utln.gp2.entites.Cours;
import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Promotion;
import fr.utln.gp2.entites.UE;

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
			System.out.println("Réponse : " + response.body() + "\n");
		}catch(IOException | InterruptedException e){
			System.out.println("Erreur creation personne : " + e + "\n");
		}
	}

	public static CompletableFuture<Personne> getPersonneInfo(String login) {
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("http://localhost:8080/api/v1/personnes/" + login))
			.GET()
			.header("Content-Type", "application/json")
			.build();

		// Retourner un CompletableFuture
		return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			.thenApply(HttpResponse::body)  // Récupère la réponse sous forme de chaîne
			.thenApply(response -> {
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					// Convertit la réponse JSON en objet Personne
					return objectMapper.readValue(response, Personne.class);
				} catch (Exception e) {
					System.err.println("Erreur lors de la conversion JSON: " + e.getMessage()+ "\n");
					return null;
				}
			})
			.exceptionally(e -> {
				e.printStackTrace();
				return null;
			});
	}

	public static CompletableFuture<Map<Integer, List<Cours>>> getCoursByPromo(PromotionId promoId){
		HttpRequest request = HttpRequest.newBuilder()
		.uri(URI.create("http://localhost:8080/api/v1/cours/by-promo?promoId=" + promoId.toString()))
		.GET()
		.header("Content-Type", "application/json")
		.build();

		CompletableFuture<Map<Integer, List<Cours>>> futureCoursMap = new CompletableFuture<>();

		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
		.thenApply(HttpResponse::body)
		.thenApply(response -> {
			System.out.println("GetCoursPromo: " + response + "\n");
			try {
				ObjectMapper mapper = new ObjectMapper();
				List<Cours> coursList = mapper.readValue(response, new TypeReference<List<Cours>>() {});
                        return coursList.stream()
                                .collect(Collectors.groupingBy(cours -> {
									LocalDate date = cours.getJour().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
									return date.get(WeekFields.of(Locale.FRANCE).weekOfWeekBasedYear());
								}));
			} catch (Exception e) {
				e.printStackTrace();
				futureCoursMap.completeExceptionally(e);
				return null;
			}
		})
		.thenAccept(coursMap -> {
			if (coursMap != null) {
				futureCoursMap.complete(coursMap);
			}
		})
		.exceptionally(e -> {
			e.printStackTrace();
			futureCoursMap.completeExceptionally(e);
			return null;
		});

		return futureCoursMap;
	}

	public static CompletableFuture<Map<Integer, List<Cours>>> getCoursByIntervenant(String intervenantLogin){
		HttpRequest request = HttpRequest.newBuilder()
		.uri(URI.create("http://localhost:8080/api/v1/cours/by-intervenant?intervenantLogin=" + intervenantLogin))
		.GET()
		.header("Content-Type", "application/json")
		.build();

		CompletableFuture<Map<Integer, List<Cours>>> futureCoursMap = new CompletableFuture<>();

		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
		.thenApply(HttpResponse::body)
		.thenApply(response -> {
			System.out.println("GetCoursProf: " + response + "\n");
			try {
				ObjectMapper mapper = new ObjectMapper();
				List<Cours> coursList = mapper.readValue(response, new TypeReference<List<Cours>>() {});
                        return coursList.stream()
                                .collect(Collectors.groupingBy(cours -> {
									LocalDate date = cours.getJour().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
									return date.get(WeekFields.of(Locale.FRANCE).weekOfWeekBasedYear());
								}));
			} catch (Exception e) {
				e.printStackTrace();
				futureCoursMap.completeExceptionally(e);
				return null;
			}
		})
		.thenAccept(coursMap -> {
			if (coursMap != null) {
				futureCoursMap.complete(coursMap);
			}
		})
		.exceptionally(e -> {
			e.printStackTrace();
			futureCoursMap.completeExceptionally(e);
			return null;
		});

		return futureCoursMap;
	}

	public static CompletableFuture<List<Promotion>> getAllPromo() {
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("http://localhost:8080/api/v1/promotions"))
			.GET()
			.header("Content-Type", "application/json")
			.build();

		// Retourner un CompletableFuture
		return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			.thenApply(HttpResponse::body)  // Récupère la réponse sous forme de chaîne
			.thenApply(response -> {
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					// Convertit la réponse JSON en objet Personne
					return objectMapper.readValue(response, new TypeReference<List<Promotion>>() {});
				} catch (Exception e) {
					System.err.println("Erreur lors de la conversion JSON: " + e.getMessage()+ "\n");
					return null;
				}
			})
			.exceptionally(e -> {
				e.printStackTrace();
				return null;
			});
	}

	public static CompletableFuture<List<UE>> getAllUE() {
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("http://localhost:8080/api/v1/ues"))
			.GET()
			.header("Content-Type", "application/json")
			.build();

		// Retourner un CompletableFuture
		return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			.thenApply(HttpResponse::body)  // Récupère la réponse sous forme de chaîne
			.thenApply(response -> {
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					return objectMapper.readValue(response, new TypeReference<List<UE>>() {});
				} catch (Exception e) {
					System.err.println("Erreur lors de la conversion JSON: " + e.getMessage()+ "\n");
					return null;
				}
			})
			.exceptionally(e -> {
				e.printStackTrace();
				return null;
			});
	}

	
}
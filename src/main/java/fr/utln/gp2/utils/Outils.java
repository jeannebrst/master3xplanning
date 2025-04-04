package fr.utln.gp2.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
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
import fr.utln.gp2.entites.Salle;
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

	public static CompletableFuture<List<UE>> getAllUE(){
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

	public static CompletableFuture<List<Salle>> getAllSalle(){
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("http://localhost:8080/api/v1/salles"))
			.GET()
			.header("Content-Type", "application/json")
			.build();

		// Retourner un CompletableFuture
		return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			.thenApply(HttpResponse::body)  // Récupère la réponse sous forme de chaîne
			.thenApply(response -> {
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					return objectMapper.readValue(response, new TypeReference<List<Salle>>() {});
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

	public static CompletableFuture<Map<Integer, List<Cours>>> getAllCours(){
		HttpRequest request = HttpRequest.newBuilder()
		.uri(URI.create("http://localhost:8080/api/v1/cours"))
		.GET()
		.header("Content-Type", "application/json")
		.build();

		CompletableFuture<Map<Integer, List<Cours>>> futureCoursMap = new CompletableFuture<>();

		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
		.thenApply(HttpResponse::body)
		.thenApply(response -> {
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


	public static List<Salle> getSalleByHeure(int numSemaine, Date date, int heureDebut, int duree){
		List<Cours> coursSemaine = getAllCours().join().get(numSemaine);
		if (coursSemaine == null || coursSemaine.isEmpty()) {
			return List.of();
		}

		Calendar calChoix = Calendar.getInstance();
	calChoix.setTime(date);
	int jour = calChoix.get(Calendar.DAY_OF_MONTH);
	int mois = calChoix.get(Calendar.MONTH);
	int annee = calChoix.get(Calendar.YEAR);

	return coursSemaine.stream()
		.filter(cours -> {
			Calendar calCours = Calendar.getInstance();
			calCours.setTime(cours.getJour());

			boolean memeJour = calCours.get(Calendar.DAY_OF_MONTH) == jour &&
							calCours.get(Calendar.MONTH) == mois &&
							calCours.get(Calendar.YEAR) == annee;

			if (!memeJour) return false;

			int debutCours = cours.getHeureDebut();
			int finCours = debutCours + cours.getDuree();
			int finChoix = heureDebut + duree;

			return !(finCours <= heureDebut || debutCours >= finChoix);
		})
		.map(Cours::getSalle)
		.distinct()
		.collect(Collectors.toList());
	}

	public static void supprimerCoursById(Long id){
		String idString = String.valueOf(id);
		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("http://localhost:8080/api/v1/cours/" + idString))
					.DELETE()
					.build();

			HttpClient client = HttpClient.newHttpClient();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			int status = response.statusCode();
			System.out.println("Status code: " + status);
			System.out.println("Response body: " + response.body());

			if (status == 200 || status == 204) {
				System.out.println("Cours supprimé avec succès !");
			} else if (status == 404) {
				System.out.println("Cours non trouvé.");
			} else {
				System.out.println("Erreur lors de la suppression du cours.");
			}

		} catch (IOException e) {
			System.err.println("Erreur réseau lors de la suppression du cours : " + e.getMessage());
		} catch (InterruptedException e) {
			System.err.println("La requête a été interrompue : " + e.getMessage());
			Thread.currentThread().interrupt(); // bonne pratique !
		} catch (IllegalArgumentException e) {
			System.err.println("URL invalide : " + e.getMessage());
		}
	}
}
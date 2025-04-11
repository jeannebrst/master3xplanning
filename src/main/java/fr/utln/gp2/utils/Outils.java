package fr.utln.gp2.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.utln.gp2.entites.Cours;
import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Promotion;
import fr.utln.gp2.entites.Salle;
import fr.utln.gp2.entites.UE;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** 
 * Ensemble de méthode pour intéragir avec la BDR
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Outils{
	private static final HttpClient client = HttpClient.newHttpClient();
	private static final String HEADER_NAME = "Content-Type";
	private static final String HEADER_VALUE = "application/json";

	

	/**
	 * Persiste une entité dans la BDR de façon générique.
	 * Requête HTTP POST vers l'API.
	 * 
	 * @param obj objet ,de classe T, à persister dans la BDR
	 */
		public static <T> void persistence(T obj){
			String classeNom = obj.getClass().getSimpleName();
			
			if (classeNom.endsWith("DTO")) {
				classeNom = classeNom.substring(0, classeNom.length() - 3); // Enlève "DTO"
			}
			classeNom = classeNom.toLowerCase();
			if (classeNom.charAt(classeNom.length()-1) != 's'){
				classeNom += "s";
			}

			System.out.println("persistence :" + classeNom);

			try{
				String s = new ObjectMapper().writeValueAsString(obj);
				
				HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("http://localhost:8080/api/v1/" + classeNom))
					.header(HEADER_NAME, HEADER_VALUE)
					.POST(HttpRequest.BodyPublishers.ofString(s))
					.build();

				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				System.out.println("Réponse : " + response.body() + "\n");
			}catch(IOException | InterruptedException e){
				System.out.println("Erreur creation personne : " + e + "\n");
			}
		}

	


	/**
	 * Récupère une personne par son login.
	 * Requête asynchrone HTTP GET vers l'API.
	 * 
	 * @param login login de la personne à récupérer
	 * @return CompletableFuture d'une Personne
	 */
	public static CompletableFuture<Personne> getPersonneInfo(String login) {
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("http://localhost:8080/api/v1/personnes/" + login))
			.GET()
			.header(HEADER_NAME, HEADER_VALUE)
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
					System.err.println("Erreur lors de la conversion JSON: " + e.getMessage()+ "\n" + response + "\n");
					return null;
				}
			})
			.exceptionally(e -> {
				e.printStackTrace();
				return null;
			});
	}

	/**
	 * Récupère les cours d'une promotion, groupés par semaine de l'année.
	 * Requête asynchrone HTTP GET vers l'API.
	 * 
	 * @param promoId id de la promotion dont on récupère les cours
	 * @return CompletableFuture de map où la clef est le numéro de semaine et la valeur est la liste des cours correspondants
	 */
	public static CompletableFuture<Map<Integer, List<Cours>>> getCoursByPromo(PromotionId promoId){
		HttpRequest request = HttpRequest.newBuilder()
		.uri(URI.create("http://localhost:8080/api/v1/cours/by-promo?promoId=" + promoId.toString()))
		.GET()
		.header(HEADER_NAME, HEADER_VALUE)
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

	/**
	 * Récupère les cours d'un intervenant, groupés par semaine de l'année.
	 * Requête asynchrone HTTP GET vers l'API.
	 * 
	 * @param intervenantLogin login de l'intervenant dont on récupère les cours
	 * @return CompletableFuture de map où la clef est le numéro de semaine et la valeur est la liste des cours correspondants
	 */
	public static CompletableFuture<Map<Integer, List<Cours>>> getCoursByIntervenant(String intervenantLogin){
		HttpRequest request = HttpRequest.newBuilder()
		.uri(URI.create("http://localhost:8080/api/v1/cours/by-intervenant?intervenantLogin=" + intervenantLogin))
		.GET()
		.header(HEADER_NAME, HEADER_VALUE)
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

	/**
	 * Récupère toutes les promotions.
	 * Requête asynchrone HTTP GET vers l'API.
	 * 
	 * @return CompletableFuture de la liste de toute les promotions
	 */
	public static CompletableFuture<List<Promotion>> getAllPromo() {
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("http://localhost:8080/api/v1/promotions"))
			.GET()
			.header(HEADER_NAME, HEADER_VALUE)
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

	/**
	 * Récupère toutes les UEs.
	 * Requête asynchrone HTTP GET vers l'API.
	 * 
	 * @return CompletableFuture de la liste de toute les UEs
	 */
	public static CompletableFuture<List<UE>> getAllUE(){
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("http://localhost:8080/api/v1/ues"))
			.GET()
			.header(HEADER_NAME, HEADER_VALUE)
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

	/**
	 * Récupère toutes les salles.
	 * Requête asynchrone HTTP GET vers l'API.
	 * 
	 * @return CompletableFuture de la liste de toute les salles
	 */
	public static CompletableFuture<List<Salle>> getAllSalle(){
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("http://localhost:8080/api/v1/salles"))
			.GET()
			.header(HEADER_NAME, HEADER_VALUE)
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

	/**
	 * Récupère tous les cours.
	 * Requête asynchrone HTTP GET vers l'API.
	 * 
	 * @return CompletableFuture de la liste de toute les promotions
	 */
	public static CompletableFuture<Map<Integer, List<Cours>>> getAllCours(){
		HttpRequest request = HttpRequest.newBuilder()
		.uri(URI.create("http://localhost:8080/api/v1/cours"))
		.GET()
		.header(HEADER_NAME, HEADER_VALUE)
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

	/**
	 * Récupère les salles occupées pendant le créneau d'heure choisi
	 * 
	 * @param numSemaine numéro de la semaine du créneau
	 * @param date jour du créneau
	 * @param heureDebut heure de début du créneau
	 * @param duree durée du créneau
	 * @return Liste de salles
	 */
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

	/**
	 * Supprime un cours de la BDR.
	 * Requête HTTP DELETE vers l'API.
	 * 
	 * @param id du cours à supprimer
	 */
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

	public static void modifierCours(Cours cours) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd")); // Format clair pour la date
		try {
			// Conversion de l'entité Cours en DTO
			CoursDTO coursDTO = CoursDTO.fromCours(cours);
			String jsonBody = objectMapper.writeValueAsString(coursDTO);
			System.out.println("Payload envoyé : " + jsonBody);
	
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("http://localhost:8080/api/v1/cours/" + cours.getCoursId()))
					.PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
					.header("Content-Type", "application/json")
					.build();
	
			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
	
			int status = response.statusCode();
			System.out.println("Status code: " + status);
			System.out.println("Response body: " + response.body());
	
			if (status == 200) {
				System.out.println("Cours modifié avec succès !");
			} else if (status == 404) {
				System.out.println("Cours non trouvé.");
			} else {
				System.out.println("Erreur lors de la modification du cours.");
			}
		} catch (IOException e) {
			System.err.println("Erreur réseau ou de sérialisation JSON : " + e.getMessage());
		} catch (InterruptedException e) {
			System.err.println("La requête a été interrompue : " + e.getMessage());
			Thread.currentThread().interrupt();
		} catch (IllegalArgumentException e) {
			System.err.println("URL invalide : " + e.getMessage());
		}
	}


	public static CompletableFuture<Cours> getCoursById(Long id) {
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("http://localhost:8080/api/v1/cours/" + id))
			.GET()
			.header("Content-Type", "application/json")
			.build();
	
		CompletableFuture<Cours> futureCours = new CompletableFuture<>();
	
		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			.thenApply(HttpResponse::body)
			.thenAccept(response -> {
				System.out.println("GetCoursById: " + response + "\n");
				try {
					ObjectMapper mapper = new ObjectMapper();
					Cours cours = mapper.readValue(response, Cours.class);
					futureCours.complete(cours);
				} catch (Exception e) {
					e.printStackTrace();
					futureCours.completeExceptionally(e);
				}
			})
			.exceptionally(e -> {
				e.printStackTrace();
				futureCours.completeExceptionally(e);
				return null;
			});
	
		return futureCours;
	}
}
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

	public static CompletableFuture<Map<Integer, List<Cours>>> getCoursByPromo(PromotionId promoId){
		HttpRequest request = HttpRequest.newBuilder()
		.uri(URI.create("http://localhost:8080/api/v1/cours/?promoId=" + promoId.toString()))
		.GET()
		.header("Content-Type", "application/json")
		.build();

		CompletableFuture<Map<Integer, List<Cours>>> futureCoursMap = new CompletableFuture<>();

		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
		.thenApply(HttpResponse::body)
		.thenApply(response -> {
			System.out.println("GetCoursPromo: " + response);
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
}
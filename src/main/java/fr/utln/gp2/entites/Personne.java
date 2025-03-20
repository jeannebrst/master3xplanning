package fr.utln.gp2.entites;

import jakarta.persistence.*;
import lombok.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Personne {
	private static final Logger logger = LoggerFactory.getLogger(Personne.class);
    private static final HttpClient client = HttpClient.newHttpClient();

	@Id
	private UUID id;

	@Column(name = "login", nullable = false)
	private String login;

	@Column(name = "mdp", nullable = false)
	private String hashMdp;

	private String nom;

	private String prenom;

	private String mail;

	private enum Role {
		PROFESSEUR,
		ETUDIANT,
		SECRETARIAT ,
		GESTIONNAIRE
	}
	private Role role;

	/**
	 * Constructeur qui récupère une personne via l'API.
	 * @param login Identifiant de la personne
	 * @throws RuntimeException Si la requête échoue
	 */
	public Personne(String login) throws RuntimeException{
		String url = "http://localhost:8080/api/v1/personnes/" + login;
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.GET()
				.build();

		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() == 200) {
				// Parser la réponse JSON
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode jsonNode = objectMapper.readTree(response.body());

				// Extraire les données et les assigner
				this.id = UUID.fromString(jsonNode.get("id").asText());
				this.login = jsonNode.get("login").asText();
				this.hashMdp = jsonNode.get("mdp").asText();
				this.nom = jsonNode.get("nom").asText();
				this.prenom = jsonNode.get("prenom").asText();
				this.mail = jsonNode.get("mail").asText();
				this.role = Role.valueOf(jsonNode.get("role").asText().toUpperCase());

			} else {
				throw new RuntimeException("Erreur API : " + response.statusCode());
			}
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException("Erreur lors de l'appel API : " + e.getMessage(), e);
		}
	}
}
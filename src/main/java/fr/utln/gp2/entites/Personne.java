package fr.utln.gp2.entites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.*;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Personne {
    private static final HttpClient client = HttpClient.newHttpClient();
	private static final Logger logger = LoggerFactory.getLogger(Personne.class);

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personne_seq")
	@SequenceGenerator(name="personne_seq", sequenceName = "personne_id_seq", allocationSize = 10)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Schema(hidden = true)
	@Column(name = "personne_id")
	private Long personneId;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Schema(hidden = true)
	private String login;

	@Column(nullable = false)
	private String hashMdp;

	@Column(name = "nom", nullable = false)
	private String nom;

	@Column(name = "prenom", nullable = false)
	private String prenom;

	private String mail;

	public enum Role {
		PROFESSEUR,
		ETUDIANT,
		SECRETARIAT,
		GESTIONNAIRE
	}
	private Role role;

	@ManyToMany(mappedBy = "personnes", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JsonIgnoreProperties("personnes")
	private List<Promotion> promos = new ArrayList<>();


	public Personne(String hashMdp, String nom, String prenom, String mail, Role role){
		this.hashMdp = hashMdp;
		this.nom = nom;
		this.prenom = prenom;
		this.mail = mail;
		this.role = role;
	}

	public void creation(){
		try{
			String s = new ObjectMapper().writeValueAsString(this);
			HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:8080/api/v1/personnes"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(s))
				.build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			logger.info("Réponse : " + response.body());
		}catch(IOException | InterruptedException e){
			logger.info("Erreur creation personne");
		}
	}

	@Override
	public String toString(){
		return super.toString();
	}

	public Personne(String hashMdp, String nom, String prenom, String mail, Role role, List<Promotion> promos) {
		this.hashMdp = hashMdp;
		this.nom = nom;
		this.prenom = prenom;
		this.mail = mail;
		this.role = role;
		this.promos = promos;
	}
}
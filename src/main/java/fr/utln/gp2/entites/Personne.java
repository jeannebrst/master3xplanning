package fr.utln.gp2.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
<<<<<<< HEAD

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
=======
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.*;
>>>>>>> origin/jpa

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Personne {
    private static final HttpClient client = HttpClient.newHttpClient();

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personne_seq")
	@SequenceGenerator(name="personne_seq", sequenceName = "personne_id_seq", allocationSize = 10)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Schema(hidden = true)
	private Long personne_id;

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

	public Personne(String hashMdp, String nom, String prenom, String mail, Role role){
		this.hashMdp = hashMdp;
		this.nom = nom;
		this.prenom = prenom;
		this.mail = mail;
		this.role = role;
		this.login = nom+prenom;
	}

	public void creation(){
		try{
			String s = new ObjectMapper().writeValueAsString(this);
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:8080/api/v1/personnes"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(s))
				.build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			//System.out.println("Réponse : " + response.body());
		}catch(IOException | InterruptedException e){

		}
	}

	@Override
	public String toString(){
		return super.toString();
	}

	@ManyToMany(mappedBy = "etudiants", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	private List<Promotion> promos;

	public Personne(String hashMdp, String nom, String prenom, String mail, Role role, List<Promotion> promos) {
		this.hashMdp = hashMdp;
		this.nom = nom;
		this.prenom = prenom;
		this.login = nom+prenom;
		this.mail = mail;
		this.role = role;
		if (role.equals(Role.ETUDIANT)) {
			this.promos = promos;
		}
		else {
			this.promos = null;
		}
	}
}
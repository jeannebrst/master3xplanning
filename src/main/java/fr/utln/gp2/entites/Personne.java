package fr.utln.gp2.entites;

import jakarta.persistence.*;
import lombok.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Personne {
    private static final HttpClient client = HttpClient.newHttpClient();

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "personne_seq", sequenceName = "personne_id_seq", allocationSize = 10)
	private Long id;

	@Column(name = "login", nullable = false)
	private String login;

	@Column(name = "mdp", nullable = false)
	private String hashMdp;

	private String nom;

	private String prenom;

	private String mail;

	public enum Role {
		PROFESSEUR,
		ETUDIANT,
		SECRETARIAT ,
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

}
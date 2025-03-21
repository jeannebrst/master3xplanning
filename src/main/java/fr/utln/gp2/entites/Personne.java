package fr.utln.gp2.entites;

import jakarta.persistence.*;
import lombok.*;



import java.net.http.HttpClient;

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

}
package fr.utln.gp2.entites;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Personne {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personne_seq")
	@SequenceGenerator(name="personne_seq", sequenceName = "personne_id_seq", allocationSize = 10)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Schema(hidden = true)
	private Long personne_id;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Schema(hidden = true)
	private String login;

	@Column(name = "mdp", nullable = false)
	private String hashMdp;

	@Column(name = "nom", nullable = false)
	private String nom;

	@Column(name = "prenom", nullable = false)
	private String prenom;

	private String mail;

	private enum Role {
		PROFESSEUR,
		ETUDIANT,
		SECRETARIAT ,
		GESTIONNAIRE
	}
	private Role role;

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
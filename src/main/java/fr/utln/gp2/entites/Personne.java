package fr.utln.gp2.entites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Personne {
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

	public Personne(String hashMdp, String nom, String prenom, String mail, Role role, List<Promotion> promos) {
		this.hashMdp = hashMdp;
		this.nom = nom;
		this.prenom = prenom;
		this.mail = mail;
		this.role = role;
		this.promos = promos;
	}
}
package fr.utln.gp2.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.utln.gp2.utils.PromotionId;
import jakarta.persistence.*;
import lombok.*;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.*;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Personne {
	private static final Logger logger = LoggerFactory.getLogger(Personne.class);

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personne_seq")
	@SequenceGenerator(name="personne_seq", sequenceName = "personne_id_seq", allocationSize = 10)
	// @JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Schema(hidden = true)
	@Column(name = "personne_id")
	private Long personneId;

	// @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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
	@JsonIgnoreProperties({"personnes","cours"})//Pour eviter maxi redondance
	private List<Promotion> promos = new ArrayList<>();

	@ManyToMany
	@JoinTable(
			name = "personne_ue",
			joinColumns = @JoinColumn(name = "personne_id"),
			inverseJoinColumns = @JoinColumn(name = "ue_id")
	)
	@JsonIgnore
	private List<UE> ues = new ArrayList<>();

	@OneToMany(mappedBy = "etudiant", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Note> notes = new ArrayList<>();

	@OneToMany(mappedBy = "etudiant", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Absence> absences = new ArrayList<>();


	public Personne(String mdp, String nom, String prenom, Role role){
		this.hashMdp = DigestUtils.sha256Hex(mdp);
		this.nom = nom;
		this.prenom = prenom;
		this.role = role;
		this.mail = prenom.toLowerCase()+"."+nom.toLowerCase()+"@master.com";
		if (nom.length()<7) {
			this.login = prenom.toLowerCase().charAt(0) + nom.toLowerCase();
		} else {
			this.login = prenom.toLowerCase().charAt(0) + nom.toLowerCase().substring(0,7);
		}
	}

	public Personne(String mdp, String nom, String prenom, Role role, List<Promotion> promos) {
		this.hashMdp =  DigestUtils.sha256Hex(mdp);
		this.nom = nom;
		this.prenom = prenom;
		this.role = role;
		this.promos = promos;
		this.mail = prenom.toLowerCase()+"."+nom.toLowerCase()+"@master.com";
		if (nom.length()<7) {
			this.login = prenom.toLowerCase().charAt(0) + nom.toLowerCase();
		} else {
			this.login = prenom.toLowerCase().charAt(0) + nom.toLowerCase().substring(0,7);
		}

	}

	@Override
	public String toString() {
		List<PromotionId> promosIds = new ArrayList<>();
		for (Promotion p : promos){
			promosIds.add(p.getPromoId());
		}

		return "Personne [personneId=" + personneId + ", login=" + login + ", nom=" + nom + ", prenom=" + prenom
				+ ", mail=" + mail + ", role=" + role + ", promos=" + promosIds + "]";
	}
}
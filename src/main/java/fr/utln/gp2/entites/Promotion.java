package fr.utln.gp2.entites;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.utln.gp2.utils.PromotionId;
import jakarta.persistence.*;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Builder
public class Promotion {
	@EmbeddedId
	private PromotionId promoId;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "promotion_cours",
			joinColumns = {
					@JoinColumn(name = "type_promotion", referencedColumnName = "type"),
					@JoinColumn(name = "annee_promotion", referencedColumnName = "annee"),
					@JoinColumn(name = "categorie_promotion", referencedColumnName = "categorie")
			},
			inverseJoinColumns = @JoinColumn(name = "cours_id")
	)
	@Column(name = "cours")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Schema(hidden = true)
	@JsonManagedReference
	public List<Cours> cours;

	private String responsableLogin;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name = "promotion_personnes",
			joinColumns = {
					@JoinColumn(name = "type_promotion", referencedColumnName = "type"),
					@JoinColumn(name = "annee_promotion", referencedColumnName = "annee"),
					@JoinColumn(name = "categorie_promotion", referencedColumnName = "categorie")
			},
			inverseJoinColumns = @JoinColumn(name = "personne_id")
	)
	@Column(name = "personnes")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Schema(hidden = true)
	private List<Personne> personnes = new ArrayList<>();

	protected Promotion() {}

	public Promotion(List<Cours> cours, String responsable_login) {
		this.promoId = new PromotionId();
		this.cours = cours;
		this.responsableLogin = responsable_login;
	}

}

package fr.utln.gp2.entites;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.utln.gp2.utils.PromotionId;
import jakarta.persistence.*;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.*;

@Getter
@Setter
@Entity
//@Builder
public class Promotion {
	@EmbeddedId
	private PromotionId promo_id;

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
	public List<Cours> cours;

	private Long responsable_id;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name = "promotion_etudiant",
			joinColumns = {
					@JoinColumn(name = "type_promotion", referencedColumnName = "type"),
					@JoinColumn(name = "annee_promotion", referencedColumnName = "annee"),
					@JoinColumn(name = "categorie_promotion", referencedColumnName = "categorie")
			},
			inverseJoinColumns = @JoinColumn(name = "personne_id")
	)
	@Column(name = "etudiants")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Schema(hidden = true)
	private List<Personne> etudiants = new ArrayList<>();

	protected Promotion() {}

	public Promotion(PromotionId id, List<Cours> cours, Long responsable_id) {
		this.promo_id = new PromotionId();
		this.cours = cours;
		this.responsable_id = responsable_id;
	}

}

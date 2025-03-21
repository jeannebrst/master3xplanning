package fr.utln.gp2.entites;

import fr.utln.gp2.utils.PromotionId;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Getter
@Setter
@Entity
@Builder
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
	public List<Cours> cours;

	private UUID responsable_id;

	protected Promotion() {}

	public Promotion(PromotionId id, List<Cours> cours, UUID responsable_id) {
		this.promo_id = new PromotionId();
		this.cours = cours;
		this.responsable_id = responsable_id;
	}
}

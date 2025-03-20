package fr.utln.gp2.entites;

import fr.utln.gp2.utils.PromotionId;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
public class Promotion {
	@EmbeddedId
	private PromotionId id;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
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
	private Long responsable_id;
}

package fr.utln.gp2.entites;

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
	private enum Type {
		LICENCE,
		MASTER,
		IUT,
		LICENCE_PROFESSIONNELLE,
		DOCTORAT
	}
	private Type type;

	private String categorie;

	@Id
	private String nom = type+categorie;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(
			name = "promotion_cours",
			joinColumns = @JoinColumn(name = "promotion_id"),
			inverseJoinColumns = @JoinColumn(name = "cours_id")
	)

	@Column(name = "cours")
	public List<Cours> cours;

	@OneToOne
	@JoinColumn(name = "responsable_id")
	private Personne responsable;
}

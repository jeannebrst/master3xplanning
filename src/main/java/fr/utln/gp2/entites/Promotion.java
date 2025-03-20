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
	@Id
	private String nom;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "promotion_cours",
			joinColumns = @JoinColumn(name = "promotion_id"),
			inverseJoinColumns = @JoinColumn(name = "cours_id")
	)
	public List<Cours> cours;

	@OneToOne
	@JoinColumn(name = "responsable_id")
	private Personne responsable;
}

package fr.utln.gp2.entites;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Cours {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "promotion_seq", sequenceName = "promotion_id_seq", allocationSize = 10)
	private Long id;

	//private UE ue;

	@ManyToMany(mappedBy = "cours", fetch = FetchType.LAZY)
	private List<Promotion> promos;

	private Long intervenant_id;

	//private Salle salle;

	private float heureDebut;

	private float duree;

	private Date jour;

	private enum Type {
		CM,
		TD,
		TP
	}
	private Type type;
}
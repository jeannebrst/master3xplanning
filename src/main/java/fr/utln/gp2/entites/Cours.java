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
	private UUID cours_id;

	//private UE ue;

	@ManyToMany(mappedBy = "cours", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	private List<Promotion> promos;

	private UUID intervenant_id;

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
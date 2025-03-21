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
@Entity
public class Cours {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cours_seq")
	@SequenceGenerator(name = "cours_seq", sequenceName = "cours_id_seq", allocationSize = 10)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Schema(hidden = true)
	private Long cours_id;

	//private UE ue;

	@ManyToMany(mappedBy = "cours", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JsonIgnoreProperties("cours")
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
package fr.utln.gp2.entites;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class Cours {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cours_seq")
	@SequenceGenerator(name = "cours_seq", sequenceName = "cours_id_seq", allocationSize = 10)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Schema(hidden = true)
	@Column(name = "cours_id")
	private Long coursId;

	@ManyToOne
	@JoinColumn(name = "ue_id")
	private UE ues;


	@ManyToMany(mappedBy = "cours", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JsonBackReference
	private List<Promotion> promos = new ArrayList<>();

	private String intervenantLogin;

	@ManyToOne
	@JoinColumn(name = "salle_id")
	private Salle salle;


	private float heureDebut;

	private float duree;

	private Date jour;

	public enum TypeC {
		CM,
		TD,
		TP
	}
	private TypeC type;

	public Cours(List<Promotion> promos, String intervenantLogin, float heureDebut, float duree, Date jour, TypeC type) {
		if(promos!=null){
			this.promos = promos;
		}
		this.intervenantLogin = intervenantLogin;
		this.heureDebut = heureDebut;
		this.duree = duree;
		this.jour = jour;
		this.type = type;
	}
}